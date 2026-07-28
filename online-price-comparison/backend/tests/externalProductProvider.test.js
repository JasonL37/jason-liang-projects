const externalProductProvider = require("../src/providers/externalProductProvider");

describe("externalProductProvider", () => {
  test("normalizes external API product fields into the app product shape", () => {
    const product = externalProductProvider.normalizeProduct(
      {
        product_id: 123,
        name: "Apple AirPods Pro",
        sale_price: "199.99",
        original_price: "249.99",
        currency: "USD",
        rating: "4.8",
        review_count: "1200",
        merchant: "Example Store",
        url: "https://example.com/airpods",
        thumbnail: "https://example.com/airpods.jpg",
        shipping_cost: "0",
      },
      0
    );

    expect(product).toEqual({
      id: "123",
      title: "Apple AirPods Pro",
      price: 199.99,
      originalPrice: 249.99,
      currency: "USD",
      rating: 4.8,
      reviewCount: 1200,
      store: "Example Store",
      productUrl: "https://example.com/airpods",
      imageUrl: "https://example.com/airpods.jpg",
      shippingCost: 0,
    });
  });
});
