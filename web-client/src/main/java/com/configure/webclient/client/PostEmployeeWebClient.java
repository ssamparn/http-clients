package com.configure.webclient.client;

import com.configure.webclient.model.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PostEmployeeWebClient extends AbstractWebClient<Employee, Employee> {

    private static final String SERVICE = "EMPLOYEE-SERVICE";
    private static final String OPERATION = "CREATE-EMPLOYEE";
    private static final String AUTH_HEADER = "x-auth-user";
    private static final String AUTH_HEADER_VALUE = "auth-header-value";
    private static final String CUSTOM_HEADER = "custom-header";
    private static final String CUSTOM_HEADER_VALUE = "custom-header-value";

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
        return doPost("/create", employeeMono, Employee.class, Employee.class, httpHeaders -> {
            httpHeaders.set(AUTH_HEADER, AUTH_HEADER_VALUE);
            httpHeaders.set(CUSTOM_HEADER, CUSTOM_HEADER_VALUE);
        });
    }

    @Override
    protected WebClient getWebClient() {
        return this.postEmployeeWebClient;
    }

    @Override
    protected CircuitBreaker getCircuitBreaker() {
        return this.postEmployeesCircuitBreaker;
    }

    @Override
    protected Retry getRetry() {
        return this.postEmployeesRetry;
    }

    @Override
    public String getService() {
        return SERVICE;
    }

    @Override
    public String getOperation() {
        return OPERATION;
    }
}
