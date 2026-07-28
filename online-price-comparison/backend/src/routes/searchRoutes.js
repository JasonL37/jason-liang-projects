const express = require("express");
const productService = require("../services/productService");

const router = express.Router();

router.get("/", async (req, res) => {
  const query = typeof req.query.q === "string" ? req.query.q.trim() : "";

  if (!query) {
    return res.status(400).json({
      error: "Search query cannot be empty",
    });
  }

  try {
    const results = await productService.searchProducts({
      query,
      sort: req.query.sort,
      minRating: req.query.minRating,
      minPrice: req.query.minPrice,
      maxPrice: req.query.maxPrice,
    });

    return res.status(200).json({
      query,
      count: results.length,
      ...(results.length === 0 ? { error: "No products found" } : {}),
      results,
    });
  } catch (error) {
    return res.status(502).json({
      error: "Unable to retrieve products",
    });
  }
});

module.exports = router;
