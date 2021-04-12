package com.configure.webclient.service;

import com.configure.webclient.model.Employee;
import reactor.core.publisher.Mono;

public interface PostEmployeeService {
    Mono<Employee> createNewEmployee(Employee employee);
}
