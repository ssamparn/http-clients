package com.configure.restclient.client.config;

import com.configure.restclient.config.CustomClientErrorHandler;
import com.configure.restclient.properties.HttpClientConnectionProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

@Slf4j
@EnableScheduling
public abstract class AbstractRestClientConfig {

    private PoolingHttpClientConnectionManager clientConnectionManager;
    private HttpClientConnectionProperties clientConnectionProperties;

    public RestClient createRestClient(HttpClientConnectionProperties clientConnectionProperties,
                                       SSLContext sslContext,
                                       List<ClientHttpRequestInterceptor> interceptors) {
        RestClient.Builder restClientBuilder = RestClient.builder()
                .baseUrl(clientConnectionProperties.getUrl())
                .defaultHeader("User-Agent", "EmployeeRestClient")
                .defaultHeader("Accept-Language", "en-US")
                .defaultHeader("Custom-Header", "Custom-Header")
                .defaultStatusHandler(new CustomClientErrorHandler())
                .requestInterceptors(clientHttpRequestInterceptors -> clientHttpRequestInterceptors.addAll(interceptors));

        CloseableHttpClient apacheHttpClient = this.closeableHttpClient(clientConnectionProperties, sslContext);
        restClientBuilder.requestFactory(new HttpComponentsClientHttpRequestFactory(apacheHttpClient));

        return restClientBuilder.build();
    }

    private CloseableHttpClient closeableHttpClient(HttpClientConnectionProperties clientConnectionProperties,
                                                       SSLContext sslContext) {
        PoolingHttpClientConnectionManagerBuilder clientConnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(clientConnectionProperties.getMaxPoolSize())
                .setMaxConnPerRoute(clientConnectionProperties.getMaxTotalConnections())
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(clientConnectionProperties.getConnectionTimeoutMs()))
                        .setSocketTimeout(Timeout.ofMilliseconds(clientConnectionProperties.getSocketTimeoutMs()))
                        .build())
                .setDefaultSocketConfig(SocketConfig.custom()
                        .setSoTimeout(Timeout.ofMilliseconds(clientConnectionProperties.getSocketTimeoutMs()))
                        .build())
                .setTlsSocketStrategy(ClientTlsStrategyBuilder.create()
                        .setSslContext(sslContext)
                        .buildClassic());

        this.clientConnectionManager = clientConnectionManagerBuilder.build();
        this.clientConnectionProperties = clientConnectionProperties;

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(clientConnectionProperties.getConnectionRequestTimeoutMs()))
                .setResponseTimeout(Timeout.ofMilliseconds(clientConnectionProperties.getSocketTimeoutMs()))
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
        return this.clientConnectionProperties.getDefaultKeepAliveTime().toMillis();
    }

    public Runnable idleConnectionMonitor() {
        return new Runnable() {
            @Override
            @Scheduled(fixedDelay = 10000)
            public void run() {
                try {
                    if (clientConnectionManager != null) {
                        log.trace("run IdleConnectionMonitor - Closing expired and idle connections...");
                        clientConnectionManager.closeExpired();
                        clientConnectionManager.closeIdle(TimeValue.ofMilliseconds(clientConnectionProperties.getIdleConnectionWaitTime().toMillis()));
                    } else {
                        log.trace("run IdleConnectionMonitor - Http Client Connection manager is not initialised");
                    }
                } catch (Exception e) {
                    log.error("run IdleConnectionMonitor - Exception occurred. msg={}, e={}", e.getMessage(), e.getStackTrace());
                }
            }
        };
    }
}
