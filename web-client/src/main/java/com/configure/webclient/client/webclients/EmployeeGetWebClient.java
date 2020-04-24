package com.configure.webclient.client.webclients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EmployeeGetWebClient {

    private final WebClient webClient;

    @Autowired
    public EmployeeGetWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
