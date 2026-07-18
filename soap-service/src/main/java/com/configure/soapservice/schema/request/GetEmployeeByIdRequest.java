package com.configure.soapservice.schema.request;

import com.configure.soapservice.schema.SoapNamespace;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(namespace = SoapNamespace.URI, propOrder = {"id"})
@XmlRootElement(name = "GetEmployeeByIdRequest", namespace = SoapNamespace.URI)
public class GetEmployeeByIdRequest {

    private long id;

    @XmlElement(namespace = SoapNamespace.URI)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

