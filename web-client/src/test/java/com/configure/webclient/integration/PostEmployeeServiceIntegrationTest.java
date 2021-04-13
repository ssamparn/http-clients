package com.configure.webclient.integration;

import com.configure.webclient.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class PostEmployeeServiceIntegrationTest extends AbstractIntegrationTest {

    @Test
    void createEmployee() {
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Shwetank")
                .lastName("Kumar")
                .yearlyIncome(75000L)
                .build();

        webTestClient.post()
                .uri("/webclient/create-employee")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employee), Employee.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Employee.class);
    }
}
