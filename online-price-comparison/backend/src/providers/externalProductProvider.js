function normalizeProduct(product, index) {
  return {
    id: String(product.id || product.product_id || product.url || `external-${index}`),
    title: product.title || product.name || "",
    price: Number(product.price || product.sale_price || 0),
    originalPrice:
      product.originalPrice || product.original_price || product.list_price
        ? Number(product.originalPrice || product.original_price || product.list_price)
        : undefined,
    currency: product.currency || "USD",
    rating: Number(product.rating || 0),
    reviewCount: Number(product.reviewCount || product.review_count || product.reviews || 0),
    store: product.store || product.source || product.merchant || "Unknown Store",
    productUrl: product.productUrl || product.product_url || product.url || "",
    imageUrl: product.imageUrl || product.image_url || product.thumbnail || "",
    shippingCost: Number(product.shippingCost || product.shipping_cost || 0),
  };
}

async function searchProducts(query) {
  if (!process.env.PRODUCT_API_URL) {
    return [];
  }

  const url = new URL(process.env.PRODUCT_API_URL);
  url.searchParams.set("q", query);

  const response = await fetch(url, {
    headers: {
      ...(process.env.PRODUCT_API_KEY
        ? { Authorization: `Bearer ${process.env.PRODUCT_API_KEY}` }
        : {}),
    },
  });

  if (!response.ok) {
    throw new Error(`Product API request failed with status ${response.status}`);
  }

  const data = await response.json();
  const rawProducts = Array.isArray(data) ? data : data.results || data.products || [];

  return rawProducts.map(normalizeProduct);
}

module.exports = {
  normalizeProduct,
  searchProducts,
};
