package com.configure.restclient.controller;

import com.configure.restclient.client.employee.EmployeeServiceClient;
import com.configure.restclient.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restclient")
public class EmployeeClientController {

    private final EmployeeServiceClient employeeServiceClient;

    @GetMapping(path = "/get-employee/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return employeeServiceClient.getEmployeeById(employeeId);
    }

    @GetMapping(path = "/get-all-employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAllEmployees(@RequestParam(defaultValue = "0") int pageNumber,
                                          @RequestParam(defaultValue = "20") int pageSize) {
        return employeeServiceClient.getAllEmployees(pageNumber, pageSize);
    }

    @PostMapping(path = "/create-employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee newEmployee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeServiceClient.createEmployee(newEmployee));
    }

    @PutMapping(path = "/update-employee/{employeeId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Employee updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employeeToBeUpdated) {
        return employeeServiceClient.updateEmployee(employeeId, employeeToBeUpdated);
    }

    @DeleteMapping(path = "/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long employeeId) {
        employeeServiceClient.deleteEmployee(employeeId);
    }
}

