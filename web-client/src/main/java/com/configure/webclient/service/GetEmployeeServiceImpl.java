package com.configure.webclient.service;

import com.configure.webclient.client.GetEmployeeWebClient;
import com.configure.webclient.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GetEmployeeServiceImpl implements GetEmployeeService {

    private final GetEmployeeWebClient getEmployeeWebClient;

    @Autowired
    public GetEmployeeServiceImpl(GetEmployeeWebClient getEmployeeWebClient) {
        this.getEmployeeWebClient = getEmployeeWebClient;
    }

    @Override
    public Mono<Employee> getEmployeeById(String employeeId) {
        return getEmployeeWebClient.getEmployee(employeeId);
    }

    @Override
    public Flux<Employee> getAllEmployees() {
        return getEmployeeWebClient.getEmployees();}
}
