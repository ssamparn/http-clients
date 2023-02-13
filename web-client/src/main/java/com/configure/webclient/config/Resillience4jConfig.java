package com.configure.webclient.config;

import com.configure.webclient.properties.CircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Optional;

@Configuration
public class Resillience4jConfig {

    private static final String GET_EMPLOYEES_CIRCUIT_BREAKER = "getAccountsCircuitBreaker";
    private static final String POST_EMPLOYEES_CIRCUIT_BREAKER = "postEmployeesCircuitBreaker";
    private static final String IS_UNAVAILABLE = " is unavailable";

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig(CircuitBreakerProperties circuitBreakerProperties) {
        return CircuitBreakerConfig.custom()
            .failureRateThreshold(circuitBreakerProperties.getFailureRateThreshold())
            .minimumNumberOfCalls(circuitBreakerProperties.getMinimumNumberOfCalls())
            .waitDurationInOpenState(Duration.ofSeconds(circuitBreakerProperties.getWaitDurationInOpenStateInSeconds()))
            .permittedNumberOfCallsInHalfOpenState(circuitBreakerProperties.getPermittedNumberOfCallsInHalfOpenState())
            .slidingWindowSize(circuitBreakerProperties.getSlidingWindowSize())
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
    public CircuitBreaker getEmployeesCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return Optional.of(circuitBreakerRegistry.find(GET_EMPLOYEES_CIRCUIT_BREAKER))
            .get()
            .orElseThrow(
                    () -> new IllegalArgumentException(GET_EMPLOYEES_CIRCUIT_BREAKER + IS_UNAVAILABLE)
            );
    }

    @Bean
    public CircuitBreaker postEmployeesCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return Optional.of(circuitBreakerRegistry.find(POST_EMPLOYEES_CIRCUIT_BREAKER))
                .get()
                .orElseThrow(
                        () -> new IllegalArgumentException(POST_EMPLOYEES_CIRCUIT_BREAKER + IS_UNAVAILABLE)
                );
    }
}
