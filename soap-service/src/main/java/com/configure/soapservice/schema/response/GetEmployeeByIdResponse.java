package com.configure.soapservice.schema.response;

import com.configure.soapservice.schema.Employee;
import com.configure.soapservice.schema.SoapNamespace;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(namespace = SoapNamespace.URI, propOrder = {"employee", "message"})
@XmlRootElement(name = "GetEmployeeByIdResponse", namespace = SoapNamespace.URI)
public class GetEmployeeByIdResponse {

    private Employee employee;

    private String message;

    @XmlElement(namespace = SoapNamespace.URI)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @XmlElement(namespace = SoapNamespace.URI)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

