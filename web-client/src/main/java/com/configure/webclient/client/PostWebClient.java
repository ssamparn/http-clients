package com.configure.webclient.client;

import com.configure.webclient.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component("postWebClient")
public class PostWebClient {

    private final WebClient webClient;

    @Autowired
    public PostWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseEntity<Employee>> createNewEmployee(Employee newEmployee) {
        return webClient.method(HttpMethod.POST)
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newEmployee), Employee.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    Mono<String> errorMsg = clientResponse.bodyToMono(String.class);
                    errorMsg.flatMap(msg -> {
                        log.error("Error message: {}", msg);
                        throw new RuntimeException(msg);
                    });
                    return Mono.error(new RuntimeException("4xx"));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    Mono<String> errorMsg = clientResponse.bodyToMono(String.class);
                    errorMsg.flatMap(msg -> {
                        log.error("Error message: {}", msg);
                        throw new RuntimeException(msg);
                    });
                    return Mono.error(new RuntimeException("5xx"));
                })
                .toEntity(Employee.class);
    }
}
