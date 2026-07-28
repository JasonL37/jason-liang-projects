import React from "react";
import { createRoot } from "react-dom/client";
import {
  AlertCircle,
  ArrowUpDown,
  ExternalLink,
  Search,
  SlidersHorizontal,
  Sparkles,
  Star,
} from "lucide-react";
import "./styles.css";

const ratingOptions = [
  { label: "Any", value: "" },
  { label: "2.0+", value: "2" },
  { label: "2.5+", value: "2.5" },
  { label: "3.0+", value: "3" },
  { label: "3.5+", value: "3.5" },
  { label: "4.0+", value: "4" },
  { label: "4.5+", value: "4.5" },
];

function formatCurrency(value, currency = "USD") {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency,
  }).format(Number(value || 0));
}

function ProductImage({ product }) {
  if (product.imageUrl) {
    return <img src={product.imageUrl} alt="" loading="lazy" />;
  }

  return (
    <div className="image-fallback" aria-hidden="true">
      <Search size={28} />
    </div>
  );
}

function ProductCard({ product }) {
  const totalPrice = Number(product.price || 0) + Number(product.shippingCost || 0);
  const hasRating = Number(product.rating) > 0;

  return (
    <article className={product.isCheapest ? "product cheapest" : "product"}>
      <div className="product-image">
        <ProductImage product={product} />
      </div>
      <div className="product-body">
        <div className="product-heading">
          <h2>{product.title || "Untitled listing"}</h2>
          {product.isCheapest ? (
            <span className="cheapest-badge">
              <Sparkles size={14} />
              Cheapest
            </span>
          ) : null}
        </div>

        <div className="price-row">
          <strong>{formatCurrency(product.price, product.currency)}</strong>
          {product.originalPrice && product.originalPrice > product.price ? (
            <span>{formatCurrency(product.originalPrice, product.currency)}</span>
          ) : null}
        </div>

        <dl className="meta-grid">
          <div>
            <dt>Store</dt>
            <dd>{product.store || "Unknown"}</dd>
          </div>
          <div>
            <dt>Shipping</dt>
            <dd>{formatCurrency(product.shippingCost, product.currency)}</dd>
          </div>
          <div>
            <dt>Total</dt>
            <dd>{formatCurrency(totalPrice, product.currency)}</dd>
          </div>
          <div>
            <dt>Rating</dt>
            <dd className="rating">
              {hasRating ? (
                <>
                  <Star size={15} fill="currentColor" />
                  {Number(product.rating).toFixed(1)}
                </>
              ) : (
                "No rating"
              )}
            </dd>
          </div>
        </dl>

        <div className="product-footer">
          <span>{Number(product.reviewCount || 0).toLocaleString()} reviews</span>
          {product.productUrl ? (
            <a href={product.productUrl} target="_blank" rel="noreferrer">
              View listing
              <ExternalLink size={15} />
            </a>
          ) : null}
        </div>
      </div>
    </article>
  );
}

function App() {
  const [query, setQuery] = React.useState("airpods");
  const [sort, setSort] = React.useState("price");
  const [minRating, setMinRating] = React.useState("");
  const [minPrice, setMinPrice] = React.useState("");
  const [maxPrice, setMaxPrice] = React.useState("");
  const [history, setHistory] = React.useState([]);
  const [searchState, setSearchState] = React.useState({
    status: "idle",
    error: "",
    results: [],
    count: 0,
    searchedQuery: "",
  });

  async function runSearch(event, nextQuery = query) {
    event?.preventDefault();
    const trimmedQuery = nextQuery.trim();

    if (!trimmedQuery) {
      setSearchState((current) => ({
        ...current,
        status: "error",
        error: "Enter a product name to search.",
      }));
      return;
    }

    setSearchState((current) => ({ ...current, status: "loading", error: "" }));

    const params = new URLSearchParams({
      q: trimmedQuery,
      sort,
    });

    if (minRating) {
      params.set("minRating", minRating);
    }

    if (minPrice) {
      params.set("minPrice", minPrice);
    }

    if (maxPrice) {
      params.set("maxPrice", maxPrice);
    }

    try {
      const response = await fetch(`/api/search?${params.toString()}`);
      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.error || "Unable to retrieve products.");
      }

      setHistory((current) => [
        trimmedQuery,
        ...current.filter((item) => item.toLowerCase() !== trimmedQuery.toLowerCase()),
      ].slice(0, 5));
      setSearchState({
        status: data.results.length ? "success" : "empty",
        error: data.error || "",
        results: data.results,
        count: data.count,
        searchedQuery: data.query,
      });
    } catch (error) {
      setSearchState({
        status: "error",
        error: error.message || "Unable to retrieve products.",
        results: [],
        count: 0,
        searchedQuery: trimmedQuery,
      });
    }
  }

  function searchFromHistory(item) {
    setQuery(item);
    runSearch(undefined, item);
  }

  return (
    <main>
      <section className="search-shell">
        <div className="title-row">
          <div>
            <p className="eyebrow">Search and compare listings</p>
            <h1>Online Price Comparison</h1>
          </div>
        </div>

        <form className="search-form" onSubmit={runSearch}>
          <label className="search-input">
            <Search size={21} />
            <input
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Search products"
            />
          </label>
          <button type="submit" disabled={searchState.status === "loading"}>
            <Search size={18} />
            {searchState.status === "loading" ? "Searching" : "Search"}
          </button>
        </form>

        <div className="controls">
          <label className="sort-control">
            <ArrowUpDown size={16} />
            <select value={sort} onChange={(event) => setSort(event.target.value)}>
              <option value="price">Lowest total price</option>
              <option value="rating">Highest rating</option>
            </select>
          </label>

          <fieldset className="price-control">
            <legend>
              <SlidersHorizontal size={16} />
              Price range
            </legend>
            <label>
              <span>$</span>
              <input
                type="number"
                min="0"
                value={minPrice}
                onChange={(event) => setMinPrice(event.target.value)}
                placeholder="Min"
              />
            </label>
            <label>
              <span>$</span>
              <input
                type="number"
                min="0"
                value={maxPrice}
                onChange={(event) => setMaxPrice(event.target.value)}
                placeholder="Max"
              />
            </label>
          </fieldset>

          <label className="rating-control">
            <span>
              <Star size={16} />
              Rating
            </span>
            <select
              value={minRating}
              onChange={(event) => setMinRating(event.target.value)}
            >
              {ratingOptions.map((option) => (
                <option key={option.label} value={option.value}>
                  {option.value ? `${option.label} stars` : option.label}
                </option>
              ))}
            </select>
          </label>
        </div>

        {history.length ? (
          <div className="history">
            {history.map((item) => (
              <button key={item} type="button" onClick={() => searchFromHistory(item)}>
                {item}
              </button>
            ))}
          </div>
        ) : null}
      </section>

      <section className="results-shell" aria-live="polite">
        {searchState.status === "idle" ? (
          <div className="state-message">Search for a product to compare prices.</div>
        ) : null}

        {searchState.status === "loading" ? (
          <div className="state-message">Searching listings...</div>
        ) : null}

        {searchState.status === "error" ? (
          <div className="state-message error">
            <AlertCircle size={18} />
            {searchState.error}
          </div>
        ) : null}

        {searchState.status === "empty" ? (
          <div className="state-message">
            No products found for "{searchState.searchedQuery}".
          </div>
        ) : null}

        {searchState.status === "success" ? (
          <>
            <div className="results-summary">
              <strong>{searchState.count}</strong> results for "{searchState.searchedQuery}"
            </div>
            <div className="results-list">
              {searchState.results.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
          </>
        ) : null}
      </section>
    </main>
  );
}

createRoot(document.getElementById("root")).render(<App />);
