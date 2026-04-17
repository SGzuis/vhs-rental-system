package com.locadora.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OmdbResponse(
    @JsonProperty("Title") String Title,
    @JsonProperty("Year") String Year,
    @JsonProperty("Rated") String Rated,
    @JsonProperty("Released") String Released,
    @JsonProperty("Runtime") String Runtime,
    @JsonProperty("Genre") String Genre,
    @JsonProperty("Director") String Director,
    @JsonProperty("Writer") String Writer,
    @JsonProperty("Actors") String Actors,
    @JsonProperty("Plot") String Plot,
    @JsonProperty("Language") String Language,
    @JsonProperty("Country") String Country,
    @JsonProperty("Awards") String Awards,
    @JsonProperty("Poster") String Poster,
    @JsonProperty("imdbRating") String imdbRating,
    @JsonProperty("imdbID") String imdbID,
    @JsonProperty("Response") String Response,
    @JsonProperty("Error") String Error
) {}
