package com.configure.webclient.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties("resilience4j.retry.config")
public class RetryConfigProperties {
    private int maximumAttempts;
    private Duration waitDurationBetweenRetryAttempts;
}
