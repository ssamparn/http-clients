package com.configure.webclient.client;

import com.configure.webclient.model.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GetEmployeeWebClient extends AbstractWebClient<String, Employee> {

    private static final String SERVICE = "EMPLOYEE-SERVICE";
    private static final String OPERATION = "GET-EMPLOYEE";

    private final WebClient getEmployeeWebClient;
    private final CircuitBreaker getEmployeesCircuitBreaker;
    private final Retry getEmployeesRetry;

    public GetEmployeeWebClient(@Qualifier("getWebClient") WebClient getEmployeeWebClient,
                                @Qualifier("getEmployeesCircuitBreaker") CircuitBreaker getEmployeesCircuitBreaker,
                                @Qualifier("getEmployeesRetry") Retry getEmployeesRetry) {
        this.getEmployeeWebClient = getEmployeeWebClient;
        this.getEmployeesCircuitBreaker = getEmployeesCircuitBreaker;
        this.getEmployeesRetry = getEmployeesRetry;
    }

    public Mono<Employee> getEmployee(String employeeId) {
        return doGet("/{employeeId}", Employee.class, employeeId);
    }

    public Flux<Employee> getEmployees() {
        return doGetMany("/all", Employee.class);
    }

    @Override
    protected WebClient getWebClient() {
        return this.getEmployeeWebClient;
    }

    @Override
    protected CircuitBreaker getCircuitBreaker() {
        return this.getEmployeesCircuitBreaker;
    }

    @Override
    protected Retry getRetry() {
        return this.getEmployeesRetry;
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
