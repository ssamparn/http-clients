# REST Client Module

## Purpose

`rest-client` demonstrates a synchronous Spring HTTP client using **Spring `RestClient` + Apache HttpClient 5**.

It also includes:

- a property-driven employee gateway
- façade endpoints for easy local testing
- a small **Spring Cloud Config refresh** example through `@RefreshScope`

## Runs on

- Port: `8081`
- Java: `25`
- Spring Boot: `4.1.0`

## Main responsibilities

- Build a reusable Spring `RestClient`
- Configure SSL and custom message converters
- Add interceptors and custom error handling
- Read the employee service base URL from configuration
- Expose a thin façade over the employee REST service for manual testing
- Expose a small config endpoint for refresh/config demos

## Important classes

- `src/main/java/com/configure/restclient/config/RestClientConfig.java`
- `src/main/java/com/configure/restclient/client/gateway/EmployeeApiClient.java`
- `src/main/java/com/configure/restclient/controller/EmployeeClientController.java`
- `src/main/java/com/configure/restclient/controller/ConfigController.java`
- `src/main/resources/application.yml`

## External dependency

This module is meant to call the `rest-service` module at:

- `http://localhost:8080/rest/employees`

## Exposed endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/client/employees` | List employees through the local client façade |
| `GET` | `/client/employees/{employeeId}` | Fetch one employee through the local client façade |
| `POST` | `/client/employees` | Create an employee through the local client façade |
| `PUT` | `/client/employees/{employeeId}` | Update an employee through the local client façade |
| `DELETE` | `/client/employees/{employeeId}` | Delete an employee through the local client façade |
| `GET` | `/getUrlFromConfigServer` | Returns the configured employee service URL |

## Configuration highlights

`application.yml` contains:

- `employee.service.http.url`
- connection pool and timeout settings
- trust store / key store values
- actuator refresh exposure
- `spring.cloud.config.enabled: false` for local runs

## Run locally

```bash
cd rest-client
./mvnw spring-boot:run
```

## Quick check

```bash
curl http://localhost:8081/getUrlFromConfigServer
curl http://localhost:8081/client/employees
```

## Notes

- The module now uses Spring's `RestClient` end-to-end for synchronous outbound calls.
- For new reactive flows, see the `web-client` module instead.

