package com.configure.webclient.integration.service;

import com.configure.webclient.WebClientApplication;
import com.configure.webclient.model.Employee;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(classes = WebClientApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetEmployeeServiceIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static WireMockServer wireMock = new WireMockServer(WireMockSpring.options()
            .port(8080)
            .notifier(new ConsoleNotifier(true))
            .extensions(new ResponseTemplateTransformer(true)));

    @BeforeAll
    static void setUpClass() {
        wireMock.start();
    }

    @AfterEach
    void tearDown() {
        wireMock.resetAll();
    }

    @AfterAll
    static void tearDownClass() {
        wireMock.shutdown();
    }

    @Test
    void getEmployeeById() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/webclient/get-employee/{employeeId}")
                        .build("employeeId"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee.class);
    }
}
