# Online Price Comparison App

## Project Overview

The Price Comparison App allows users to search for a product and compare prices from online sources. The app displays product results with price, rating, store/source, image, shipping cost when available, and a link to the original product page.

## Features

- Search for a product by name

- Display matching product results

- Show product title, price, store/source, rating, image, and product link

- Sort products by lowest total price

- Handle loading, error, and no-result states

- Use mock product data before connecting to real product APIs

## Tech Stack

- Frontend: React

- Backend: Express.js

- Testing: Vitest + React Testing Library, Supertest, Playright

## Backend environment

Create `backend/.env` from `backend/.env.sample` and add your eBay developer
application credentials:

```env
EBAY_CLIENT_ID=your_client_id
EBAY_CLIENT_SECRET=your_client_secret
EBAY_MARKETPLACE_ID=EBAY_US
PLATZI_ENABLED=true
# Optional: override fallback currency conversion rates, expressed in USD
CURRENCY_RATES_JSON={"USD":1,"CAD":0.74,"EUR":1.09,"GBP":1.27}
# Optional: cache live exchange rates for 12 hours by default
CURRENCY_RATE_CACHE_TTL_MS=43200000
```

Currency conversion uses the free ExchangeRate-API endpoint and shares rate
requests between concurrent searches. Prices, original prices, and shipping use
the same rate within each search, rounded to the target currency's minor units.
Fallback rates are approximate USD values per unit (for example, CAD=0.74 means
one CAD is worth 0.74 USD). Overrides must be positive, finite JSON numbers; USD
always remains 1. Invalid overrides are ignored.

The search API defaults to USD when currency is omitted. Blank, repeated, object,
and unsupported currency parameters return 400. If live rates fail or take longer
than three seconds, a valid fallback pair is used. If no pair is available or a
listing contains invalid monetary values, the search returns 502 rather than
displaying incorrectly labeled prices.

## Run the app

Start the backend:

```bash
cd backend
npm run dev
```

Start the frontend in another terminal:

```bash
cd frontend
npm install
npm run dev
```
