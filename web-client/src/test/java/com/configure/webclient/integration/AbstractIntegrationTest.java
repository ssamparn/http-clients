package com.configure.webclient.integration;

import com.configure.webclient.WebClientApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@AutoConfigureWebTestClient
@SpringBootTest(classes = WebClientApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    private static final Path TEST_RESOURCES_DIR = Path.of("src", "test", "resources").toAbsolutePath();

    public static final WireMockServer wireMock = new WireMockServer(WireMockConfiguration.options()
            .dynamicPort()
            .usingFilesUnderDirectory(TEST_RESOURCES_DIR.toString())
            .disableRequestJournal()
            .notifier(new ConsoleNotifier(true))
            .globalTemplating(true));

    static {
        wireMock.start();
    }

    @DynamicPropertySource
    static void registerWireMockProperties(DynamicPropertyRegistry registry) {
        registry.add("get.http.url", () -> wireMock.baseUrl() + "/rest/employees");
        registry.add("post.http.url", () -> wireMock.baseUrl() + "/rest/employees");
    }

    @Autowired
    protected WebTestClient webTestClient;

    @BeforeAll
    static void registerDefaultStubs() {
        wireMock.stubFor(get(urlPathEqualTo("/rest/employees/all"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("getAllEmployees.json")));

        wireMock.stubFor(get(urlMatching("/rest/employees/[0-9]+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("getEmployeeById.json")));

        wireMock.stubFor(post(urlPathEqualTo("/rest/employees/create"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("createNewEmployee.json")));
    }


    @AfterEach
    void tearDown() {
        wireMock.resetRequests();
    }

    @AfterAll
    static void tearDownClass() {
        wireMock.shutdown();
    }
}
