const platziProductProvider = require("../src/providers/platziProductProvider");

describe("platziProductProvider", () => {
  const originalFetch = global.fetch;

  beforeEach(() => {
    global.fetch = jest.fn();
  });

  afterEach(() => {
    global.fetch = originalFetch;
  });

  test("normalizes Platzi products into the app product shape", () => {
    const product = platziProductProvider.normalizePlatziProduct({
      id: 12,
      title: "Classic Red Hoodie",
      price: 25,
      images: ["https://example.com/hoodie.jpg"],
    });

    expect(product).toEqual({
      id: "platzi-12",
      title: "Classic Red Hoodie",
      price: 25,
      originalPrice: undefined,
      currency: "USD",
      rating: 0,
      reviewCount: 0,
      store: "Platzi Fake Store",
      productUrl: "https://api.escuelajs.co/api/v1/products/12",
      imageUrl: "https://example.com/hoodie.jpg",
      shippingCost: 0,
    });
  });

  test("searches Platzi products by title", async () => {
    global.fetch.mockResolvedValueOnce({
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

    const results = await platziProductProvider.searchProducts("hoodie");

    expect(results).toHaveLength(1);
    expect(results[0]).toEqual(
      expect.objectContaining({
        id: "platzi-12",
        title: "Classic Red Hoodie",
        store: "Platzi Fake Store",
      })
    );
    expect(global.fetch.mock.calls[0][0].toString()).toBe(
      "https://api.escuelajs.co/api/v1/products?title=hoodie&offset=0&limit=20"
    );
  });
});
