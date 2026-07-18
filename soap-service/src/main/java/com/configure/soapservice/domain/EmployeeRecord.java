package com.configure.soapservice.domain;

public record EmployeeRecord(long id,
                             String firstName,
                             String lastName,
                             String department,
                             String email) {
}

