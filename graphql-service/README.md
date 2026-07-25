# GraphQL Service Module

## Purpose

`graphql-service` is a small GraphQL server that exposes movie and actor data.

It is intentionally simple and uses in-memory repositories so the GraphQL pieces stay easy to understand.

## Runs on

- Port: `9090`
- Java: `25`
- Spring Boot: `4.1.0`

## Features

- GraphQL query for all movies
- GraphQL query for a movie by id
- GraphQL query for an actor by id
- GraphQL mutation to add a movie
- GraphiQL enabled for easy local exploration

## Main GraphQL operations

### Query all movies

```graphql
{
  movies {
    id
    title
    year
    genres
    director
  }
}
```

### Query one movie

```graphql
{
  moviebyid(id: 1) {
    id
    title
    director
  }
}
```

### Add a movie

```graphql
mutation {
  addmovie(
    id: 6
    title: "Dark Knight"
    year: 2010
    genres: ["Action", "SciFi"]
    director: "Christopher Nolan"
  ) {
    id
    title
    director
  }
}
```

## Local URLs

- GraphQL endpoint: `http://localhost:9090/graphql`
- GraphiQL UI: `http://localhost:9090/graphiql`

## Notes

- Data is in-memory, so every restart resets the dataset.
- This service is consumed by the `graphql-client` module.

