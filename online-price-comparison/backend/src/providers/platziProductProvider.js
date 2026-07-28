const DEFAULT_BASE_URL = "https://api.escuelajs.co/api/v1";
const DEFAULT_LIMIT = "20";

function getBaseUrl() {
  return process.env.PLATZI_API_URL || DEFAULT_BASE_URL;
}

function getFirstImage(images) {
  if (!Array.isArray(images)) {
    return "";
  }

  return images.find((image) => typeof image === "string" && image.startsWith("http")) || "";
}

function normalizePlatziProduct(product) {
  return {
    id: `platzi-${product.id}`,
    title: product.title || "",
    price: Number(product.price || 0),
    originalPrice: undefined,
    currency: "USD",
    rating: 0,
    reviewCount: 0,
    store: "Platzi Fake Store",
    productUrl: `${getBaseUrl()}/products/${product.id}`,
    imageUrl: getFirstImage(product.images),
    shippingCost: 0,
  };
}

async function searchProducts(query) {
  const url = new URL(`${getBaseUrl()}/products`);

  url.searchParams.set("title", query);
  url.searchParams.set("offset", "0");
  url.searchParams.set("limit", process.env.PLATZI_SEARCH_LIMIT || DEFAULT_LIMIT);

  const response = await fetch(url);

  if (!response.ok) {
    throw new Error(`Platzi product request failed with status ${response.status}`);
  }

  const data = await response.json();
  const products = Array.isArray(data) ? data : [];

  return products.map(normalizePlatziProduct);
}

module.exports = {
  normalizePlatziProduct,
  searchProducts,
};
