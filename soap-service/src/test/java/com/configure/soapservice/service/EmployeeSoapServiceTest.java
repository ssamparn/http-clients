package com.configure.soapservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.configure.soapservice.domain.EmployeeRecord;
import com.configure.soapservice.repository.EmployeeRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeSoapServiceTest {

    private EmployeeSoapService employeeSoapService;

    @BeforeEach
    void setUp() {
        employeeSoapService = new EmployeeSoapService(new EmployeeRepository());
    }

    @Test
    void getByIdReturnsSeedEmployeeWhenIdExists() {
        Optional<EmployeeRecord> employee = employeeSoapService.getById(1L);

        assertThat(employee).isPresent();
        assertThat(employee.orElseThrow().firstName()).isEqualTo("Alice");
    }

    @Test
    void createStoresEmployeeWithGeneratedId() {
        EmployeeRecord createdEmployee = employeeSoapService.create(
                new EmployeeRecord(0L, "Diana", "Stone", "Marketing", "diana.stone@sample.com"));

        assertThat(createdEmployee.id()).isGreaterThan(0L);
        assertThat(createdEmployee.firstName()).isEqualTo("Diana");
    }

    @Test
    void getAllIncludesSeedDataAndNewRecords() {
        employeeSoapService.create(new EmployeeRecord(0L, "Evan", "Khan", "Support", "evan.khan@sample.com"));

        List<EmployeeRecord> employees = employeeSoapService.getAll();

        assertThat(employees).hasSize(4);
        assertThat(employees).extracting(EmployeeRecord::firstName).contains("Evan");
    }
}

