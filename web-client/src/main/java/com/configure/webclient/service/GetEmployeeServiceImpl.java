package com.configure.webclient.service;

import com.configure.webclient.model.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GetEmployeeServiceImpl implements GetEmployeeService {

    private final WebClient getEmployeeWebClient;
    private final CircuitBreaker getEmployeesCircuitBreaker;

    @Autowired
    public GetEmployeeServiceImpl(@Qualifier("getEmployeeWebClient") WebClient webClient,
                                  CircuitBreaker getEmployeesCircuitBreaker) {
        this.getEmployeeWebClient = webClient;
        this.getEmployeesCircuitBreaker = getEmployeesCircuitBreaker;
    }

    @Override
    public Mono<Employee> getEmployeeById(String employeeId) {
        return getEmployeeWebClient.get()
                .uri("/{employeeId}", employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    Mono<String> errorMsg = clientResponse.bodyToMono(String.class);
                    errorMsg.flatMap(msg -> {
                        log.error("Error message: {}", msg);
                        throw new RuntimeException(msg);
                    });
                    return Mono.error(new RestClientException("4xx"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    Mono<String> errorMsg = clientResponse.bodyToMono(String.class);
                    errorMsg.flatMap(msg -> {
                        log.error("Error message: {}", msg);
                        throw new RuntimeException(msg);
                    });
                    return Mono.error(new RuntimeException("5xx"));
                })
                .bodyToMono(Employee.class)
                .transformDeferred(CircuitBreakerOperator.of(getEmployeesCircuitBreaker));
    }

    @Override
    public Flux<Employee> getAllEmployees() {
        return getEmployeeWebClient.method(HttpMethod.GET)
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON)
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
                .bodyToFlux(Employee.class)
                .transformDeferred(CircuitBreakerOperator.of(getEmployeesCircuitBreaker));
    }
}
