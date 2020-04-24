package com.configure.restservice.service;

import com.configure.restservice.entity.EmployeeEntity;
import com.configure.restservice.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceResponseFactory {

    public Employee createGetEmployeeResponse(EmployeeEntity employeeEntity) {
        return Employee.builder()
                .id(employeeEntity.getId())
                .firstName(employeeEntity.getFirstName())
                .lastName(employeeEntity.getLastName())
                .yearlyIncome(employeeEntity.getYearlyIncome())
                .build();
    }

    public List<Employee> createGetAllEmployeesResponse(List<EmployeeEntity> allEmployees) {
        return allEmployees.stream()
                .map(employeeEntity -> {
                    Employee employee = new Employee();
                    employee.setId(employeeEntity.getId());
                    employee.setFirstName(employeeEntity.getFirstName());
                    employee.setLastName(employeeEntity.getLastName());
                    employee.setYearlyIncome(employeeEntity.getYearlyIncome());
                    return employee;
                }).collect(Collectors.toList());
    }

    public Employee createNewEmployeeResponse(EmployeeEntity employeeEntity) {
        return Employee.builder()
                .id(employeeEntity.getId())
                .firstName(employeeEntity.getFirstName())
                .lastName(employeeEntity.getLastName())
                .yearlyIncome(employeeEntity.getYearlyIncome())
                .build();
    }

    public Employee createUpdateEmployeeResponse(EmployeeEntity employeeEntity) {
        return Employee.builder()
                .id(employeeEntity.getId())
                .firstName(employeeEntity.getFirstName())
                .lastName(employeeEntity.getLastName())
                .yearlyIncome(employeeEntity.getYearlyIncome())
                .build();
    }
}
