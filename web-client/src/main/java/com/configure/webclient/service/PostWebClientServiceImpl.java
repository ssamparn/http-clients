package com.configure.webclient.service;

import com.configure.webclient.client.PostWebClient;
import com.configure.webclient.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostWebClientServiceImpl implements PostWebClientService {

    private final PostWebClient postWebClient;

    @Override
    public Mono<ResponseEntity<Employee>> createNewEmployee(Employee employee) {
        return postWebClient.createNewEmployee(employee);
    }
}
