const express = require("express");
const productService = require("../services/productService");
const currencyConverter = require("../utils/currencyConverter");

const router = express.Router();

router.get("/", async (req, res) => {
  const query = typeof req.query.q === "string" ? req.query.q.trim() : "";

  if (!query) {
    return res.status(400).json({
      error: "Search query cannot be empty",
    });
  }

  const currency = currencyConverter.normalizeCurrency(req.query.currency);

  if (!currencyConverter.isSupportedCurrency(currency)) {
    return res.status(400).json({
      error: `Currency must be one of: ${currencyConverter
        .getSupportedCurrencies()
        .join(", ")}`,
    });
  }

  try {
    const results = await productService.searchProducts({
      query,
      sort: req.query.sort,
      minRating: req.query.minRating,
      minPrice: req.query.minPrice,
      maxPrice: req.query.maxPrice,
      currency,
    });

    return res.status(200).json({
      query,
      currency,
      count: results.length,
      ...(results.length === 0 ? { error: "No products found" } : {}),
      results,
    });
  } catch (error) {
    return res.status(502).json({
      error: error.code === "CURRENCY_CONVERSION_FAILED"
        ? "Unable to convert product prices to the selected currency. Please try again later."
        : "Unable to retrieve products",
    });
  }
});

module.exports = router;
