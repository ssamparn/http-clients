package com.configure.soapservice.schema.request;

import com.configure.soapservice.schema.Employee;
import com.configure.soapservice.schema.SoapNamespace;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(namespace = SoapNamespace.URI, propOrder = {"employee"})
@XmlRootElement(name = "CreateEmployeeRequest", namespace = SoapNamespace.URI)
public class CreateEmployeeRequest {

    private Employee employee;

    @XmlElement(namespace = SoapNamespace.URI, required = true)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}

