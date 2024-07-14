package com.configure.webclient.client;

import com.configure.webclient.model.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
@Component
public class PostEmployeeWebClient extends ReactiveWebClient<Employee, Employee> {

    private final WebClient postEmployeeWebClient;
    private final CircuitBreaker postEmployeesCircuitBreaker;
    private final Retry postEmployeesRetry;

    public PostEmployeeWebClient(@Qualifier("postWebClient") WebClient postEmployeeWebClient,
                                 @Qualifier("postEmployeesCircuitBreaker") CircuitBreaker postEmployeesCircuitBreaker,
                                 @Qualifier("postEmployeesRetry") Retry postEmployeesRetry) {
        this.postEmployeeWebClient = postEmployeeWebClient;
        this.postEmployeesCircuitBreaker = postEmployeesCircuitBreaker;
        this.postEmployeesRetry = postEmployeesRetry;
    }

    public Mono<Employee> newEmployee(Mono<Employee> employeeMono) {
        return postEmployeeWebClient.method(HttpMethod.POST)
                .uri("/create")
                .accept(MediaType.APPLICATION_JSON)
                .headers(createHttpHeaders())
                .body(employeeMono, Employee.class)
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
                .bodyToMono(Employee.class)
                .transformDeferred(CircuitBreakerOperator.of(postEmployeesCircuitBreaker))
                .transformDeferred(RetryOperator.of(postEmployeesRetry));
    }

    private Consumer<HttpHeaders> createHttpHeaders() {
        return httpHeaders -> {
            httpHeaders.set("x-auth-user", "auth-header-value");
            httpHeaders.set("custom-header", "custom-header-value");
        };
    }

    @Override
    protected WebClient getWebClient() {
        return null;
    }

    @Override
    protected String getBaseUrl() {
        return null;
    }

    @Override
    public String getService() {
        return null;
    }

    @Override
    public String getOperation() {
        return null;
    }
}
