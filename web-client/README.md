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

## Exposed endpoints

Base path: `/webclient`

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/get-employee/{employeeId}` | Fetch one employee reactively |
| `GET` | `/get-all-employee` | Fetch all employees reactively |
| `POST` | `/create-employee` | Create an employee reactively |

## Configuration highlights

`application.yml` includes:

- `get.http.*` and `post.http.*` timeouts
- client SSL settings
- Resilience4j circuit breaker configuration
- Resilience4j retry configuration

## Notes

- Use this module when you want the most production-like client example in the workspace.
- If you want a synchronous example instead, check `rest-client`.

