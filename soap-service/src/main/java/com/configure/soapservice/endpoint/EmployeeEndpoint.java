package com.configure.soapservice.endpoint;

import com.configure.soapservice.domain.EmployeeRecord;
import com.configure.soapservice.schema.request.CreateEmployeeRequest;
import com.configure.soapservice.schema.response.CreateEmployeeResponse;
import com.configure.soapservice.schema.Employee;
import com.configure.soapservice.schema.request.GetAllEmployeesRequest;
import com.configure.soapservice.schema.response.GetAllEmployeesResponse;
import com.configure.soapservice.schema.request.GetEmployeeByIdRequest;
import com.configure.soapservice.schema.response.GetEmployeeByIdResponse;
import com.configure.soapservice.service.EmployeeSoapService;
import java.util.List;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class EmployeeEndpoint {

    private static final String NAMESPACE_URI = "http://configure.com/soapservice/employees";

    private final EmployeeSoapService employeeSoapService;

    public EmployeeEndpoint(EmployeeSoapService employeeSoapService) {
        this.employeeSoapService = employeeSoapService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateEmployeeRequest")
    @ResponsePayload
    public CreateEmployeeResponse createEmployee(@RequestPayload CreateEmployeeRequest request) {
        if (request == null || request.getEmployee() == null) {
            CreateEmployeeResponse errorResponse = new CreateEmployeeResponse();
            errorResponse.setMessage("Employee payload is required");
            return errorResponse;
        }

        EmployeeRecord recordToCreate = new EmployeeRecord(
                0L,
                request.getEmployee().getFirstName(),
                request.getEmployee().getLastName(),
                request.getEmployee().getDepartment(),
                request.getEmployee().getEmail());

        EmployeeRecord createdRecord = employeeSoapService.create(recordToCreate);

        CreateEmployeeResponse response = new CreateEmployeeResponse();
        response.setEmployee(toSoapEmployee(createdRecord));
        response.setMessage("Employee created successfully");
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetEmployeeByIdRequest")
    @ResponsePayload
    public GetEmployeeByIdResponse getEmployeeById(@RequestPayload GetEmployeeByIdRequest request) {
        GetEmployeeByIdResponse response = new GetEmployeeByIdResponse();
        employeeSoapService.getById(request.getId())
                .ifPresentOrElse(
                        employeeRecord -> {
                            response.setEmployee(toSoapEmployee(employeeRecord));
                            response.setMessage("Employee found");
                        },
                        () -> response.setMessage("Employee not found for id: " + request.getId()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllEmployeesRequest")
    @ResponsePayload
    public GetAllEmployeesResponse getAllEmployees(@RequestPayload GetAllEmployeesRequest request) {
        List<EmployeeRecord> employees = employeeSoapService.getAll();

        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        response.getEmployees().addAll(employees.stream().map(this::toSoapEmployee).toList());
        response.setCount(employees.size());
        response.setMessage("Employees fetched successfully");
        return response;
    }

    private Employee toSoapEmployee(EmployeeRecord employeeRecord) {
        Employee employee = new Employee();
        employee.setId(employeeRecord.id());
        employee.setFirstName(employeeRecord.firstName());
        employee.setLastName(employeeRecord.lastName());
        employee.setDepartment(employeeRecord.department());
        employee.setEmail(employeeRecord.email());
        return employee;
    }
}

