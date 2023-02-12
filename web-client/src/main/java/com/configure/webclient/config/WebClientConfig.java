package com.configure.webclient.config;

import com.configure.webclient.properties.ClientConnectionProperties;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Configuration
public class WebClientConfig {

    public static final int MAX_BUFFER_SIZE_BYTES = 2 * 1024 * 1024; // 2 mb
    private static final int ACQUIRE_TIMEOUT_SECONDS = 5;
    private static final int MAX_CONNECTIONS = 2000;
    private static final int TCP_KEEPIDLE_SECONDS = 3000;
    private static final int TCP_KEEPINTVL_SECONDS = 60;
    private static final int TCP_KEEPCNT = 8;

    @Bean
    public WebClient getEmployeeWebClient(@Qualifier("getEmployeeHttpClient") HttpClient httpClient,
                               @Qualifier("requestFilter") ExchangeFilterFunction requestFilter,
                               @Qualifier("responseFilter") ExchangeFilterFunction responseFilter,
                               @Qualifier("getEmployeeConnectionProperties") ClientConnectionProperties connectionProperties) {
        return WebClient.builder()
                .baseUrl(connectionProperties.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(requestFilter)
                .filter(responseFilter)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(MAX_BUFFER_SIZE_BYTES))
                        .build())
                .build();
    }

    @Bean
    public WebClient postEmployeeWebClient(@Qualifier("postEmployeeHttpClient")HttpClient httpClient,
                               @Qualifier("requestFilter") ExchangeFilterFunction requestFilter,
                               @Qualifier("responseFilter") ExchangeFilterFunction responseFilter,
                               @Qualifier("postEmployeeConnectionProperties") ClientConnectionProperties connectionProperties) {
        return WebClient.builder()
                .baseUrl(connectionProperties.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(requestFilter)
                .filter(responseFilter)
                .exchangeStrategies(ExchangeStrategies
                        .builder()
                        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(MAX_BUFFER_SIZE_BYTES))
                        .build())
                .build();
    }

    @Bean
    public HttpClient getEmployeeHttpClient(SslContext sslContext,
                                 @Qualifier("getEmployeeConnectionProperties") ClientConnectionProperties connectionProperties) {

        ConnectionProvider getHttpClientConnectionProvider = ConnectionProvider
                .builder("get-webclient-connection-pool")
                .maxConnections(MAX_CONNECTIONS)
                .pendingAcquireTimeout(Duration.ofSeconds(ACQUIRE_TIMEOUT_SECONDS))
                .pendingAcquireMaxCount(-1)
                .build();

        return HttpClient
            .create(getHttpClientConnectionProvider)
            .secure(sslSpec -> sslSpec.sslContext(sslContext))
            .wiretap(true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionProperties.getConnectionTimeout())
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, TCP_KEEPIDLE_SECONDS)
            .option(EpollChannelOption.TCP_KEEPINTVL, TCP_KEEPINTVL_SECONDS)
            .option(EpollChannelOption.TCP_KEEPCNT, TCP_KEEPCNT)
            .doOnConnected(connection ->
                    connection.addHandlerLast(new ReadTimeoutHandler(connectionProperties.getReadTimeout(), MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(connectionProperties.getWriteTimeout(), MILLISECONDS)));
    }

    @Bean
    public HttpClient postEmployeeHttpClient(SslContext sslContext,
                                 @Qualifier("postEmployeeConnectionProperties") ClientConnectionProperties connectionProperties) {

        ConnectionProvider postHttpClientConnectionProvider = ConnectionProvider
                .builder("post-webclient-connection-pool")
                .maxConnections(MAX_CONNECTIONS)
                .pendingAcquireTimeout(Duration.ofSeconds(ACQUIRE_TIMEOUT_SECONDS))
                .pendingAcquireMaxCount(-1)
                .build();

        return HttpClient
                .create(postHttpClientConnectionProvider)
                .secure(sslSpec -> sslSpec.sslContext(sslContext))
                .wiretap(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionProperties.getConnectionTimeout())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(EpollChannelOption.TCP_KEEPIDLE, TCP_KEEPIDLE_SECONDS)
                .option(EpollChannelOption.TCP_KEEPINTVL, TCP_KEEPINTVL_SECONDS)
                .option(EpollChannelOption.TCP_KEEPCNT, TCP_KEEPCNT)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(connectionProperties.getReadTimeout(), MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(connectionProperties.getWriteTimeout(), MILLISECONDS)));
    }
}
