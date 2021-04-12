package com.configure.webclient.service;

import com.configure.webclient.client.GetEmployeeWebClient;
import com.configure.webclient.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetEmployeeServiceImpl implements GetEmployeeService {

    private final GetEmployeeWebClient getEmployeeWebClient;

    @Override
    public Mono<Employee> getEmployeeById(String employeeId) {
        return getEmployeeWebClient.doGetById(employeeId);
    }

    @Override
    public Flux<Employee> getAllEmployees() {
        return getEmployeeWebClient.doGetAll();
    }
}
