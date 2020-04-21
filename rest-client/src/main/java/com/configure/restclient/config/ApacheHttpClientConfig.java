package com.configure.restclient.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableScheduling
public class ApacheHttpClientConfig {

    @Bean(name = "employeeClientConnectionManager")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(ConnectionProperties connectionProperties) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(connectionProperties.getMaxHttpPoolsize());
        connectionManager.setDefaultMaxPerRoute(connectionProperties.getMaxHttpRouteConnection());

        return connectionManager;
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy(ConnectionProperties connectionProperties) {
        return ((httpResponse, httpContext) -> {
            HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
            HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);

            while (elementIterator.hasNext()) {
                HeaderElement element = elementIterator.nextElement();
                String param = element.getName();
                String value = element.getValue();

                if (value != null && param.equalsIgnoreCase("timeout")) {
                    return Long.parseLong(value) * 1000;
                }
            }
            return connectionProperties.getDefaultKeepAliveTime();
        });
    }

    @Bean
    public Runnable idleConnectionMonitor(@Qualifier("employeeClientConnectionManager") PoolingHttpClientConnectionManager httpClientPool, ConnectionProperties connectionProperties) {
        return new Runnable() {
            @Override
            @Scheduled(fixedDelay = 20000)
            public void run() {
                if (httpClientPool != null) {
                    httpClientPool.closeExpiredConnections();
                    httpClientPool.closeIdleConnections(connectionProperties.getIdleConnectionWaitTime(), TimeUnit.MILLISECONDS);
                }
            }
        };
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("idleMonitor");
        scheduler.setPoolSize(5);

        return scheduler;
    }

    @Bean
    public CloseableHttpClient httpClient(ConnectionProperties connectionProperties) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectionProperties.getConnectionTimeout())
                .setConnectionRequestTimeout(connectionProperties.getRequestTimeout())
                .setSocketTimeout(connectionProperties.getSocketTimeout())
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingHttpClientConnectionManager(connectionProperties))
                .setKeepAliveStrategy(connectionKeepAliveStrategy(connectionProperties))
                .disableConnectionState()
                .build();
    }
}
