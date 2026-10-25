const productService = require("../src/services/productService");
const currencyConverter = require("../src/utils/currencyConverter");
const mockProductProvider = require("../src/providers/mockProductProvider");

describe("productService", () => {
  const originalEnv = process.env;
  const originalFetch = global.fetch;

  afterEach(() => {
    jest.restoreAllMocks();
    process.env = originalEnv;
    global.fetch = originalFetch;
    currencyConverter.resetRateCache();
  });

  test("searches mock product data and highlights the cheapest result", async () => {
    const results = await productService.searchProducts({ query: "airpods" });

    const cheapestProducts = results.filter((product) => product.isCheapest);
    const cheapestTotalPrice = Math.min(
      ...results.map((product) => product.price + product.shippingCost)
    );

    expect(results.length).toBeGreaterThan(0);
    expect(cheapestProducts).toHaveLength(1);
    expect(cheapestProducts[0].price + cheapestProducts[0].shippingCost).toBe(
      cheapestTotalPrice
    );
  });

  test("sorts searched products by rating when requested", async () => {
    const results = await productService.searchProducts({
      query: "airpods",
      sort: "rating",
    });

    const ratings = results.map((product) => product.rating);
    const sortedRatings = [...ratings].sort((a, b) => b - a);

    expect(ratings).toEqual(sortedRatings);
  });

  test("filters searched products by minimum and maximum price", async () => {
    const results = await productService.searchProducts({
      query: "airpods",
      minPrice: 140,
      maxPrice: 190,
    });

    expect(results.length).toBeGreaterThan(0);
    expect(
      results.every((product) => product.price >= 140 && product.price <= 190)
    ).toBe(true);
  });

  test("converts product prices to the requested currency before sorting", async () => {
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

    const results = await productService.searchProducts({
      query: "airpods",
      currency: "CAD",
    });

    const totalPrices = results.map((product) => product.price + product.shippingCost);
    const sortedTotalPrices = [...totalPrices].sort((a, b) => a - b);

    expect(results.length).toBeGreaterThan(0);
    expect(results.every((product) => product.currency === "CAD")).toBe(true);
    expect(results.every((product) => product.originalCurrency === "USD")).toBe(true);
    expect(results[0].price).toBeCloseTo(188.99, 2);
    expect(totalPrices).toEqual(sortedTotalPrices);
  });

  test("uses enabled external product sources instead of mock data", async () => {
    global.fetch = jest.fn().mockResolvedValueOnce({
      ok: true,
      json: async () => [
        {
          id: 12,
          title: "Classic Red Hoodie",
          price: 25,
          images: ["https://example.com/hoodie.jpg"],
        },
      ],
    });
    process.env = {
      ...originalEnv,
      EBAY_CLIENT_ID: "",
      EBAY_CLIENT_SECRET: "",
      PRODUCT_API_URL: "",
      PLATZI_ENABLED: "true",
    };

    const products = await productService.getProductListings("hoodie");

    expect(products).toEqual([
      expect.objectContaining({
        id: "platzi-12",
        title: "Classic Red Hoodie",
        store: "Platzi Fake Store",
      }),
    ]);
  });

  test("sorts, filters, and highlights mixed-currency listings using converted totals", async () => {
    process.env = {
      ...originalEnv, EBAY_CLIENT_ID: "", EBAY_CLIENT_SECRET: "",
      PRODUCT_API_URL: "", PLATZI_ENABLED: "false",
    };
    jest.spyOn(mockProductProvider, "searchProducts").mockResolvedValue([
      { id: "usd", title: "Headphones", currency: "USD", price: 10, shippingCost: 2 },
      { id: "cad", title: "Headphones", currency: "CAD", price: 16, shippingCost: 0 },
    ]);
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ result: "success", rates: { USD: 1, CAD: 1.5 } }),
    });

    const results = await productService.searchProducts({ query: "headphones", currency: "CAD" });

    expect(results.map(({ id, price, shippingCost, isCheapest }) => ({
      id, price, shippingCost, isCheapest,
    }))).toEqual([
      { id: "cad", price: 16, shippingCost: 0, isCheapest: true },
      { id: "usd", price: 15, shippingCost: 3, isCheapest: false },
    ]);

    const filtered = await productService.searchProducts({
      query: "headphones", currency: "CAD", maxPrice: 15,
    });
    expect(filtered).toHaveLength(1);
    expect(filtered[0]).toMatchObject({ id: "usd", price: 15, isCheapest: true });
  });
});
