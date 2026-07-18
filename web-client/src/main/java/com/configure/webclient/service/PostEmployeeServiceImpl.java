package com.configure.webclient.service;

import com.configure.webclient.client.PostEmployeeWebClient;
import com.configure.webclient.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostEmployeeServiceImpl implements PostEmployeeService {

    private final PostEmployeeWebClient postEmployeeWebClient;

    @Override
    public Mono<Employee> createNewEmployee(Mono<Employee> employeeMono) {
        return postEmployeeWebClient.newEmployee(employeeMono);
    }
}
