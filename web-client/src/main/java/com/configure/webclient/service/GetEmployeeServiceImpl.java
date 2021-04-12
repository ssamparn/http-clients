package com.configure.webclient.service;

import com.configure.webclient.client.GetEmployeeWebClient;
import com.configure.webclient.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetEmployeeServiceImpl implements GetEmployeeService {

    private final GetEmployeeWebClient getEmployeeWebClient;

    @Override
    public Mono<ResponseEntity<Employee>> getEmployeeById(String employeeId) {
        return getEmployeeWebClient.doGetById(employeeId);
    }

    @Override
    public Mono<ResponseEntity<List<Employee>>> getAllEmployees() {
        return getEmployeeWebClient.doGetAll();
    }
}
