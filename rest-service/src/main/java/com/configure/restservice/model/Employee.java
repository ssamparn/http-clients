package com.configure.restservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class Employee implements Serializable {

    private static final long serialVersionUID = -6209873697684245092L;

    private Long id;
    private String firstName;
    private String lastName;
    private Long yearlyIncome;

}
