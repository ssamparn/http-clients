package com.configure.graphqlclient.controller;

import com.configure.graphqlclient.client.MovieGraphQlClient;
import com.configure.graphqlclient.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class MovieRestController {

    private final MovieGraphQlClient graphQlClient;

    @GetMapping(path = "/get-all-movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Movie>> getAllMovies() {
        return graphQlClient.getAllMovies();
    }

    @PostMapping(path = "/add-movie", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Movie> addMovie() {
        return graphQlClient.addMovie();
    }
}
