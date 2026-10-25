const currencyConverter = require("../src/utils/currencyConverter");

describe("currencyConverter", () => {
  const originalEnv = process.env;
  const originalFetch = global.fetch;

  beforeEach(() => {
    process.env = { ...originalEnv };
    delete process.env.CURRENCY_RATES_JSON;
    delete process.env.CURRENCY_RATE_CACHE_TTL_MS;
  });

  afterEach(() => {
    jest.useRealTimers();
    process.env = originalEnv;
    global.fetch = originalFetch;
    currencyConverter.resetRateCache();
  });

  test("uses live ExchangeRate-API rates when converting currencies", async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => ({
        result: "success",
        rates: {
          USD: 1,
          CAD: 1.5,
        },
      }),
    });

    const amount = await currencyConverter.convertAmount(20, "USD", "CAD");

    expect(amount).toBe(30);
    expect(global.fetch).toHaveBeenCalledWith(
      "https://open.er-api.com/v6/latest/USD",
      expect.objectContaining({ signal: expect.any(AbortSignal) })
    );
  });

  test("caches live rates for repeated conversions with the same base currency", async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => ({
        result: "success",
        rates: {
          USD: 1,
          CAD: 1.5,
        },
      }),
    });

    await currencyConverter.convertAmount(10, "USD", "CAD");
    await currencyConverter.convertAmount(20, "USD", "CAD");

    expect(global.fetch).toHaveBeenCalledTimes(1);
  });

  test("falls back to configured static rates when live rates fail", async () => {
    process.env = {
      ...originalEnv,
      CURRENCY_RATES_JSON: JSON.stringify({
        USD: 1,
        CAD: 0.5,
      }),
    };
    global.fetch = jest.fn().mockResolvedValue({
      ok: false,
      status: 500,
    });

    const amount = await currencyConverter.convertAmount(10, "USD", "CAD");

    expect(amount).toBe(20);
  });

  test("uses one request and rate for a batch, including sale prices and shipping", async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ result: "success", rates: { USD: 1, CAD: 1.5 } }),
    });
    const product = { currency: "usd", price: 10.01, originalPrice: 20, shippingCost: 2 };
    const results = await currencyConverter.convertProducts([product, product], " cad ");

    expect(global.fetch).toHaveBeenCalledTimes(1);
    expect(results).toEqual([
      { currency: "CAD", price: 15.02, originalPrice: 30, shippingCost: 3, originalCurrency: "USD" },
      { currency: "CAD", price: 15.02, originalPrice: 30, shippingCost: 3, originalCurrency: "USD" },
    ]);
    expect(product.price).toBe(10.01);
    expect(product.currency).toBe("usd");
  });

  test("shares in-flight requests between simultaneous searches", async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ result: "success", rates: { USD: 1, CAD: 1.5, EUR: 0.9 } }),
    });

    const results = await Promise.all([
      currencyConverter.convertAmount(10, "USD", "CAD"),
      currencyConverter.convertAmount(10, "USD", "EUR"),
    ]);

    expect(results).toEqual([15, 9]);
    expect(global.fetch).toHaveBeenCalledTimes(1);
  });

  test("uses one fallback decision across a batch when the API is down", async () => {
    process.env.CURRENCY_RATES_JSON = JSON.stringify({ CAD: 0.5 });
    global.fetch = jest.fn().mockRejectedValue(new Error("Network failure"));

    const results = await currencyConverter.convertProducts([
      { currency: "USD", price: 10, originalPrice: 15, shippingCost: 2 },
      { currency: "USD", price: 20, shippingCost: 0 },
    ], "CAD");

    expect(results.map(({ price, shippingCost }) => [price, shippingCost])).toEqual([[20, 4], [40, 0]]);
    expect(results[0].originalPrice).toBe(30);
    expect(global.fetch).toHaveBeenCalledTimes(1);
  });

  test("rejects an unavailable currency pair instead of relabeling the original price", async () => {
    global.fetch = jest.fn().mockRejectedValue(new Error("Network failure"));
    const product = { currency: "JPY", price: 1000, shippingCost: 0 };

    await expect(currencyConverter.convertProduct(product, "CAD")).rejects.toMatchObject({
      code: "CURRENCY_CONVERSION_FAILED",
    });
    expect(product.currency).toBe("JPY");
    expect(product.price).toBe(1000);
  });

  test.each([undefined, null, "", "ZZZ", ["USD"], { code: "USD" }])(
    "rejects missing or invalid source currencies: %p",
    async (currency) => {
      global.fetch = jest.fn();
      await expect(currencyConverter.convertProduct({
        currency, price: 10, shippingCost: 0,
      }, "CAD")).rejects.toMatchObject({ code: "CURRENCY_CONVERSION_FAILED" });
      expect(global.fetch).not.toHaveBeenCalled();
    }
  );

  test("does not expose invalid currency codes from fallback configuration", () => {
    process.env.CURRENCY_RATES_JSON = JSON.stringify({ ZZZ: 1, INVALID: 1, JPY: 0.01 });

    expect(currencyConverter.getSupportedCurrencies()).toEqual(["USD", "CAD", "EUR", "GBP", "JPY"]);
  });

  test.each([0, -1, NaN, Infinity, "1.5", null, undefined])(
    "uses fallback when the live target rate is invalid: %p",
    async (rate) => {
      process.env.CURRENCY_RATES_JSON = JSON.stringify({ CAD: 0.5 });
      global.fetch = jest.fn().mockResolvedValue({
        ok: true,
        json: async () => ({ result: "success", rates: { USD: 1, CAD: rate } }),
      });

      await expect(currencyConverter.convertAmount(10, "USD", "CAD")).resolves.toBe(20);
    }
  );

  test.each([
    null,
    { result: "error", rates: { USD: 1, CAD: 5 } },
    { result: "success", base_code: "EUR", rates: { USD: 1, CAD: 5 } },
    { result: "success", rates: { USD: 2, CAD: 5 } },
    { result: "success", rates: [] },
  ])("rejects malformed or mismatched live responses: %p", async (data) => {
    process.env.CURRENCY_RATES_JSON = JSON.stringify({ CAD: 0.5 });
    global.fetch = jest.fn().mockResolvedValue({ ok: true, json: async () => data });

    await expect(currencyConverter.convertAmount(10, "USD", "CAD")).resolves.toBe(20);
  });

  test.each([
    "invalid json",
    "null",
    "[]",
    '{"CAD":0,"EUR":-1,"GBP":"NaN"}',
    '{"CAD":1e400}',
  ])("ignores invalid fallback configuration: %s", async (configuration) => {
    process.env.CURRENCY_RATES_JSON = configuration;
    global.fetch = jest.fn().mockRejectedValue(new Error("Offline"));

    await expect(currencyConverter.convertAmount(10, "USD", "CAD")).resolves.toBe(13.51);
  });

  test("keeps USD anchored at one in configured fallback rates", async () => {
    process.env.CURRENCY_RATES_JSON = JSON.stringify({ USD: 5, CAD: 0.5 });
    global.fetch = jest.fn().mockRejectedValue(new Error("Offline"));

    await expect(currencyConverter.convertAmount(10, "USD", "CAD")).resolves.toBe(20);
    await expect(currencyConverter.convertAmount(20, "CAD", "USD")).resolves.toBe(10);
  });

  test.each([NaN, Infinity, -1, null, undefined, "", "12", Number.MAX_VALUE])(
    "rejects invalid monetary amounts even for same-currency listings: %p",
    async (amount) => {
      await expect(currencyConverter.convertProduct({
        currency: "USD", price: amount, shippingCost: 0,
      }, "USD")).rejects.toMatchObject({ code: "CURRENCY_CONVERSION_FAILED" });
    }
  );

  test.each(["price", "originalPrice", "shippingCost"])("validates the %s field", async (field) => {
    await expect(currencyConverter.convertProduct({
      currency: "USD", price: 10, originalPrice: 20, shippingCost: 0, [field]: NaN,
    }, "USD")).rejects.toMatchObject({ code: "CURRENCY_CONVERSION_FAILED" });
  });

  test("normalizes same-currency amounts without calling the API", async () => {
    global.fetch = jest.fn();
    const product = await currencyConverter.convertProduct({
      currency: " usd ", price: 10.125, originalPrice: 15.125, shippingCost: 0,
    }, "USD");

    expect(product).toMatchObject({ currency: "USD", price: 10.13, originalPrice: 15.13 });
    expect(global.fetch).not.toHaveBeenCalled();
  });

  test("rounds to the target currency's number of minor units", async () => {
    global.fetch = jest.fn();

    await expect(currencyConverter.convertAmount(10.6, "JPY", "JPY")).resolves.toBe(11);
    await expect(currencyConverter.convertAmount(10.1236, "KWD", "KWD")).resolves.toBe(10.124);
  });

  test("refreshes rates after cache expiry", async () => {
    jest.useFakeTimers();
    process.env.CURRENCY_RATE_CACHE_TTL_MS = "1000";
    global.fetch = jest.fn()
      .mockResolvedValueOnce({ ok: true, json: async () => ({ result: "success", rates: { USD: 1, CAD: 1.5 } }) })
      .mockResolvedValueOnce({ ok: true, json: async () => ({ result: "success", rates: { USD: 1, CAD: 2 } }) });

    await expect(currencyConverter.convertAmount(10, "USD", "CAD")).resolves.toBe(15);
    jest.advanceTimersByTime(1001);
    await expect(currencyConverter.convertAmount(10, "USD", "CAD")).resolves.toBe(20);
    expect(global.fetch).toHaveBeenCalledTimes(2);
  });

  test.each(["bad", "-10", "0", "Infinity"])("uses the default cache TTL for invalid configuration: %s", async (ttl) => {
    jest.useFakeTimers();
    process.env.CURRENCY_RATE_CACHE_TTL_MS = ttl;
    global.fetch = jest.fn().mockResolvedValue({
      ok: true, json: async () => ({ result: "success", rates: { USD: 1, CAD: 1.5 } }),
    });

    await currencyConverter.convertAmount(10, "USD", "CAD");
    jest.advanceTimersByTime(1000);
    await currencyConverter.convertAmount(10, "USD", "CAD");
    expect(global.fetch).toHaveBeenCalledTimes(1);
  });
});
