package com.configure.soapservice.schema.response;

import com.configure.soapservice.schema.Employee;
import com.configure.soapservice.schema.SoapNamespace;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(namespace = SoapNamespace.URI, propOrder = {"employees", "count", "message"})
@XmlRootElement(name = "GetAllEmployeesResponse", namespace = SoapNamespace.URI)
public class GetAllEmployeesResponse {

    private List<Employee> employees = new ArrayList<>();

    private int count;

    private String message;

    @XmlElement(name = "employees", namespace = SoapNamespace.URI)
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @XmlElement(namespace = SoapNamespace.URI)
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @XmlElement(namespace = SoapNamespace.URI)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

