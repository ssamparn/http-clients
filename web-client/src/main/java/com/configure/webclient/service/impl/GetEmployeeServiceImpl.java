package com.configure.webclient.service.impl;

import com.configure.webclient.client.GetEmployeeWebClient;
import com.configure.webclient.model.Employee;
import com.configure.webclient.service.GetEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetEmployeeServiceImpl implements GetEmployeeService {

    private final GetEmployeeWebClient getEmployeeWebClient;

    @Override
    public Mono<Employee> getEmployeeById(String employeeId) {
        return getEmployeeWebClient.getEmployee(employeeId);
    }

    @Override
    public Flux<Employee> getAllEmployees() {
        return getEmployeeWebClient.getEmployees();
    }
}
