package com.configure.restclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ConfigController {

    @Value("${employee.service.http.url:none}")
    private String url;

    @GetMapping("/getUrlFromConfigServer")
    public String getUrl() {
        return "Url is: " + url;
    }

}
