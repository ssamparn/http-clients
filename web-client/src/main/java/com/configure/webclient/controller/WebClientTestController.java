package com.configure.webclient.controller;

import com.configure.webclient.model.Employee;
import com.configure.webclient.service.GetEmployeeService;
import com.configure.webclient.service.PostEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webclient")
public class WebClientTestController {

    private final GetEmployeeService getEmployeeService;
    private final PostEmployeeService postEmployeeService;

    @GetMapping(path = "/get-employee/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Employee> getEmployeeById(@PathVariable(name = "employeeId") String employeeId) {
        return getEmployeeService.getEmployeeById(employeeId);
    }

    @GetMapping(path = "/get-all-employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Employee> getEmployeeById() {
        return getEmployeeService.getAllEmployees();
    }

    @PostMapping(path = "/create-employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Employee> createEmployee(@RequestBody Mono<Employee> employeeMono) {
        return postEmployeeService.createNewEmployee(employeeMono);
    }
}
