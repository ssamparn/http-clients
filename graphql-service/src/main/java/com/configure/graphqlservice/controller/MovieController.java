package com.configure.graphqlservice.controller;

import com.configure.graphqlservice.model.Movie;
import com.configure.graphqlservice.service.MovieRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MovieController {

    private final MovieRepository movieRepository;
    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @QueryMapping(name = "allmovies")
    public List<Movie> getAllMovies() {
        return movieRepository.getAllMovies();
    }

    @QueryMapping(name = "moviebyid")
    public Movie getMovieById(@Argument Integer id) {
        return movieRepository.getById(id);
    }

    @MutationMapping(name = "addmovie")
    public Movie addMovie(@Argument Integer id, @Argument String title, @Argument Integer year, @Argument List<String> genres, @Argument String director) {
        Movie movie = new Movie(id, title, year, genres, director);
        movieRepository.addMovie(movie);
        return movie;
    }
}
