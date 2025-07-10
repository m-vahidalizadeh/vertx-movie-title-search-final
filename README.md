# Vert.x Movie Title Search

A high-performance, non-blocking movie search API built with Java and the Vert.x toolkit. This project provides a simple REST endpoint to search for movies using The Movie Database (TMDb) API.

Created by **Vahid**.

## Project Description

This application functions as a simple microservice that exposes a single REST API endpoint: `/api/movies/search/:keyword`.

When a request is made to this endpoint, the service performs the following actions:
1.  It makes an asynchronous HTTP call to the official The Movie Database (TMDb) API with the provided search keyword.
2.  It receives the search results as a JSON payload.
3.  It parses the response, deserializing the data into type-safe Java `Movie` objects.
4.  It selects the top 3 most relevant results from the list.
5.  It sends these top 3 results back to the original caller as a clean JSON array.

This demonstrates a common pattern for creating a backend-for-frontend (BFF) or an API gateway service using the reactive, non-blocking model of Vert.x.

## Features

*   **Asynchronous:** Built on Vert.x to handle many concurrent requests efficiently.
*   **RESTful:** Provides a clean, easy-to-use API endpoint for searching.
*   **Modern Java:** Uses modern Java features (JDK 17) and a type-safe data model (`Movie.java`) with Lombok.
*   **Well-Managed:** Dependencies and build process are managed with Maven, including a Bill of Materials (BOM) for version consistency.

## Prerequisites

Before you begin, ensure you have the following installed:
*   **JDK 17** (or a newer LTS version)
*   **Apache Maven**

## Setup and Configuration

This project requires an API key from The Movie Database (TMDb) to function. The key is loaded from an environment variable for security, preventing secrets from being hardcoded in the source code.

### 1. Get Your TMDb API Key

1.  Go to themoviedb.org and create a free account.
2.  After logging in, go to your account **Settings**.
3.  In the left-hand menu, click on the **API** section.
4.  Follow the instructions to request an API key for a "Developer" use case. It's an instant process.
5.  Once you have your key (it will be a long string of letters and numbers), copy it.

### 2. Configure the API Key Environment Variable

You must set the `TMDB_API_KEY` environment variable to the value you obtained from TMDb. Below are instructions for the most common development scenarios.

#### Option A: IntelliJ IDEA (Recommended for Development)

1.  In the top menu, go to **Run** -> **Edit Configurations...**.
2.  Select the run configuration for this application (it might be named `App` or the project name).
3.  In the configuration window, find the **Environment variables** field and click the icon to the right to open the editor.
4.  Click the **+** (plus) icon to add a new variable:
    *   **Name:** `TMDB_API_KEY`
    *   **Value:** `your_actual_api_key_here` (paste your key here)
5.  Click **OK** to save the variable, then **Apply** and **OK** to save the run configuration.

Now, whenever you run or debug the application from IntelliJ, the API key will be available to the program.

#### Option B: Command Line

You can also provide the environment variable directly from the command line when you run the application.

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
