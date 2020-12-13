package com.configure.webclient.client;

import com.configure.webclient.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component("getWebClient")
public class GetWebClient {

    private final WebClient webClient;

    @Autowired
    public GetWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseEntity<Employee>> getEmployeeById(String employeeId) {
        return webClient.method(HttpMethod.GET)
                .uri("/{employeeId}", employeeId)
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
                .toEntity(Employee.class)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<List<Employee>>> getAllEmployees() {
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
                .toEntity(new ParameterizedTypeReference<List<Employee>>() {
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)
                );
    }
}
