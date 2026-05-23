
// loads supertest module for testing HTTP requests
const request = require("supertest");

// loads the Express app from the specified path
const app = require("../src/app");

describe ("GET/api/search", () => {
    test("should return status 400 if search query is empty", async () => {
        const response = await request(app).get("/api/search?q=");
        expect(response.statusCode).toBe(400);
        expect(response.body).toHaveProperty("error", "Search query cannot be empty");
    });

    test("should return status 200 if search query is valid", async () => {
        const response = await request(app).get("/api/search?q=airpods");
        expect(response.statusCode).toBe(200);
        expect(response.body.query).toBe("airpods");
        expect(response.body).toHaveProperty("results");
        expect(Array.isArray(response.body.results)).toBe(true);
        expect(response.body.results.length).toBeGreaterThan(0);

        const product = response.body.results[0];
        expect(product).toHaveProperty("id");
        expect(product).toHaveProperty("title");
        expect(product).toHaveProperty("price");
        expect(product).toHaveProperty("currency");
        expect(product).toHaveProperty("store");
        expect(product).toHaveProperty("productUrl");
    });

    test("should return status 200 if search query has no results", async () => {
        const response = await request(app).get("/api/search?q=nothing");
        expect(response.statusCode).toBe(200);
        expect(response.body.query).toBe("nothing");
        expect(response.body).toHaveProperty("results");
        expect(Array.isArray(response.body.results)).toBe(true);
        expect(response.body.results.length).toBe(0);
    });
});