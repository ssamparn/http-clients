package com.configure.webclient.client;

import com.configure.webclient.model.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GetEmployeeWebClient extends ReactiveWebClient<String, Employee> {

    private final WebClient getEmployeeWebClient;
    private final CircuitBreaker getEmployeesCircuitBreaker;
    private final Retry getEmployeesRetry;

    @Autowired
    public GetEmployeeWebClient(@Qualifier("getWebClient") WebClient getEmployeeWebClient,
                                @Qualifier("getEmployeesCircuitBreaker") CircuitBreaker getEmployeesCircuitBreaker,
                                @Qualifier("getEmployeesRetry") Retry getEmployeesRetry) {
        this.getEmployeeWebClient = getEmployeeWebClient;
        this.getEmployeesCircuitBreaker = getEmployeesCircuitBreaker;
        this.getEmployeesRetry = getEmployeesRetry;
    }

    public Mono<Employee> getEmployee(String employeeId) {
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
                .transformDeferred(CircuitBreakerOperator.of(getEmployeesCircuitBreaker))
                .transformDeferred(RetryOperator.of(getEmployeesRetry));
    }

    public Flux<Employee> getEmployees() {
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
                .transformDeferred(CircuitBreakerOperator.of(getEmployeesCircuitBreaker))
                .transformDeferred(RetryOperator.of(getEmployeesRetry));
    }

    @Override
    protected WebClient getWebClient() {
        return this.getEmployeeWebClient;
    }

    @Override
    protected String getBaseUrl() {
        return null;
    }

    @Override
    public String getService() {
        return "GET-EMPLOYEE";
    }

    @Override
    public String getOperation() {
        return "GET-EMPLOYEE";
    }
}
