package com.configure.restservice.web.controller;

import com.configure.restservice.entity.EmployeeEntity;
import com.configure.restservice.model.Employee;
import com.configure.restservice.service.EmployeeService;
import com.configure.restservice.service.EmployeeServiceResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/employees")
public class EmployeeRestController {

    private final EmployeeService employeeService;
    private final EmployeeServiceResponseFactory responseFactory;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService, EmployeeServiceResponseFactory responseFactory) {
        this.employeeService = employeeService;
        this.responseFactory = responseFactory;
    }

    @GetMapping(path = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        EmployeeEntity employeeEntity = employeeService.getEmployee(employeeId);

        Employee employee = responseFactory.createGetEmployeeResponse(employeeEntity);

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        List<EmployeeEntity> allEmployees = employeeService.getAllEmployees(pageNumber, pageSize);
        List<Employee> allEmployeesResponse = responseFactory.createGetAllEmployeesResponse(allEmployees);
        return new ResponseEntity<>(allEmployeesResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeEntity newEmployee) {
        EmployeeEntity savedEmployee = employeeService.createNewEemployee(newEmployee);
        Employee newEmployeeResponse = responseFactory.createNewEmployeeResponse(savedEmployee);
        return new ResponseEntity<>(newEmployeeResponse, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{employeeId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> updateEmplyee(@PathVariable(name = "employeeId") Long employeeId, @RequestBody EmployeeEntity employeeTobeUpdated) {
        EmployeeEntity updatedEmployee = employeeService.updateEmployee(employeeId, employeeTobeUpdated);
        Employee updateEmployeeResponse = responseFactory.createUpdateEmployeeResponse(updatedEmployee);
        return new ResponseEntity<>(updateEmployeeResponse, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

}
