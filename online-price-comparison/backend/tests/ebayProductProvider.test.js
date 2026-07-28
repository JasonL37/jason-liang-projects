const ebayProductProvider = require("../src/providers/ebayProductProvider");

describe("ebayProductProvider", () => {
  const originalEnv = process.env;
  const originalFetch = global.fetch;

  beforeEach(() => {
    jest.resetModules();
    process.env = {
      ...originalEnv,
      EBAY_CLIENT_ID: "client-id",
      EBAY_CLIENT_SECRET: "client-secret",
      EBAY_MARKETPLACE_ID: "EBAY_US",
    };
    ebayProductProvider.resetTokenCache();
    global.fetch = jest.fn();
  });

  afterEach(() => {
    process.env = originalEnv;
    global.fetch = originalFetch;
  });

  test("normalizes eBay item summary fields into the app product shape", () => {
    const product = ebayProductProvider.normalizeEbayProduct(
      {
        itemId: "v1|123|0",
        title: "Apple AirPods Pro 2nd Generation",
        price: {
          value: "189.99",
          currency: "USD",
        },
        marketingPrice: {
          originalPrice: {
            value: "249.99",
            currency: "USD",
          },
        },
        seller: {
          username: "example-seller",
          feedbackPercentage: "98.6",
          feedbackScore: 1234,
        },
        itemWebUrl: "https://www.ebay.com/itm/123",
        image: {
          imageUrl: "https://i.ebayimg.com/images/example.jpg",
        },
        shippingOptions: [
          {
            shippingCost: {
              value: "4.99",
              currency: "USD",
            },
          },
        ],
      },
      0
    );

    expect(product).toEqual({
      id: "v1|123|0",
      title: "Apple AirPods Pro 2nd Generation",
      price: 189.99,
      originalPrice: 249.99,
      currency: "USD",
      rating: 4.9,
      reviewCount: 1234,
      store: "example-seller",
      productUrl: "https://www.ebay.com/itm/123",
      imageUrl: "https://i.ebayimg.com/images/example.jpg",
      shippingCost: 4.99,
    });
  });

  test("gets an OAuth token and searches eBay Browse API", async () => {
    global.fetch
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          access_token: "access-token",
          expires_in: 7200,
        }),
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({
          itemSummaries: [
            {
              itemId: "v1|123|0",
              title: "Apple AirPods Pro",
              price: {
                value: "199.99",
                currency: "USD",
              },
              seller: {
                username: "ebay-seller",
              },
              itemWebUrl: "https://www.ebay.com/itm/123",
            },
          ],
        }),
      });

    const results = await ebayProductProvider.searchProducts("airpods");

    expect(results).toHaveLength(1);
    expect(results[0]).toEqual(
      expect.objectContaining({
        id: "v1|123|0",
        title: "Apple AirPods Pro",
        price: 199.99,
        store: "ebay-seller",
      })
    );
    expect(global.fetch).toHaveBeenCalledTimes(2);
    expect(global.fetch.mock.calls[0][0]).toBe(
      "https://api.ebay.com/identity/v1/oauth2/token"
    );
    expect(global.fetch.mock.calls[1][0].toString()).toBe(
      "https://api.ebay.com/buy/browse/v1/item_summary/search?q=airpods&limit=20"
    );
    expect(global.fetch.mock.calls[1][1].headers).toMatchObject({
      Authorization: "Bearer access-token",
      "X-EBAY-C-MARKETPLACE-ID": "EBAY_US",
    });
  });
});
