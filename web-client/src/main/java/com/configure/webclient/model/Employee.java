package com.configure.webclient.model;

import java.io.Serializable;
import java.io.Serial;

public record Employee(Long id, String firstName, String lastName, Long yearlyIncome) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6209873697684245092L;


}
