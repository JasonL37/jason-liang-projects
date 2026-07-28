const DEFAULT_SCOPE = "https://api.ebay.com/oauth/api_scope";
const DEFAULT_MARKETPLACE_ID = "EBAY_US";
const DEFAULT_LIMIT = "20";

let cachedToken;

function getBaseUrl() {
  return process.env.EBAY_ENV === "sandbox"
    ? "https://api.sandbox.ebay.com"
    : "https://api.ebay.com";
}

function getCredentials() {
  const clientId = process.env.EBAY_CLIENT_ID;
  const clientSecret = process.env.EBAY_CLIENT_SECRET;

  if (!clientId || !clientSecret) {
    throw new Error("Missing eBay API credentials");
  }

  return { clientId, clientSecret };
}

function getBasicAuthHeader(clientId, clientSecret) {
  return `Basic ${Buffer.from(`${clientId}:${clientSecret}`).toString("base64")}`;
}

function isCachedTokenValid() {
  return cachedToken && cachedToken.expiresAt > Date.now() + 60 * 1000;
}

async function getAccessToken() {
  if (isCachedTokenValid()) {
    return cachedToken.value;
  }

  const { clientId, clientSecret } = getCredentials();
  const body = new URLSearchParams({
    grant_type: "client_credentials",
    scope: process.env.EBAY_OAUTH_SCOPE || DEFAULT_SCOPE,
  });

  const response = await fetch(`${getBaseUrl()}/identity/v1/oauth2/token`, {
    method: "POST",
    headers: {
      Authorization: getBasicAuthHeader(clientId, clientSecret),
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body,
  });

  if (!response.ok) {
    throw new Error(`eBay token request failed with status ${response.status}`);
  }

  const data = await response.json();

  cachedToken = {
    value: data.access_token,
    expiresAt: Date.now() + Number(data.expires_in || 0) * 1000,
  };

  return cachedToken.value;
}

function parseAmount(amount) {
  if (!amount) {
    return 0;
  }

  return Number(amount.value || amount) || 0;
}

function normalizeEbayProduct(item, index) {
  const sellerFeedbackPercentage = Number(item.seller?.feedbackPercentage);
  const rating = Number.isNaN(sellerFeedbackPercentage)
    ? 0
    : Math.round((sellerFeedbackPercentage / 20) * 10) / 10;
  const shippingOption = item.shippingOptions?.[0];

  return {
    id: String(item.itemId || `ebay-${index}`),
    title: item.title || "",
    price: parseAmount(item.price || item.currentBidPrice),
    originalPrice: item.marketingPrice?.originalPrice
      ? parseAmount(item.marketingPrice.originalPrice)
      : undefined,
    currency:
      item.price?.currency ||
      item.currentBidPrice?.currency ||
      item.marketingPrice?.originalPrice?.currency ||
      "USD",
    rating,
    reviewCount: Number(item.seller?.feedbackScore || 0),
    store: item.seller?.username || "eBay",
    productUrl: item.itemWebUrl || "",
    imageUrl: item.image?.imageUrl || "",
    shippingCost: parseAmount(shippingOption?.shippingCost),
  };
}

async function searchProducts(query) {
  const accessToken = await getAccessToken();
  const url = new URL(`${getBaseUrl()}/buy/browse/v1/item_summary/search`);

  url.searchParams.set("q", query);
  url.searchParams.set("limit", process.env.EBAY_SEARCH_LIMIT || DEFAULT_LIMIT);

  const response = await fetch(url, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
      "X-EBAY-C-MARKETPLACE-ID":
        process.env.EBAY_MARKETPLACE_ID || DEFAULT_MARKETPLACE_ID,
    },
  });

  if (!response.ok) {
    throw new Error(`eBay search request failed with status ${response.status}`);
  }

  const data = await response.json();

  return (data.itemSummaries || []).map(normalizeEbayProduct);
}

function resetTokenCache() {
  cachedToken = undefined;
}

module.exports = {
  getAccessToken,
  normalizeEbayProduct,
  resetTokenCache,
  searchProducts,
};
