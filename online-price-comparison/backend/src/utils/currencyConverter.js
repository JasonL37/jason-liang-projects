const DEFAULT_USD_RATES = {
  USD: 1,
  CAD: 0.74,
  EUR: 1.09,
  GBP: 1.27,
};

const DEFAULT_RATE_CACHE_TTL_MS = 12 * 60 * 60 * 1000;
const RATE_REQUEST_TIMEOUT_MS = 3000;
const currencyCodes = new Set(Intl.supportedValuesOf("currency"));
const liveRateCache = new Map();
const pendingRateRequests = new Map();

function currencyError(message) {
  const error = new Error(message);
  error.code = "CURRENCY_CONVERSION_FAILED";
  return error;
}

function normalizeCurrency(currency) {
  if (currency === undefined) {
    return "USD";
  }

  return typeof currency === "string" ? currency.trim().toUpperCase() : "";
}

function isValidRate(rate) {
  return typeof rate === "number" && Number.isFinite(rate) && rate > 0;
}

function getFallbackRates() {
  const rates = { ...DEFAULT_USD_RATES };

  try {
    const configuredRates = JSON.parse(process.env.CURRENCY_RATES_JSON || "{}");

    if (configuredRates && typeof configuredRates === "object" && !Array.isArray(configuredRates)) {
      for (const [currency, rate] of Object.entries(configuredRates)) {
        const code = normalizeCurrency(currency);

        if (currencyCodes.has(code) && isValidRate(rate)) {
          rates[code] = rate;
        }
      }
    }
  } catch (error) {
    // Invalid overrides leave the built-in fallback rates intact.
  }

  rates.USD = 1;
  return rates;
}

function getSupportedCurrencies() {
  return Object.keys(getFallbackRates());
}

function isSupportedCurrency(currency) {
  return Object.hasOwn(getFallbackRates(), normalizeCurrency(currency));
}

function validateCurrencyCode(currency) {
  const code = normalizeCurrency(currency);

  if (!currencyCodes.has(code)) {
    throw currencyError("Invalid currency code");
  }

  return code;
}

function validateAmount(amount) {
  if (typeof amount !== "number" || !Number.isFinite(amount) || amount < 0) {
    throw currencyError("Product amounts must be finite, non-negative numbers");
  }

  return amount;
}

function roundMoney(amount, currency) {
  validateAmount(amount);
  const digits = new Intl.NumberFormat("en-US", {
    style: "currency",
    currency,
  }).resolvedOptions().maximumFractionDigits;
  const factor = 10 ** digits;
  const minorUnits = Math.round((amount + Number.EPSILON) * factor);

  if (!Number.isSafeInteger(minorUnits)) {
    throw currencyError("Converted amount is too large");
  }

  return minorUnits / factor;
}

function getCacheTtlMs() {
  const ttl = Number(process.env.CURRENCY_RATE_CACHE_TTL_MS);
  return Number.isFinite(ttl) && ttl > 0 ? ttl : DEFAULT_RATE_CACHE_TTL_MS;
}

async function fetchLiveRates(baseCurrency) {
  const base = validateCurrencyCode(baseCurrency);
  const cachedRates = liveRateCache.get(base);

  if (cachedRates && cachedRates.expiresAt > Date.now()) {
    return cachedRates.rates;
  }

  if (pendingRateRequests.has(base)) {
    return pendingRateRequests.get(base);
  }

  const request = (async () => {
    const response = await fetch(`https://open.er-api.com/v6/latest/${base}`, {
      signal: AbortSignal.timeout(RATE_REQUEST_TIMEOUT_MS),
    });

    if (!response.ok) {
      throw currencyError("Exchange rate request failed");
    }

    const data = await response.json();
    const rawRates = data?.rates;

    if (
      data?.result !== "success" ||
      (data.base_code !== undefined && data.base_code !== base) ||
      !rawRates || typeof rawRates !== "object" || Array.isArray(rawRates) ||
      !Object.hasOwn(rawRates, base) || rawRates[base] !== 1
    ) {
      throw currencyError("Exchange rate response did not include usable rates");
    }

    const rates = Object.freeze(Object.fromEntries(
      Object.entries(rawRates).filter(([code, rate]) => currencyCodes.has(code) && isValidRate(rate))
    ));

    liveRateCache.set(base, { rates, expiresAt: Date.now() + getCacheTtlMs() });
    return rates;
  })();

  pendingRateRequests.set(base, request);

  try {
    return await request;
  } finally {
    pendingRateRequests.delete(base);
  }
}

async function getConversionRate(source, target) {
  if (source === target) {
    return 1;
  }

  try {
    const rates = await fetchLiveRates(source);

    if (Object.hasOwn(rates, target)) {
      return rates[target];
    }
  } catch (error) {
    // A failed or malformed response uses a validated fallback pair below.
  }

  const rates = getFallbackRates();

  if (Object.hasOwn(rates, source) && Object.hasOwn(rates, target)) {
    const rate = rates[source] / rates[target];

    if (isValidRate(rate)) {
      return rate;
    }
  }

  throw currencyError(`Unable to convert ${source} to ${target}: no valid exchange rate`);
}

async function convertAmount(amount, sourceCurrency, targetCurrency) {
  const source = validateCurrencyCode(sourceCurrency ?? "");
  const target = validateCurrencyCode(targetCurrency);
  validateAmount(amount);
  const rate = await getConversionRate(source, target);
  return roundMoney(amount * rate, target);
}

async function convertProduct(product, targetCurrency, rateSnapshot = new Map()) {
  const source = validateCurrencyCode(product.currency ?? "");
  const target = validateCurrencyCode(targetCurrency);
  const price = validateAmount(product.price);
  const shippingCost = validateAmount(product.shippingCost === undefined ? 0 : product.shippingCost);
  const originalPrice = product.originalPrice == null ? undefined : validateAmount(product.originalPrice);
  const pair = `${source}/${target}`;

  // Share the same rate, including fallback decisions, across the entire search.
  if (!rateSnapshot.has(pair)) {
    rateSnapshot.set(pair, getConversionRate(source, target));
  }

  const rate = await rateSnapshot.get(pair);

  return {
    ...product,
    price: roundMoney(price * rate, target),
    originalPrice: originalPrice === undefined ? undefined : roundMoney(originalPrice * rate, target),
    shippingCost: roundMoney(shippingCost * rate, target),
    currency: target,
    originalCurrency: source,
  };
}

function convertProducts(products, targetCurrency) {
  const rateSnapshot = new Map();
  return Promise.all(products.map((product) => convertProduct(product, targetCurrency, rateSnapshot)));
}

function resetRateCache() {
  liveRateCache.clear();
  pendingRateRequests.clear();
}

module.exports = {
  convertAmount,
  convertProduct,
  convertProducts,
  fetchLiveRates,
  getSupportedCurrencies,
  isSupportedCurrency,
  normalizeCurrency,
  resetRateCache,
};
