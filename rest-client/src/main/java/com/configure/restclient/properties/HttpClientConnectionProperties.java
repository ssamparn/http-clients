package com.configure.restclient.properties;

import lombok.Data;

import java.time.Duration;

@Data
public class HttpClientConnectionProperties {
    private String url;
    private int maxPoolSize;
    private int maxTotalConnections;
    private int validateAfterInactivityMs;
    private int socketTimeoutMs;
    private int connectionTimeoutMs;
    private int connectionRequestTimeoutMs;
    private Duration defaultKeepAliveTime;
    private Duration idleConnectionWaitTime;
}