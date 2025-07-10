package com.example.movie.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A model class (POJO) representing a movie from The Movie Database (TMDb).
 * Using a dedicated model class provides type safety and makes the code cleaner.
 */
// This annotation tells the JSON mapper to ignore any fields in the JSON
// that we haven't defined in this class. This makes our app more resilient
// to changes in the external API.
@Getter
@Setter
@ToString(of = {"id", "title", "releaseDate"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    private long id;
    private String title;
    private String overview;

    // The @JsonProperty annotation maps a JSON key with underscores
    // to a more conventional Java camelCase field name.
    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("vote_average")
    private double voteAverage;
}