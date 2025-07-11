package com.configure.restservice.service;

import com.configure.restservice.entity.EmployeeEntity;
import com.configure.restservice.model.Employee;
import com.configure.restservice.repository.EmployeeRepository;
import com.configure.restservice.web.exception.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Cacheable(cacheNames = "Get-Employee", key = "#employeeId")
    public EmployeeEntity getEmployee(Long employeeId) {
        return this.repository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public List<EmployeeEntity> getAllEmployees(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<EmployeeEntity> pagedResult = this.repository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public EmployeeEntity createNewEmployee(Employee newEmployee) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setFirstName(newEmployee.firstName());
        entity.setLastName(newEmployee.lastName());
        entity.setYearlyIncome(newEmployee.yearlyIncome());

        return this.repository.save(entity);
    }

    @CachePut(value = "Update-Employee", key = "#employeeId")
    public EmployeeEntity updateEmployee(Long employeeId, Employee employee) {
        EmployeeEntity employeeEntity = repository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        employeeEntity.setFirstName(employee.firstName());
        employeeEntity.setLastName(employee.lastName());
        employeeEntity.setYearlyIncome(employee.yearlyIncome());

        return this.repository.save(employeeEntity);
    }

    @CacheEvict(value = "Delete-Employee")
    public void deleteEmployee(Long employeeId) {
        this.repository.deleteById(employeeId);
    }
}
