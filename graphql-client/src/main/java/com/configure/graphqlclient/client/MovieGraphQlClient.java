package com.configure.graphqlclient.client;

import com.configure.graphqlclient.model.Movie;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class MovieGraphQlClient {

    private final HttpGraphQlClient graphQlClient;

    public MovieGraphQlClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:9090/graphql")
                .build();

        this.graphQlClient = HttpGraphQlClient.builder(webClient)
                .build();
    }

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

    public Mono<Movie> addMovie() {
        String graphQlMutation = """
                mutation {
                   addmovie (
                     id: 6
                     title: "Dark Knight"
                     year: 2010
                     genres: ["Action", "SciFi"]
                     director: "Christopher Nolan"
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
                .retrieve("addmovie")
                .toEntity(Movie.class);
    }
}
