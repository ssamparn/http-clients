# WebClient Module

## Purpose

`web-client` demonstrates a **reactive HTTP client** built with Spring `WebClient`.

It is the modern counterpart to the `rest-client` module and adds:

- Reactor Netty client tuning
- SSL client setup
- Resilience4j circuit breaker and retry
- Reactive REST endpoints that proxy employee service calls

## Runs on

- Port: `8082`
- Java: `25`
- Spring Boot: `4.1.0`

## External dependency

This module calls the employee REST service at:

- `http://localhost:8080/rest/employees`

## Important classes

- `src/main/java/com/configure/webclient/config/WebClientConfig.java`
- `src/main/java/com/configure/webclient/config/Resillience4jConfig.java`
- `src/main/java/com/configure/webclient/client/GetEmployeeWebClient.java`
- `src/main/java/com/configure/webclient/client/PostEmployeeWebClient.java`
- `src/main/java/com/configure/webclient/controller/WebClientTestController.java`

## Exposed endpoints

Base path: `/webclient`

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/get-employee/{employeeId}` | Fetch one employee reactively |
| `GET` | `/get-all-employee` | Fetch all employees reactively |
| `POST` | `/create-employee` | Create an employee reactively |

## Sample requests

```bash
curl http://localhost:8082/webclient/get-employee/1
curl http://localhost:8082/webclient/get-all-employee
curl -X POST http://localhost:8082/webclient/create-employee \
  -H 'Content-Type: application/json' \
  -d '{"firstName":"Grace","lastName":"Hopper","yearlyIncome":150000}'
```

## Configuration highlights

`application.yml` includes:

- `get.http.*` and `post.http.*` timeouts
- client SSL settings
- Resilience4j circuit breaker configuration
- Resilience4j retry configuration

## Test support

This module includes WireMock-based integration test scaffolding under:

- `src/test/java/com/configure/webclient/integration`

## Run locally

```bash
cd web-client
./mvnw spring-boot:run
```

## Notes

- Use this module when you want the most production-like client example in the workspace.
- If you want a synchronous example instead, check `rest-client`.

