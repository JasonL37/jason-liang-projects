const mockProductProvider = require("../providers/mockProductProvider");
const externalProductProvider = require("../providers/externalProductProvider");
const ebayProductProvider = require("../providers/ebayProductProvider");
const platziProductProvider = require("../providers/platziProductProvider");
const sortProducts = require("../utils/sortProducts");

function isPlatziEnabled() {
  return process.env.PLATZI_ENABLED === "true";
}

async function getProductListings(query) {
  const providers = [];

  if (process.env.EBAY_CLIENT_ID && process.env.EBAY_CLIENT_SECRET) {
    providers.push(() => ebayProductProvider.searchProducts(query));
  }

  if (process.env.PRODUCT_API_URL) {
    providers.push(() => externalProductProvider.searchProducts(query));
  }

  if (isPlatziEnabled()) {
    providers.push(() => platziProductProvider.searchProducts(query));
  }

  if (providers.length === 0) {
    return mockProductProvider.searchProducts(query);
  }

  const results = await Promise.allSettled(providers.map((provider) => provider()));
  const successfulResults = results.filter((result) => result.status === "fulfilled");
  const products = successfulResults.flatMap((result) => result.value);

  if (successfulResults.length > 0) {
    return products;
  }

  throw results.find((result) => result.status === "rejected").reason;
}

function parseOptionalNumber(value) {
  return value === undefined || value === null || value === "" ? NaN : Number(value);
}

function filterProducts(products, { query, minRating, minPrice, maxPrice }) {
  const normalizedQuery = query.toLowerCase();
  const parsedMinRating = parseOptionalNumber(minRating);
  const parsedMinPrice = parseOptionalNumber(minPrice);
  const parsedMaxPrice = parseOptionalNumber(maxPrice);

  return products.filter((product) => {
    const matchesQuery = product.title.toLowerCase().includes(normalizedQuery);
    const meetsMinRating =
      Number.isNaN(parsedMinRating) || product.rating >= parsedMinRating;
    const meetsMinPrice =
      Number.isNaN(parsedMinPrice) || product.price >= parsedMinPrice;
    const meetsMaxPrice =
      Number.isNaN(parsedMaxPrice) || product.price <= parsedMaxPrice;

    return matchesQuery && meetsMinRating && meetsMinPrice && meetsMaxPrice;
  });
}

function sortProductResults(products, sort) {
  if (sort === "rating") {
    return sortProducts.byHighestRating(products);
  }

  return sortProducts.byLowestTotalPrice(products);
}

function highlightCheapestProduct(products) {
  if (products.length === 0) {
    return [];
  }

  const cheapestTotalPrice = Math.min(
    ...products.map((product) => product.price + product.shippingCost)
  );
  const cheapestIndex = products.findIndex(
    (product) => product.price + product.shippingCost === cheapestTotalPrice
  );

  return products.map((product, index) => ({
    ...product,
    isCheapest: index === cheapestIndex,
  }));
}

async function searchProducts({ query, sort, minRating, minPrice, maxPrice }) {
  const products = await getProductListings(query);
  const filteredProducts = filterProducts(products, {
    query,
    minRating,
    minPrice,
    maxPrice,
  });
  const sortedProducts = sortProductResults(filteredProducts, sort);

  return highlightCheapestProduct(sortedProducts);
}

module.exports = {
  filterProducts,
  getProductListings,
  highlightCheapestProduct,
  searchProducts,
  sortProductResults,
};
