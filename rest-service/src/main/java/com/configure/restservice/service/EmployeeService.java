package com.configure.restservice.service;

import com.configure.restservice.entity.EmployeeEntity;
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
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "Get-Employee", key = "#employeeId")
    public EmployeeEntity getEmployee(Long employeeId) {
        Optional<EmployeeEntity> employee = repository.findById(employeeId);
        employee.orElseThrow(EmployeeNotFoundException::new);
        return employee.get();
    }

    public List<EmployeeEntity> getAllEmployees(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<EmployeeEntity> pagedResult = repository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public EmployeeEntity createNewEmployee(EmployeeEntity newEmployee) {
        return repository.save(newEmployee);
    }

    @CachePut(value = "Update-Employee", key = "#employeeId")
    public EmployeeEntity updateEmployee(Long employeeId, EmployeeEntity employeeTobeUpdated) {
        EmployeeEntity employeeEntity = repository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        employeeEntity.setFirstName(employeeTobeUpdated.getFirstName());
        employeeEntity.setLastName(employeeTobeUpdated.getLastName());
        employeeEntity.setId(employeeTobeUpdated.getId());
        employeeEntity.setYearlyIncome(employeeTobeUpdated.getYearlyIncome());

        EmployeeEntity savedEntity = repository.save(employeeEntity);
        return savedEntity;
    }

    @CacheEvict(value = "Delete-Employee")
    public void deleteEmployee(Long employeeId) {
        repository.deleteById(employeeId);
    }
}
