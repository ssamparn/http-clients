package com.configure.webclient.service;

import com.configure.webclient.model.Employee;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GetWebClientService {
    Mono<ResponseEntity<Employee>> getEmployeeById(String employeeId);
    Mono<ResponseEntity<List<Employee>>> getAllEmployees();
}
