package com.configure.restclient.client.config;

import com.configure.restclient.properties.HttpClientConnectionProperties;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeServiceRestClientConfig extends AbstractRestClientConfig {

    @Override
    @Bean(name = "employeeServiceHttpClient")
    public CloseableHttpClient closeableHttpClient(Registry<ConnectionSocketFactory> registry,
                                                   @Qualifier("employeeServiceHttpClientConnectionProperties") HttpClientConnectionProperties connectionProperties) {
        return super.closeableHttpClient(registry, connectionProperties);
    }

    @Override
    @Bean(name = "employeeServiceIdleConnectionMonitor")
    public Runnable idleConnectionMonitor() {
        return super.idleConnectionMonitor();
    }
}
