function getTotalPrice(product) {
  return product.price + (product.shippingCost || 0);
}

function byLowestTotalPrice(products) {
  return [...products].sort((a, b) => getTotalPrice(a) - getTotalPrice(b));
}

function byHighestRating(products) {
  return [...products].sort((a, b) => b.rating - a.rating);
}

module.exports = {
  byLowestTotalPrice,
  byHighestRating,
};
