# HTTP Clients and Services Demo Suite

A multi-module Maven workspace showing several ways to call HTTP APIs with modern Spring on **Java 25** and **Spring Boot 4.1.0**.

## Modules at a glance

| Module | Port | What it does |
| --- | --- | --- |
| `rest-service` | `8080` | Employee REST API backed by Spring MVC, JPA, cache abstraction, and H2 |
| `rest-client` | `8081` | Synchronous Spring `RestClient` façade plus a small Spring Cloud Config refresh demo |
| `web-client` | `8082` | Reactive `WebClient` client with SSL settings and Resilience4j circuit breaker + retry |
| `graphql-service` | `9090` | GraphQL server exposing movie and actor queries plus an add-movie mutation |
| `graphql-client` | `9091` | GraphQL consumer that wraps `HttpGraphQlClient` behind simple REST endpoints |

## Tech baseline

- Java 25
- Spring Boot 4.1.0
- Spring Cloud 2025.1.2
- Maven multi-module build
- Lombok 1.18.46

## Project structure

```text
http-clients/
├── rest-service/
├── rest-client/
├── web-client/
├── graphql-service/
└── graphql-client/
```

## Build everything

Run from the repository root:

```bash
mvn test
```

To package all modules:

```bash
mvn clean package
```

## Recommended local run order

1. Start `rest-service`
2. Start `graphql-service`
3. Start `rest-client`, `web-client`, and/or `graphql-client`

## Run a single module

Each module includes its own Maven wrapper, so you can run them independently:

```bash
cd rest-service && ./mvnw spring-boot:run
cd graphql-service && ./mvnw spring-boot:run
cd rest-client && ./mvnw spring-boot:run
cd web-client && ./mvnw spring-boot:run
cd graphql-client && ./mvnw spring-boot:run
```

## Quick smoke tests

### REST service

```bash
curl http://localhost:8080/rest/employees/all
curl http://localhost:8080/rest/employees/1
```

### WebClient façade

```bash
curl http://localhost:8082/webclient/get-all-employee
curl http://localhost:8082/webclient/get-employee/1
```

### RestClient façade

```bash
curl http://localhost:8081/client/employees
curl http://localhost:8081/client/employees/1
```

### GraphQL service

```bash
curl http://localhost:9090/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ movies { id title year director } }"}'
```

### GraphQL client façade

```bash
curl http://localhost:9091/get-all-movies
curl -X POST http://localhost:9091/add-movie \
  -H 'Content-Type: application/json' \
  -d '{"id":6,"title":"Dark Knight","year":2010,"genres":["Action","SciFi"],"director":"Christopher Nolan"}'
```

## Where to look next

- `rest-service/README.md`
- `rest-client/README.md`
- `web-client/README.md`
- `graphql-service/README.md`
- `graphql-client/ReadMe.md`

