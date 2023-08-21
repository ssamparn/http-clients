package com.configure.restclient.client.config;

import com.configure.restclient.properties.HttpClientConnectionProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.Iterator;

@Slf4j
@EnableScheduling
public abstract class AbstractRestClientConfig {

    private PoolingHttpClientConnectionManager connectionManager;
    private HttpClientConnectionProperties connectionProperties;

    public CloseableHttpClient closeableHttpClient(Registry<ConnectionSocketFactory> registry, HttpClientConnectionProperties connectionProperties) {
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager(registry);
        clientConnectionManager.setMaxTotal(connectionProperties.getMaxPoolSize());
        clientConnectionManager.setDefaultMaxPerRoute(connectionProperties.getMaxTotalConnections());
        clientConnectionManager.setDefaultConnectionConfig(ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectionProperties.getConnectionTimeoutMs()))
                .setSocketTimeout(Timeout.ofMilliseconds(connectionProperties.getSocketTimeoutMs()))
                .build());

        this.connectionManager = clientConnectionManager;
        this.connectionProperties = connectionProperties;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectionProperties.getConnectionTimeoutMs()))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectionProperties.getConnectionRequestTimeoutMs()))
                .setResponseTimeout(Timeout.ofMilliseconds(connectionProperties.getSocketTimeoutMs()))
                .setExpectContinueEnabled(true)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(clientConnectionManager)
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .disableConnectionState()
                .build();
    }

    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return ((httpResponse, httpContext) -> TimeValue.ofMilliseconds(configureConnectionKeepAliveDuration(httpResponse)));
    }

    private long configureConnectionKeepAliveDuration(HttpResponse httpResponse) {
        Iterator<Header> headerIterator = httpResponse.headerIterator("Keep-Alive");
        BasicHeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);
        while (elementIterator.hasNext()) {
            HeaderElement element = elementIterator.next();
            if (element.getValue() != null && element.getName().equalsIgnoreCase("timeout")) {
                return Duration.ofSeconds(Long.parseLong(element.getValue())).toMillis();
            }
        }
        return this.connectionProperties.getDefaultKeepAliveTime().toMillis();
    }

    public Runnable idleConnectionMonitor() {
        return new Runnable() {
            @Override
            @Scheduled(fixedDelay = 10000)
            public void run() {
                try {
                    if (connectionManager != null) {
                        log.trace("run IdleConnectionMonitor - Closing expired and idle connections...");
                        connectionManager.closeExpired();
                        connectionManager.closeIdle(TimeValue.ofMilliseconds(connectionProperties.getIdleConnectionWaitTime().toMillis()));
                    } else {
                        log.trace("run IdleConnectionMonitor - Http Client Connection manager is not initialised");
                    }
                } catch (Exception e) {
                    log.error("run IdleConnectionMonitor - Exception occurred. msg={}, e={}", e.getMessage(), e);
                }
            }
        };
    }
}
