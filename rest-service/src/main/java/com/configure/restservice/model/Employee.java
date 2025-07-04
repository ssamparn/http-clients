package com.configure.restservice.model;

import lombok.Builder;

@Builder
public record Employee(
        Long id,
        String firstName,
        String lastName,
        Long yearlyIncome
)  {
}
