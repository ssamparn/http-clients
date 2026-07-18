package com.configure.soapservice.repository;

import com.configure.soapservice.domain.EmployeeRecord;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    private final Map<Long, EmployeeRecord> employees = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    public EmployeeRepository() {
        save(new EmployeeRecord(0L, "Alice", "Johnson", "Engineering", "alice.johnson@sample.com"));
        save(new EmployeeRecord(0L, "Bob", "Smith", "Finance", "bob.smith@sample.com"));
        save(new EmployeeRecord(0L, "Carol", "Miller", "Human Resources", "carol.miller@sample.com"));
    }

    public Optional<EmployeeRecord> findById(long id) {
        return Optional.ofNullable(employees.get(id));
    }

    public EmployeeRecord save(EmployeeRecord employeeRecord) {
        long generatedId = idSequence.incrementAndGet();
        EmployeeRecord employeeToStore = new EmployeeRecord(
                generatedId,
                employeeRecord.firstName(),
                employeeRecord.lastName(),
                employeeRecord.department(),
                employeeRecord.email());
        employees.put(generatedId, employeeToStore);
        return employeeToStore;
    }

    public Collection<EmployeeRecord> findAll() {
        return employees.values().stream().sorted(Comparator.comparingLong(EmployeeRecord::id)).toList();
    }
}

