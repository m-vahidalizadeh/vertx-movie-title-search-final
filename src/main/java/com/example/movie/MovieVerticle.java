package com.example.movie;

import com.example.movie.models.Movie;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

public class MovieVerticle extends AbstractVerticle {

    /**
     * A client for making asynchronous HTTP requests.
     * It's created once when the verticle starts for efficiency.
     */
    private WebClient webClient;

    /**
     * This is where you should place your TMDb API key.
     * IMPORTANT: For a real application, it's highly recommended to load this
     * from a configuration file or an environment variable instead of hardcoding it.
     */
    private static final String TMDb_API_KEY = System.getenv("TMDB_API_KEY");

    @Override
    public void start(Promise<Void> startPromise) {
        // Initialize the WebClient
        this.webClient = WebClient.create(vertx);

        // Create a router to handle web requests
        Router router = Router.router(vertx);

        // The route now includes a path parameter ':kw' for the keyword
        router.get("/api/movies/search/:kw").handler(this::handleSearch);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        System.out.println("HTTP server started on port 8080");
                    } else {
                        startPromise.fail(http.cause());
                    }
                });
    }

    /**
     * Handles the movie search request. It calls the TMDb API and returns the top 3 results.
     *
     * @param context The routing context containing request and response objects.
     */
    private void handleSearch(RoutingContext context) {
        // Extract the keyword from the URL path
        String keyword = context.pathParam("kw");

        // Best Practice: Validate user input
        if (keyword == null || keyword.isBlank()) {
            context.response()
                    .setStatusCode(400) // Bad Request
                    .putHeader("content-type", "application/json")
                    .end(new JsonObject().put("error", "Search keyword cannot be empty.").encode());
            return;
        }

        if ("YOUR_TMDB_API_KEY_HERE".equals(TMDb_API_KEY)) {
            context.response()
                    .setStatusCode(500) // Internal Server Error
                    .putHeader("content-type", "application/json")
                    .end(new JsonObject().put("error", "API Key not configured.").encode());
            return;
        }

        // Make an asynchronous call to the external TMDb API
        webClient
                .get(443, "api.themoviedb.org", "/3/search/movie")
                .ssl(true) // Use HTTPS
                .addQueryParam("api_key", TMDb_API_KEY)
                .addQueryParam("query", keyword)
                .send()
                .onSuccess(apiResponse -> {
                    if (apiResponse.statusCode() != 200) {
                        // Handle errors from the TMDb API itself (e.g., invalid key)
                        context.response()
                                .setStatusCode(apiResponse.statusCode())
                                .end(apiResponse.bodyAsString());
                        return;
                    }

                    // --- CHANGE: Process the successful response using the Movie model ---
                    JsonObject tmdbResult = apiResponse.bodyAsJsonObject();
                    JsonArray moviesArray = tmdbResult.getJsonArray("results", new JsonArray());

                    // Use Java Streams and Vert.x's JSON mapping to create a list of Movie objects.
                    // This is a more modern and type-safe approach.
                    List<Movie> topThreeMovies = moviesArray.stream()
                            .map(obj -> ((JsonObject) obj).mapTo(Movie.class)) // Deserialize each JsonObject to a Movie POJO
                            .limit(3) // Take only the first 3 results
                            .collect(Collectors.toList()); // Collect them into a List

                    // For debugging, we can now print the movie objects.
                    // The toString() method in the Movie class will provide a nice format.
                    System.out.println("--- Top 3 Movies Found ---");
                    topThreeMovies.forEach(System.out::println);
                    System.out.println("--------------------------");

                    // Send the final JSON response to the client.
                    // Vert.x can automatically serialize the List of Movie objects into a JSON array.
                    context.response()
                            .putHeader("content-type", "application/json")
                            .end(Json.encodePrettily(topThreeMovies));
                })
                .onFailure(err -> {
                    // Handle network failures (e.g., no internet connection)
                    System.err.println("Error calling TMDb API: " + err.getMessage());
                    context.response()
                            .setStatusCode(502) // Bad Gateway
                            .end(new JsonObject().put("error", "Failed to contact movie service.").encode());
                });
    }
}