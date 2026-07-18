# REST Service Module

## Purpose

`rest-service` is the sample employee API used by the client modules.

It demonstrates:

- Spring MVC REST endpoints
- Spring Data JPA
- H2 in-memory database
- Spring Cache abstraction

## Runs on

- Port: `8080`
- Java: `25`
- Spring Boot: `4.1.0`

## Main responsibilities

- Create, fetch, update, and delete employee records
- Return paginated employee lists
- Show cache names for demo purposes
- Bootstrap a simple local persistence setup with H2

## Important classes

- `src/main/java/com/configure/restservice/web/controller/EmployeeRestController.java`
- `src/main/java/com/configure/restservice/service/EmployeeService.java`
- `src/main/java/com/configure/restservice/repository/EmployeeRepository.java`
- `src/main/resources/application.yml`

## Endpoints

Base path: `/rest/employees`

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/all?pageNumber=0&pageSize=20` | List employees |
| `GET` | `/{employeeId}` | Get one employee |
| `POST` | `/create` | Create an employee |
| `PUT` | `/{employeeId}` | Update an employee |
| `DELETE` | `/{employeeId}` | Delete an employee |
| `GET` | `/getCacheNames` | List currently configured cache names |

## Sample request

```bash
curl -X POST http://localhost:8080/rest/employees/create \
  -H 'Content-Type: application/json' \
  -d '{"firstName":"Ada","lastName":"Lovelace","yearlyIncome":120000}'
```

## Local data/config
- H2 Driver Class Name: `org.h2.Driver`
- H2 JDBC URL: `jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE`
- H2 console: `http://localhost:8080/h2-console/`
- Cache type: `simple`

## Run locally

```bash
cd rest-service
./mvnw spring-boot:run
```

## Notes

- Redis-related dependencies and config are intentionally left commented out as an optional extension point.
- This is the backing service for both `rest-client` and `web-client`.

