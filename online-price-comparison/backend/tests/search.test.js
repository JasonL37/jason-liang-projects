// loads supertest module for testing HTTP requests
const request = require("supertest");

// loads the Express app from the specified path
const app = require("../src/app");
const currencyConverter = require("../src/utils/currencyConverter");
const productService = require("../src/services/productService");

describe("GET /api/search", () => {
  const originalFetch = global.fetch;

  afterEach(() => {
    jest.restoreAllMocks();
    global.fetch = originalFetch;
    currencyConverter.resetRateCache();
  });

  test("returns 400 when the search query is missing", async () => {
    const response = await request(app).get("/api/search");

    expect(response.statusCode).toBe(400);
    expect(response.body).toEqual({
      error: "Search query cannot be empty",
    });
  });

  test("returns 400 when the search query is just whitespace", async () => {
    const response = await request(app).get("/api/search").query({ q: "   " });

    expect(response.statusCode).toBe(400);
    expect(response.body).toEqual({
      error: "Search query cannot be empty",
    });
  });

  test("returns matching products for a product name search", async () => {
    const response = await request(app).get("/api/search").query({ q: "airpods" });

    expect(response.statusCode).toBe(200);
    expect(response.body).toMatchObject({
      query: "airpods",
      count: expect.any(Number),
      results: expect.any(Array),
    });
    expect(response.body.results.length).toBeGreaterThan(0);
    expect(
      response.body.results.every((product) =>
        product.title.toLowerCase().includes("airpods")
      )
    ).toBe(true);
  });

  test("returns matching products with all the correct fields", async () => {
    const response = await request(app).get("/api/search").query({ q: "airpods" });

    expect(response.statusCode).toBe(200);

    expect(response.body.results[0]).toEqual(
      expect.objectContaining({
        id: expect.any(String),
        title: expect.any(String),
        price: expect.any(Number),
        currency: "USD",
        rating: expect.any(Number),
        reviewCount: expect.any(Number),
        store: expect.any(String),
        productUrl: expect.stringMatching(/^https?:\/\//),
        imageUrl: expect.stringMatching(/^https?:\/\//),
        shippingCost: expect.any(Number),
        isCheapest: expect.any(Boolean),
      })
    );
  });

  test("matches products case-insensitively and trims the returned query", async () => {
    const response = await request(app).get("/api/search").query({ q: "  AIRPODS  " });

    expect(response.statusCode).toBe(200);
    expect(response.body.query).toBe("AIRPODS");
    expect(response.body.results.length).toBeGreaterThan(0);
  });

  test("returns an empty results array when no products match", async () => {
    const response = await request(app).get("/api/search").query({ q: "espresso machine" });

    expect(response.statusCode).toBe(200);
    expect(response.body).toEqual({
      query: "espresso machine",
      currency: "USD",
      count: 0,
      error: "No products found",
      results: [],
    });
  });

  test("returns matching products converted to the selected currency", async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => ({
        result: "success",
        rates: {
          USD: 1,
          CAD: 1.35,
        },
      }),
    });

    const response = await request(app)
      .get("/api/search")
      .query({ q: "airpods", currency: "CAD" });

    expect(response.statusCode).toBe(200);
    expect(response.body.currency).toBe("CAD");
    expect(response.body.results.length).toBeGreaterThan(0);
    expect(response.body.results.every((product) => product.currency === "CAD")).toBe(
      true
    );
    expect(
      response.body.results.every((product) => product.originalCurrency === "USD")
    ).toBe(true);
  });

  test("returns 400 when the selected currency is unsupported", async () => {
    const response = await request(app)
      .get("/api/search")
      .query({ q: "airpods", currency: "JPY" });

    expect(response.statusCode).toBe(400);
    expect(response.body.error).toBe("Currency must be one of: USD, CAD, EUR, GBP");
  });

  test.each([
    "currency=",
    "currency=%20%20",
    "currency=USD&currency=CAD",
    "currency[code]=USD",
    "currency=US",
    "currency=__proto__",
  ])("rejects malformed currency parameters before searching: %s", async (parameter) => {
    const search = jest.spyOn(productService, "searchProducts");
    const response = await request(app).get(`/api/search?q=airpods&${parameter}`);

    expect(response.statusCode).toBe(400);
    expect(search).not.toHaveBeenCalled();
  });

  test("normalizes lowercase currency parameters", async () => {
    const response = await request(app)
      .get("/api/search")
      .query({ q: "airpods", currency: " usd " });

    expect(response.statusCode).toBe(200);
    expect(response.body.currency).toBe("USD");
    expect(response.body.results.every((product) => product.currency === "USD")).toBe(true);
  });

  test("returns a clear error when prices cannot be converted", async () => {
    const error = new Error("Unavailable currency pair");
    error.code = "CURRENCY_CONVERSION_FAILED";
    jest.spyOn(productService, "searchProducts").mockRejectedValue(error);

    const response = await request(app).get("/api/search?q=airpods&currency=CAD");

    expect(response.statusCode).toBe(502);
    expect(response.body.error).toBe(
      "Unable to convert product prices to the selected currency. Please try again later."
    );
    expect(response.body.results).toBeUndefined();
  });

  test("sorts matching products by lowest total price first", async () => {
    const response = await request(app).get("/api/search").query({ q: "airpods" });

    const totalPrices = response.body.results.map(
      (product) => product.price + product.shippingCost
    );
    const sortedTotalPrices = [...totalPrices].sort((a, b) => a - b);

    expect(totalPrices).toEqual(sortedTotalPrices);
  });

  test("sorts matching products by highest rating when sort=rating", async () => {
    const response = await request(app)
      .get("/api/search")
      .query({ q: "airpods", sort: "rating" });

    expect(response.statusCode).toBe(200);

    const ratings = response.body.results.map((product) => product.rating);
    const sortedRatings = [...ratings].sort((a, b) => b - a);

    expect(ratings).toEqual(sortedRatings);
  });

  test("includes sale price and original price when a product is on sale", async () => {
    const response = await request(app).get("/api/search").query({ q: "airpods" });

    const saleProducts = response.body.results.filter(
      (product) => product.originalPrice > product.price
    );

    expect(saleProducts.length).toBeGreaterThan(0);
    expect(saleProducts[0]).toEqual(
      expect.objectContaining({
        price: expect.any(Number),
        originalPrice: expect.any(Number),
      })
    );
  });

  test("filters matching products by minimum rating", async () => {
    const response = await request(app)
      .get("/api/search")
      .query({ q: "airpods", minRating: 4.7 });

    expect(response.statusCode).toBe(200);
    expect(response.body.results.length).toBeGreaterThan(0);
    expect(response.body.results.every((product) => product.rating >= 4.7)).toBe(true);
  });

  test("filters matching products by maximum price", async () => {
    const response = await request(app)
      .get("/api/search")
      .query({ q: "airpods", maxPrice: 180 });

    expect(response.statusCode).toBe(200);
    expect(response.body.results.length).toBeGreaterThan(0);
    expect(response.body.results.every((product) => product.price <= 180)).toBe(true);
  });

  test("filters matching products by minimum price", async () => {
    const response = await request(app)
      .get("/api/search")
      .query({ q: "airpods", minPrice: 180 });

    expect(response.statusCode).toBe(200);
    expect(response.body.results.length).toBeGreaterThan(0);
    expect(response.body.results.every((product) => product.price >= 180)).toBe(true);
  });

  test("includes shipping cost when available", async () => {
    const response = await request(app).get("/api/search").query({ q: "airpods" });

    expect(response.statusCode).toBe(200);
    expect(
      response.body.results.some((product) => typeof product.shippingCost === "number")
    ).toBe(true);
  });

  test("highlights exactly one cheapest option when results are found", async () => {
    const response = await request(app).get("/api/search").query({ q: "airpods" });

    const cheapestProducts = response.body.results.filter((product) => product.isCheapest);
    const totalPrices = response.body.results.map(
      (product) => product.price + product.shippingCost
    );
    const cheapestTotalPrice = Math.min(...totalPrices);

    expect(cheapestProducts).toHaveLength(1);
    expect(cheapestProducts[0].price + cheapestProducts[0].shippingCost).toBe(
      cheapestTotalPrice
    );
  });

  test("keeps the cheapest option highlighted when sorting by rating", async () => {
    const response = await request(app)
      .get("/api/search")
      .query({ q: "airpods", sort: "rating" });

    const highlightedProduct = response.body.results.find((product) => product.isCheapest);
    const cheapestTotalPrice = Math.min(
      ...response.body.results.map((product) => product.price + product.shippingCost)
    );

    expect(highlightedProduct.price + highlightedProduct.shippingCost).toBe(
      cheapestTotalPrice
    );
  });
});
