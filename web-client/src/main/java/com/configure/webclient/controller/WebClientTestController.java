package com.configure.webclient.controller;

import com.configure.webclient.model.Employee;
import com.configure.webclient.service.GetWebClientService;
import com.configure.webclient.service.PostWebClientService;
import lombok.RequiredArgsConstructor;
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
public class WebClientTestController {

    private final GetWebClientService getService;
    private final PostWebClientService postService;

    @GetMapping(path = "/get-employee/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Employee>> getEmployeeById(@PathVariable(name = "employeeId") String employeeId) {

        Mono<ResponseEntity<Employee>> employeeById = getService.getEmployeeById(employeeId);

        return employeeById;
    }

    @GetMapping(path = "/get-all-employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Employee>>> getEmployeeById() {

        Mono<ResponseEntity<List<Employee>>> allEmployees = getService.getAllEmployees();

        return allEmployees;
    }

    @PostMapping(path = "/create-employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Employee>> createEmployee(@RequestBody Employee employee) {

        Mono<ResponseEntity<Employee>> newEmployee = postService.createNewEmployee(employee);

        return newEmployee;
    }
}
