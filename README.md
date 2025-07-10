# Vert.x Movie Title Search

A high-performance, non-blocking movie search API built with Java and the Vert.x toolkit. This project provides a simple REST endpoint to search for movies using The Movie Database (TMDb) API.

Created by **Vahid**.

## Project Description

This application functions as a simple microservice that exposes a single REST API endpoint: `/api/movies/search/:keyword`.

When a request is made to this endpoint, the service performs the following actions:
1. It makes an asynchronous HTTP call to the official The Movie Database (TMDb) API with the provided search keyword.
2. It receives the search results as a JSON payload.
3. It parses the response, deserializing the data into type-safe Java `Movie` objects.
4. It selects the top 3 most relevant results from the list.
5. It sends these top 3 results back to the original caller as a clean JSON array.

This demonstrates a common pattern for creating a backend-for-frontend (BFF) or an API gateway service using the reactive, non-blocking model of Vert.x.

## Features

* **Asynchronous:** Built on Vert.x to handle many concurrent requests efficiently.
* **RESTful:** Provides a clean, easy-to-use API endpoint for searching.
* **Modern Java:** Uses modern Java features (JDK 17) and a type-safe data model (`Movie.java`) with Lombok.
* **Well-Managed:** Dependencies and build process are managed with Maven, including a Bill of Materials (BOM) for version consistency.

## Prerequisites

Before you begin, ensure you have the following installed:
* **JDK 17** (or a newer LTS version)
* **Apache Maven**

## Setup and Configuration

### 1. Get Your TMDb API Key

1. Go to [themoviedb.org](https://www.themoviedb.org) and create a free account.
2. After logging in, go to your account **Settings**.
3. In the left-hand menu, click on the **API** section.
4. Follow the instructions to request an API key for a "Developer" use case. It's an instant process.
5. Once you have your key (it will be a long string of letters and numbers), copy it.

### 2. Add the API Key to the Project

1. Open the project in your favorite IDE.
2. Navigate to the following file:
   `src/main/java/com/example/movie/MovieVerticle.java`
3. Find the `TMDb_API_KEY` constant near the top of the class.
4. Replace the placeholder string with the actual API key you just copied.

## Running the Server

To launch the HTTP server on `localhost:8080`, run the following Maven command:

```bash
mvn compile exec:java -Dexec.mainClass=com.example.movie.App
```

You should see a message indicating the server has started on port 8080.

## Testing the API Locally

Once the server is running, you can test the movie search endpoint using your browser, Postman, or curl.

Example request:

```
http://localhost:8080/api/movies/search/Matrix
```

Expected result:

```json
[
  {
    "title": "The Matrix",
    "overview": "A computer hacker learns from mysterious rebels...",
    "releaseDate": "1999-03-30"
  },
  {
    "title": "The Matrix Reloaded",
    "overview": "...",
    "releaseDate": "2003-05-15"
  },
  {
    "title": "The Matrix Revolutions",
    "overview": "...",
    "releaseDate": "2003-11-05"
  }
]
```

## License

This project is provided for educational purposes. No affiliation with TMDb.
