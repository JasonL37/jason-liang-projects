const products = require("../data/products");

async function searchProducts() {
  return products;
}

module.exports = {
  searchProducts,
};
