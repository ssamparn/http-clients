package com.configure.webclient.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("resilience4j.circuitbreaker.config")
public class CircuitBreakerConfigProperties {

    private int minimumNumberOfCalls;

    private float failureRateThreshold;

    private int permittedNumberOfCallsInHalfOpenState;

    private long waitDurationInOpenStateInSeconds;

    private int slidingWindowSize;
}
