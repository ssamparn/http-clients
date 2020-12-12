package com.configure.restclient.client.config;

import com.configure.restclient.config.ConnectionProperties;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
public class EmployeeServiceConnectionProperties implements ConnectionProperties {

    @Value("${employeeService.http.url}")
    private String url;

    @Value("${employeeService.connection.timeout.milliseconds}")
    private int connectionTimeout;

    @Value("${employeeService.socket.timeout.milliseconds}")
    private int socketTimeout;

    @Value("${employeeService.request.timeout.milliseconds}")
    private int requestTimeout;

    @Value("${employeeService.http.maxpoolsize}")
    private int maxHttpPoolsize;

    @Value("${employeeService.http.maxrouteconnection}")
    private int maxHttpRouteConnection;

    @Value("${employeeService.http.defaultkeepalivetime.milliseconds}")
    private int defaultKeepAliveTime;

    @Value("${employeeService.http.idleConnectionWaitTime.milliseconds}")
    private int idleConnectionWaitTime;

}
