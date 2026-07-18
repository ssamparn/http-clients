package com.configure.restclient.client.employee;

import com.configure.restclient.model.Employee;
import com.configure.restclient.properties.HttpClientConnectionProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class EmployeeServiceClient {

    private final RestClient employeeServiceRestClient;
    private final HttpClientConnectionProperties connectionProperties;

    public EmployeeServiceClient(@Qualifier("employeeServiceRestClient") RestClient employeeServiceRestClient,
                                 @Qualifier("employeeServiceHttpClientConnectionProperties") HttpClientConnectionProperties connectionProperties) {
        this.employeeServiceRestClient = employeeServiceRestClient;
        this.connectionProperties = connectionProperties;
    }

    public String getBaseUrl() {
        return connectionProperties.getUrl();
    }

    public Employee getEmployeeById(Long employeeId) {
        return Objects.requireNonNull(employeeServiceRestClient.get()
                .uri("/{id}", employeeId)
                .retrieve()
                .body(Employee.class), "Employee service returned an empty response for employee " + employeeId);
    }

    public List<Employee> getAllEmployees(int pageNumber, int pageSize) {
        Employee[] employees = employeeServiceRestClient.get()
                .uri("/all?pageNumber={pageNumber}&pageSize={pageSize}", pageNumber, pageSize)
                .retrieve()
                .body(Employee[].class);

        return employees == null ? Collections.emptyList() : Arrays.asList(employees);
    }

    public Employee createEmployee(Employee newEmployee) {
        return Objects.requireNonNull(employeeServiceRestClient.post()
                .uri("/create")
                .body(newEmployee)
                .retrieve()
                .body(Employee.class), "Employee service returned an empty response for create operation");
    }

    public Employee updateEmployee(Long employeeId, Employee employeeToBeUpdated) {
        return Objects.requireNonNull(employeeServiceRestClient.put()
                .uri("/{id}", employeeId)
                .body(employeeToBeUpdated)
                .retrieve()
                .body(Employee.class), "Employee service returned an empty response for update operation");
    }

    public void deleteEmployee(Long employeeId) {
        employeeServiceRestClient.delete()
                .uri("/{id}", employeeId)
                .retrieve()
                .toBodilessEntity();
    }
}

