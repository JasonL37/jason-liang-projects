# Project Overview
The app allows users to search for a product and compares prices from online sources, showing the cheapest options along with ratings, seller/store, shipping cost, and product links.

# Functional Requirements
- the user can enter a product name into a search bar
- the system sends search query to the backend
- backend retrieves matching product listings from external sources
- system displays a list of matching products found online
- products should be sorted from cheapest to most expensive by default
- each product result includes title, price, store/source, product link, image, review count, and rating if available
- users can click a product link to view it on the original store website.
- the system displays an error message if no products are found or if the external API fails
- the user can sort results by highest rating
- the app should show the sale price of items along with their original price if the item is on sale
- the user can filter results by minimum rating
- the user can filter results by maximum price
- system should ignore invalid or empty search queries
- each product should display the shipping cost when available
- the system highlights the cheapest option
- backend validates the search query before calling external APIs
- the user can decide on the currency they want the prices of their results to show up in
- search bar should show the user's search history

# Non-functional Requirements
- the app should return results within a reasonable time, such as under 5 seconds.
- the UI should be simple and easy to use.
- the backend should normalize product data into one consistent format across platforms
- the app should handle missing product data, such as missing ratings or images
- app should display error message when a product cannot be found
- backend should cache repeated search results to reduce response time and external API usage.
- cached product results should expire after a fixed time to avoid showing stale prices.
- the product should be deployed on an accessible website
- the frontend should be hosted on a platform
