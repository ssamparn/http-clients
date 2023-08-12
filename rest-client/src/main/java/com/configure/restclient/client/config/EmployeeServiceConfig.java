package com.configure.restclient.client.config;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class EmployeeServiceConfig {

    @Bean(name = "employeeServiceConnectionManager")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(EmployeeServiceConnectionProperties connectionProperties) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(connectionProperties.getMaxHttpPoolsize());
        connectionManager.setDefaultMaxPerRoute(connectionProperties.getMaxHttpRouteConnection());

        return connectionManager;
    }

    @Bean(name = "employeeServiceHttpClient")
    public CloseableHttpClient httpClient(@Qualifier("employeeServiceConnectionManager") PoolingHttpClientConnectionManager connectionManager, EmployeeServiceConnectionProperties employeeServiceConnectionProperties) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(employeeServiceConnectionProperties.getConnectionTimeout())
                .setConnectionRequestTimeout(employeeServiceConnectionProperties.getRequestTimeout())
                .setSocketTimeout(employeeServiceConnectionProperties.getSocketTimeout())
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(connectionKeepAliveStrategy(employeeServiceConnectionProperties))
                .disableConnectionState()
                .build();
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy(EmployeeServiceConnectionProperties employeeServiceConnectionProperties) {
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
            return employeeServiceConnectionProperties.getDefaultKeepAliveTime();
        });
    }

    @Bean
    public Runnable idleConnectionMonitor(@Qualifier("employeeServiceConnectionManager") PoolingHttpClientConnectionManager httpClientPool, EmployeeServiceConnectionProperties connectionProperties) {
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

}
