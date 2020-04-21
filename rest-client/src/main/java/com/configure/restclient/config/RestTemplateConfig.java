package com.configure.restclient.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    private final CloseableHttpClient httpClient;

    @Autowired
    public RestTemplateConfig(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }

    @Bean
    public RestTemplate employeeServiceRestTemplate(@Qualifier("httpMessageConverter") List<HttpMessageConverter<?>> messageConverters) {
        return new RestTemplateBuilder()
                .messageConverters(messageConverters)
                .requestFactory(this::clientHttpRequestFactory)
                .errorHandler(new CustomClientErrorHandler())
                .interceptors(new CustomClientHttpRequestInterceptor())
                .build();
    }

}
