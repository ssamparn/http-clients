package com.configure.soapservice.service;

import com.configure.soapservice.domain.EmployeeRecord;
import com.configure.soapservice.repository.EmployeeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSoapService {

    private final EmployeeRepository employeeRepository;

    public EmployeeSoapService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<EmployeeRecord> getById(long id) {
        return employeeRepository.findById(id);
    }

    public EmployeeRecord create(EmployeeRecord employeeRecord) {
        return employeeRepository.save(employeeRecord);
    }

    public List<EmployeeRecord> getAll() {
        return employeeRepository.findAll()
                .stream()
                .toList();
    }
}

