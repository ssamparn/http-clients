package com.configure.webclient.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientPropertyConfigurer {

    @Bean("getEmployeeConnectionProperties")
    @ConfigurationProperties(prefix = "get.http")
    public ClientConnectionProperties getEmployeeConnectionProperties() {
        return new ClientConnectionProperties();
    }

    @Bean("postEmployeeConnectionProperties")
    @ConfigurationProperties(prefix = "post.http")
    public ClientConnectionProperties postEmployeeConnectionProperties() {
        return new ClientConnectionProperties();
    }

}
