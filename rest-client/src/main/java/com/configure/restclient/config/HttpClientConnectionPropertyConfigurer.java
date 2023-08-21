package com.configure.restclient.config;

import com.configure.restclient.properties.HttpClientConnectionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConnectionPropertyConfigurer {

    @Bean
    @ConfigurationProperties(prefix = "employee.service.http")
    public HttpClientConnectionProperties employeeServiceHttpClientConnectionProperties() {
        return new HttpClientConnectionProperties();
    }
}
