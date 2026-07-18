# SOAP Service Sample (Spring Boot 4.1.0, Java 25)

This module is a minimal SOAP service reference you can use to learn Spring-WS with Spring Boot.

## Operations implemented

1. `GetEmployeeById` - fetch an employee by unique id.
2. `CreateEmployee` - create a new employee.
3. `GetAllEmployees` - fetch all employees.

## Contract and WSDL

- XSD: `src/main/resources/xsd/employees.xsd`
- WSDL endpoint (after app startup): `http://localhost:7070/ws/employees.wsdl`
- SOAP endpoint URI: `http://localhost:7070/ws`
- Target namespace: `http://configure.com/soapservice/employees`

## Initial sample data

The in-memory repository starts with three employees:

- `id=1` Alice Johnson
- `id=2` Bob Smith
- `id=3` Carol Miller

## SOAP examples

Use the following headers for manual requests:

- `Content-Type: text/xml;charset=UTF-8`
- `SOAPAction: ""`

### 1) Create Employee

Request:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
				  xmlns:ns="http://configure.com/soapservice/employees">
	<soapenv:Header/>
	<soapenv:Body>
		<ns:CreateEmployeeRequest>
			<ns:employee>
				<ns:firstName>John</ns:firstName>
				<ns:lastName>Doe</ns:lastName>
				<ns:department>QA</ns:department>
				<ns:email>john.doe@sample.com</ns:email>
			</ns:employee>
		</ns:CreateEmployeeRequest>
	</soapenv:Body>
</soapenv:Envelope>
```

Success response:

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
	<SOAP-ENV:Body>
		<ns2:CreateEmployeeResponse xmlns:ns2="http://configure.com/soapservice/employees">
			<ns2:employee>
				<ns2:id>4</ns2:id>
				<ns2:firstName>John</ns2:firstName>
				<ns2:lastName>Doe</ns2:lastName>
				<ns2:department>QA</ns2:department>
				<ns2:email>john.doe@sample.com</ns2:email>
			</ns2:employee>
			<ns2:message>Employee created successfully</ns2:message>
		</ns2:CreateEmployeeResponse>
	</SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

Validation-style response when employee payload is missing:

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
	<SOAP-ENV:Body>
		<ns2:CreateEmployeeResponse xmlns:ns2="http://configure.com/soapservice/employees">
			<ns2:message>Employee payload is required</ns2:message>
		</ns2:CreateEmployeeResponse>
	</SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### 2) Get Employee By Id

Request:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
				  xmlns:ns="http://configure.com/soapservice/employees">
	<soapenv:Header/>
	<soapenv:Body>
		<ns:GetEmployeeByIdRequest>
			<ns:id>1</ns:id>
		</ns:GetEmployeeByIdRequest>
	</soapenv:Body>
</soapenv:Envelope>
```

Success response:

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
	<SOAP-ENV:Body>
		<ns2:GetEmployeeByIdResponse xmlns:ns2="http://configure.com/soapservice/employees">
			<ns2:employee>
				<ns2:id>1</ns2:id>
				<ns2:firstName>Alice</ns2:firstName>
				<ns2:lastName>Johnson</ns2:lastName>
				<ns2:department>Engineering</ns2:department>
				<ns2:email>alice.johnson@sample.com</ns2:email>
			</ns2:employee>
			<ns2:message>Employee found</ns2:message>
		</ns2:GetEmployeeByIdResponse>
	</SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

Not-found response:

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
	<SOAP-ENV:Body>
		<ns2:GetEmployeeByIdResponse xmlns:ns2="http://configure.com/soapservice/employees">
			<ns2:message>Employee not found for id: 999</ns2:message>
		</ns2:GetEmployeeByIdResponse>
	</SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### 3) Get All Employees

Request:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
				  xmlns:ns="http://configure.com/soapservice/employees">
	<soapenv:Header/>
	<soapenv:Body>
		<ns:GetAllEmployeesRequest/>
	</soapenv:Body>
</soapenv:Envelope>
```

Response:

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
	<SOAP-ENV:Body>
		<ns2:GetAllEmployeesResponse xmlns:ns2="http://configure.com/soapservice/employees">
			<ns2:employees>
				<ns2:id>1</ns2:id>
				<ns2:firstName>Alice</ns2:firstName>
				<ns2:lastName>Johnson</ns2:lastName>
				<ns2:department>Engineering</ns2:department>
				<ns2:email>alice.johnson@sample.com</ns2:email>
			</ns2:employees>
			<ns2:employees>
				<ns2:id>2</ns2:id>
				<ns2:firstName>Bob</ns2:firstName>
				<ns2:lastName>Smith</ns2:lastName>
				<ns2:department>Finance</ns2:department>
				<ns2:email>bob.smith@sample.com</ns2:email>
			</ns2:employees>
			<ns2:employees>
				<ns2:id>3</ns2:id>
				<ns2:firstName>Carol</ns2:firstName>
				<ns2:lastName>Miller</ns2:lastName>
				<ns2:department>Human Resources</ns2:department>
				<ns2:email>carol.miller@sample.com</ns2:email>
			</ns2:employees>
			<ns2:count>3</ns2:count>
			<ns2:message>Employees fetched successfully</ns2:message>
		</ns2:GetAllEmployeesResponse>
	</SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
