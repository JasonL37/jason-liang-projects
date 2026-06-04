const express = require("express");
const products = require("../data/products");
const sortProducts = require("../utils/sortProducts");

const router = express.Router();

router.get("/", (req, res) => {
  const query = typeof req.query.q === "string" ? req.query.q.trim() : "";

  if (!query) {
    return res.status(400).json({
      error: "Search query cannot be empty",
    });
  }

  const minRating = Number(req.query.minRating);
  const maxPrice = Number(req.query.maxPrice);
  const normalizedQuery = query.toLowerCase();
  const results = products.filter((product) =>
    product.title.toLowerCase().includes(normalizedQuery)
  );
  const filteredResults = results.filter((product) => {
    const meetsMinRating =
      Number.isNaN(minRating) || product.rating >= minRating;
    const meetsMaxPrice = Number.isNaN(maxPrice) || product.price <= maxPrice;

    return meetsMinRating && meetsMaxPrice;
  });
  const sortedResults =
    req.query.sort === "rating"
      ? sortProducts.byHighestRating(filteredResults)
      : sortProducts.byLowestTotalPrice(filteredResults);
  const cheapestTotalPrice = Math.min(
    ...sortedResults.map((product) => product.price + product.shippingCost)
  );
  const highlightedResults = sortedResults.map((product, index) => ({
    ...product,
    isCheapest:
      index ===
      sortedResults.findIndex(
        (item) => item.price + item.shippingCost === cheapestTotalPrice
      ),
  }));

  return res.status(200).json({
    query,
    count: highlightedResults.length,
    ...(highlightedResults.length === 0 ? { error: "No products found" } : {}),
    results: highlightedResults,
  });
});

module.exports = router;
