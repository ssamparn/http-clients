package com.configure.webclient.service;

import com.configure.webclient.client.PostEmployeeWebClient;
import com.configure.webclient.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostEmployeeServiceImpl implements PostEmployeeService {

    private final PostEmployeeWebClient postEmployeeWebClient;

    @Override
    public Mono<Employee> createNewEmployee(Employee employee) {
        return postEmployeeWebClient.createNewEmployee(employee);
    }
}
