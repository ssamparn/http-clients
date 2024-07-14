package com.configure.webclient.service;

import com.configure.webclient.client.PostEmployeeWebClient;
import com.configure.webclient.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
@Service
public class PostEmployeeServiceImpl implements PostEmployeeService {

    private final PostEmployeeWebClient postEmployeeWebClient;

    @Autowired
    public PostEmployeeServiceImpl(PostEmployeeWebClient postEmployeeWebClient) {
        this.postEmployeeWebClient = postEmployeeWebClient;
    }

    @Override
    public Mono<Employee> createNewEmployee(Mono<Employee> employeeMono) {
        return postEmployeeWebClient.newEmployee(employeeMono);
    }

    private Consumer<HttpHeaders> createHttpHeaders() {
        return httpHeaders -> {
            httpHeaders.set("x-auth-user", "auth-header-value");
            httpHeaders.set("custom-header", "custom-header-value");
        };
    }
}
