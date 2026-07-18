package com.configure.restclient.controller;

import com.configure.restclient.client.employee.EmployeeServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequiredArgsConstructor
public class ConfigController {

    private final EmployeeServiceClient employeeServiceClient;

    @GetMapping("/getUrlFromConfigServer")
    public String getUrl() {
        return "Url is: " + employeeServiceClient.getBaseUrl();
    }

}
