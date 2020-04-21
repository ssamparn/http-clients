package com.configure.restclient.web;

import com.configure.restclient.entity.EmployeeEntity;
import com.configure.restclient.service.EmployeeService;
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

    @Autowired
    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeEntity> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        EmployeeEntity employeeEntity = employeeService.getEmployee(employeeId);
        return new ResponseEntity<>(employeeEntity, HttpStatus.OK);
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeEntity>> getAllEmployees(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        List<EmployeeEntity> allEmployees = employeeService.getAllEmployees(pageNumber, pageSize);
        return new ResponseEntity<>(allEmployees, HttpStatus.OK);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeEntity> createEmployee(@RequestBody EmployeeEntity newEmployee) {
        EmployeeEntity savedEmployee = employeeService.createNewEemployee(newEmployee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{employeeId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeEntity> updateEmplyee(@PathVariable(name = "employeeId") Long employeeId, @RequestBody EmployeeEntity employeeTobeUpdated) {
        EmployeeEntity updatedEmployee = employeeService.updateEmployee(employeeId, employeeTobeUpdated);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

}
