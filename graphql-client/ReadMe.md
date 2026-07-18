# GraphQL Client Module

## Purpose

`graphql-client` consumes the local GraphQL service by using Spring's `HttpGraphQlClient` and then exposes a very small REST façade on top of it.

This makes it easy to compare:

- GraphQL service-to-service communication
- traditional REST endpoints that internally call GraphQL

## Runs on

- Port: `9091`
- Java: `25`
- Spring Boot: `4.1.0`

## External dependency

This module calls the GraphQL service at:

- `http://localhost:9090/graphql`

The endpoint is now externalized via `graphql.client.url` in `application.yml`.

## Important classes

- `src/main/java/com/configure/graphqlclient/client/MovieGraphQlClient.java`
- `src/main/java/com/configure/graphqlclient/controller/MovieRestController.java`
- `src/main/resources/application.yml`

## Exposed REST endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/get-all-movies` | Fetch all movies through GraphQL |
| `POST` | `/add-movie` | Add a movie through GraphQL using a JSON request body |

## Sample requests

```bash
curl http://localhost:9091/get-all-movies
curl -X POST http://localhost:9091/add-movie \
  -H 'Content-Type: application/json' \
  -d '{"id":6,"title":"Dark Knight","year":2010,"genres":["Action","SciFi"],"director":"Christopher Nolan"}'
```

## What the module sends to GraphQL

- A `movies` query to fetch the full list
- An `addmovie` mutation that is populated from request variables at runtime

## Run locally

Start `graphql-service` first, then run:

```bash
cd graphql-client
./mvnw spring-boot:run
```

## Notes

- The GraphQL client is now created through Spring configuration instead of being instantiated directly in application code.
- This module is a good reference if you want to see `HttpGraphQlClient` with externalized endpoint configuration and variable-driven mutations.

