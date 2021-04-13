package com.configure.webclient.integration;

import com.configure.webclient.model.Employee;
import org.junit.jupiter.api.Test;

public class GetEmployeeServiceIntegrationTest extends AbstractIntegrationTest {

    @Test
    void getEmployeeById() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/webclient/get-employee/{employeeId}")
                        .build("employeeId"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee.class);
    }

    @Test
    void getAllEmployees_approach1() {
        webTestClient.get()
                .uri("/webclient/get-all-employee")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee[].class);
    }

    @Test
    void getAllEmployees_approach2() {
        webTestClient.get()
                .uri("/webclient/get-all-employee")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Employee.class);
    }

}
