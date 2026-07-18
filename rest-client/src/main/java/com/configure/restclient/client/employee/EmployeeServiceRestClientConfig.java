package com.configure.restclient.client.employee;

import com.configure.restclient.client.config.AbstractRestClientConfig;
import com.configure.restclient.config.CustomClientHttpRequestInterceptor;
import com.configure.restclient.properties.HttpClientConnectionProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import java.util.List;

@Configuration
public class EmployeeServiceRestClientConfig extends AbstractRestClientConfig {

    @Bean(name = "employeeServiceRestClient")
    public RestClient createEmployeeServiceRestClient(@Qualifier("employeeServiceHttpClientConnectionProperties") HttpClientConnectionProperties connectionProperties,
                                          SSLContext sslContext) {
        return super.createRestClient(connectionProperties, sslContext, List.of(new CustomClientHttpRequestInterceptor()));
    }

    @Override
    @Bean(name = "employeeServiceIdleConnectionMonitor")
    public Runnable idleConnectionMonitor() {
        return super.idleConnectionMonitor();
    }
}
