package com.configure.webclient.controller;

import com.configure.webclient.client.webclients.EmployeeGetWebClient;
import com.configure.webclient.client.webclients.EmployeePostWebClient;
import com.configure.webclient.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webclient")
public class EmployeeWebClientTestController {

    private final EmployeeGetWebClient getWebClient;
    private final EmployeePostWebClient postWebClient;

    @GetMapping(path = "/get-employee/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Employee>> getEmployeeById(@PathVariable(name = "employeeId") String employeeId) {

        Mono<ResponseEntity<Employee>> employeeById = getWebClient.getEmployeeById(employeeId);

        return employeeById;
    }

    @GetMapping(path = "/get-all-employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Employee>>> getEmployeeById() {

        Mono<ResponseEntity<List<Employee>>> allEmployees = getWebClient.getAllEmployees();

        return allEmployees;
    }

    @PostMapping(path = "/create-employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Employee>> createEmployee(@RequestBody Employee employee) {

        Mono<ResponseEntity<Employee>> newEmployee = postWebClient.createNewEmployee(employee);

        return newEmployee;
    }


}
