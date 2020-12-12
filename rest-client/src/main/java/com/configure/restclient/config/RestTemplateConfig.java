package com.configure.restclient.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean("employeeServiceRestTemplate")
    public RestTemplate createEmployeeServiceRestTemplate(@Qualifier("employeeServiceHttpClient") CloseableHttpClient closeableHttpClient,
                                                          ObjectMappersConfig objectMappersConfig) {
        return createRestTemplate(closeableHttpClient, objectMappersConfig.createHttpMessageConverter());
    }

    private RestTemplate createRestTemplate(CloseableHttpClient closeableHttpClient, List<HttpMessageConverter<?>> httpMessageConverter) {
        RestTemplate restTemplate = new RestTemplate(getHttpComponentsClient(closeableHttpClient));
        restTemplate.setMessageConverters(httpMessageConverter);
        restTemplate.setInterceptors(Collections.singletonList(new CustomClientHttpRequestInterceptor()));
        restTemplate.setErrorHandler(new CustomClientErrorHandler());

        return restTemplate;
    }

    private HttpComponentsClientHttpRequestFactory getHttpComponentsClient(CloseableHttpClient closeableHttpClient) {
        return new HttpComponentsClientHttpRequestFactory(closeableHttpClient);
    }

}
