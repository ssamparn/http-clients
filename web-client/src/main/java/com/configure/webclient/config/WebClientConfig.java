package com.configure.webclient.config;

import com.configure.webclient.properties.ClientConnectionProperties;
import io.netty.channel.ChannelOption;
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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(HttpClient httpClient,
                               @Qualifier("requestFilter") ExchangeFilterFunction requestFilter,
                               @Qualifier("responseFilter") ExchangeFilterFunction responseFilter,
                               ClientConnectionProperties connectionProperties) {
        WebClient client = WebClient.builder()
                .baseUrl(connectionProperties.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(requestFilter)
                .filter(responseFilter)
                .build();
        return client;
    }

    @Bean
    public HttpClient httpClient(SslContext sslContext,
                                 ClientConnectionProperties connectionProperties) {
        HttpClient httpClient = HttpClient.create()
                .wiretap(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionProperties.getConnectionTimeout())
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(connectionProperties.getReadTimeout(), MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(connectionProperties.getWriteTimeout(), MILLISECONDS)))
                .responseTimeout(Duration.ofSeconds(2))
                .secure(sslSpec ->
                        sslSpec.sslContext(sslContext)
                );

        return httpClient;
    }

}
