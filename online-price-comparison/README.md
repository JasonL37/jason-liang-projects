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
```

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
