const productService = require("../src/services/productService");

describe("productService", () => {
  const originalEnv = process.env;
  const originalFetch = global.fetch;

  afterEach(() => {
    process.env = originalEnv;
    global.fetch = originalFetch;
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
});
