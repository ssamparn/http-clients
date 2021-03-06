package com.configure.webclient.service;

import com.configure.webclient.model.Employee;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface PostWebClientService {
    Mono<ResponseEntity<Employee>> createNewEmployee(Employee employee);
}
