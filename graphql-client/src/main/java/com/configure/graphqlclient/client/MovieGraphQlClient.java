package com.configure.graphqlclient.client;

import com.configure.graphqlclient.model.AddMovieRequest;
import com.configure.graphqlclient.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieGraphQlClient {

    private final HttpGraphQlClient graphQlClient;

    public Mono<List<Movie>> getAllMovies() {
        String graphQlQuery = """
                query {
                  movies {
                    id
                    title
                    year
                    genres
                    director
                  }
                }
                """;

        return this.graphQlClient.document(graphQlQuery)
                .retrieve("movies")
                .toEntityList(Movie.class);
    }

    public Mono<Movie> addMovie(AddMovieRequest request) {
        String graphQlMutation = """
                mutation AddMovie($id: ID!, $title: String!, $year: Int!, $genres: [String]!, $director: String!) {
                   addmovie (
                     id: $id
                     title: $title
                     year: $year
                     genres: $genres
                     director: $director
                   ) {
                     id
                     title
                     year
                     genres
                     director
                   }
                 }
                """;

        return this.graphQlClient.document(graphQlMutation)
                .variable("id", request.id())
                .variable("title", request.title())
                .variable("year", request.year())
                .variable("genres", request.genres())
                .variable("director", request.director())
                .retrieve("addmovie")
                .toEntity(Movie.class);
    }
}
