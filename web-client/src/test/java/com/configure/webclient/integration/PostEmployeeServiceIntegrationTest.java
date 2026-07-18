package com.configure.webclient.integration;

import com.configure.webclient.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class PostEmployeeServiceIntegrationTest extends AbstractIntegrationTest {

    @Test
    void createEmployee() {
        Employee employee = new Employee(1L, "Shwetank", "Kumar", 75000L);

        webTestClient.post()
                .uri("/webclient/create-employee")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employee), Employee.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Employee.class);
    }
}
