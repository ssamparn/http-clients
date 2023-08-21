package com.configure.webclient.config;

import com.configure.webclient.properties.CircuitBreakerConfigProperties;
import com.configure.webclient.properties.RetryConfigProperties;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class Resillience4jConfig {

    private static final String GET_EMPLOYEES_CIRCUIT_BREAKER = "getAccountsCircuitBreaker";
    private static final String POST_EMPLOYEES_CIRCUIT_BREAKER = "postEmployeesCircuitBreaker";
    private static final String GET_EMPLOYEES_RETRY = "getAccountsRetry";
    private static final String POST_EMPLOYEES_RETRY = "postEmployeesRetry";
    private static final String IS_UNAVAILABLE = " is unavailable";

    private final CircuitBreakerConfigProperties circuitBreakerConfigProperties;
    private final RetryConfigProperties retryConfigProperties;

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowSize(circuitBreakerConfigProperties.getSlidingWindowSize())
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .failureRateThreshold(circuitBreakerConfigProperties.getFailureRateThreshold())
                .minimumNumberOfCalls(circuitBreakerConfigProperties.getMinimumNumberOfCalls())
                .waitDurationInOpenState(Duration.ofSeconds(circuitBreakerConfigProperties.getWaitDurationInOpenStateInSeconds()))
                .permittedNumberOfCallsInHalfOpenState(circuitBreakerConfigProperties.getPermittedNumberOfCallsInHalfOpenState())
                .build();
    }

    @Bean
    public RetryConfig retryConfig() {
        return RetryConfig.custom()
                .maxAttempts(retryConfigProperties.getMaximumAttempts())
                .waitDuration(retryConfigProperties.getWaitDurationBetweenRetryAttempts())
                .retryExceptions(RuntimeException.class)
                .build();
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig) {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        circuitBreakerRegistry.circuitBreaker(GET_EMPLOYEES_CIRCUIT_BREAKER);
        circuitBreakerRegistry.circuitBreaker(POST_EMPLOYEES_CIRCUIT_BREAKER);

        return circuitBreakerRegistry;
    }

    @Bean
    public RetryRegistry retryRegistry(RetryConfig retryConfig) {
        RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);
        retryRegistry.retry(GET_EMPLOYEES_RETRY);
        retryRegistry.retry(POST_EMPLOYEES_RETRY);

        return retryRegistry;
    }

    @Bean(name = "getEmployeesCircuitBreaker")
    public CircuitBreaker getEmployeesCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return Optional.of(circuitBreakerRegistry.find(GET_EMPLOYEES_CIRCUIT_BREAKER))
            .get()
            .orElseThrow(
                    () -> new IllegalArgumentException(GET_EMPLOYEES_CIRCUIT_BREAKER + IS_UNAVAILABLE)
            );
    }

    @Bean(name = "postEmployeesCircuitBreaker")
    public CircuitBreaker postEmployeesCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return Optional.of(circuitBreakerRegistry.find(POST_EMPLOYEES_CIRCUIT_BREAKER))
                .get()
                .orElseThrow(
                        () -> new IllegalArgumentException(POST_EMPLOYEES_CIRCUIT_BREAKER + IS_UNAVAILABLE)
                );
    }

    @Bean(name = "getEmployeesRetry")
    public Retry getEmployeesRetry(RetryRegistry retryRegistry) {
        return Optional.of(retryRegistry.find(GET_EMPLOYEES_RETRY))
                .get()
                .orElseThrow(
                        () -> new IllegalArgumentException(GET_EMPLOYEES_RETRY + IS_UNAVAILABLE)
                );
    }

    @Bean(name = "postEmployeesRetry")
    public Retry postEmployeesRetry(RetryRegistry retryRegistry) {
        return Optional.of(retryRegistry.find(POST_EMPLOYEES_RETRY))
                .get()
                .orElseThrow(
                        () -> new IllegalArgumentException(POST_EMPLOYEES_RETRY + IS_UNAVAILABLE)
                );
    }
}
