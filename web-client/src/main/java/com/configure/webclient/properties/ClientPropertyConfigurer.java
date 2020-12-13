package com.configure.webclient.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientPropertyConfigurer {

    @Bean
    @ConfigurationProperties(prefix = "employee.get.http")
    public ClientConnectionProperties getEmployeeConnectionProperties() {
        return new ClientConnectionProperties();
    }

}
