package com.configure.webclient.service;

import com.configure.webclient.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetEmployeeService {
    Mono<Employee> getEmployeeById(String employeeId);
    Flux<Employee> getAllEmployees();
}
