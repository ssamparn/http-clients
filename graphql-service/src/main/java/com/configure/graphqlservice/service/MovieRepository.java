package com.configure.graphqlservice.service;

import com.configure.graphqlservice.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieRepository {

    private List<Movie> mockMovies;

    @PostConstruct
    public void initializeMockMovies() {
        mockMovies = new ArrayList<>(List.of(
                new Movie(1, "The Matrix", 1999, List.of("Action", "Sci-Fi"), "The Wachowskis"),
                new Movie(2, "The Matrix Reloaded", 2003, List.of("Action", "Sci-Fi"), "The Wachowskis"),
                new Movie(3, "The Matrix Revolutions", 2003, List.of("Action", "Sci-Fi"), "The Wachowskis")
        )
        );
    }

    public Movie getById(Integer id) {
        return mockMovies.stream().filter(movie -> movie.id() == (id)).findFirst().orElse(null);
    }

    public void addMovie(Movie movie) {
        mockMovies.add(movie);
    }
}
