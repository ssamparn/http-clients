package com.configure.graphqlclient.model;

import java.util.List;
import java.util.Objects;

public record AddMovieRequest(Integer id, String title, Integer year, List<String> genres, String director) {

    public AddMovieRequest {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(year, "year must not be null");
        title = requireText(title, "title");
        director = requireText(director, "director");
        genres = List.copyOf(Objects.requireNonNull(genres, "genres must not be null"));
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return trimmedValue;
    }
}

