package com.configure.webclient.service;

import com.configure.webclient.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
@Service
public class PostEmployeeServiceImpl implements PostEmployeeService {

    private final WebClient postEmployeeWebClient;

    @Autowired
    public PostEmployeeServiceImpl(@Qualifier("postEmployeeWebClient") WebClient webClient) {
        this.postEmployeeWebClient = webClient;
    }

    @Override
    public Mono<Employee> createNewEmployee(Employee newEmployee) {
        return postEmployeeWebClient.method(HttpMethod.POST)
                .uri("/create")
                .accept(MediaType.APPLICATION_JSON)
                .headers(createHttpHeaders())
                .body(Mono.just(newEmployee), Employee.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    Mono<String> errorMsg = clientResponse.bodyToMono(String.class);
                    errorMsg.flatMap(msg -> {
                        log.error("Error message: {}", msg);
                        throw new RuntimeException(msg);
                    });
                    return Mono.error(new RuntimeException("4xx"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    Mono<String> errorMsg = clientResponse.bodyToMono(String.class);
                    errorMsg.flatMap(msg -> {
                        log.error("Error message: {}", msg);
                        throw new RuntimeException(msg);
                    });
                    return Mono.error(new RuntimeException("5xx"));
                })
                .bodyToMono(Employee.class);
    }

    private Consumer<HttpHeaders> createHttpHeaders() {
        return httpHeaders -> {
            httpHeaders.set("x-auth-user", "auth-header-value");
            httpHeaders.set("custom-header", "custom-header-value");
        };
    }
}
