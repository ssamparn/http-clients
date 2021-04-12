package com.configure.webclient.client;

import com.configure.webclient.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component("getWebClient")
public class GetEmployeeWebClient {

    private final WebClient webClient;

    @Autowired
    public GetEmployeeWebClient(@Qualifier("getEmployeeWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Employee> doGetById(String employeeId) {

        return webClient.get()
                .uri("/{employeeId}", employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    Mono<String> errorMsg = clientResponse.bodyToMono(String.class);
                    errorMsg.flatMap(msg -> {
                        log.error("Error message: {}", msg);
                        throw new RuntimeException(msg);
                    });
                    return Mono.error(new RestClientException("4xx"));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    Mono<String> errorMsg = clientResponse.bodyToMono(String.class);
                    errorMsg.flatMap(msg -> {
                        log.error("Error message: {}", msg);
                        throw new RuntimeException(msg);
                    });
                    return Mono.error(new RuntimeException("5xx"));
                }).bodyToMono(Employee.class);

    }

    public Flux<Employee> doGetAll() {
        return webClient.method(HttpMethod.GET)
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
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
                .bodyToFlux(Employee.class);
    }
}
