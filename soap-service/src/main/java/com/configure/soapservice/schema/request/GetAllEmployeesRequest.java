package com.configure.soapservice.schema.request;

import com.configure.soapservice.schema.SoapNamespace;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = SoapNamespace.URI)
@XmlRootElement(name = "GetAllEmployeesRequest", namespace = SoapNamespace.URI)
public class GetAllEmployeesRequest {
}

