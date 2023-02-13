package com.configure.restclient.client.resttemplate;

import com.configure.restclient.client.RestClient;
import com.configure.restclient.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class PostEmployeeRestClient extends RestClient<Employee, Employee> {

    private static final String CUSTOM_HEADER = "Custom-Header";
    private static final String SERVICE = "EmployeeService";
    private static final String OPERATION = "PostEmployee";

    private final RestTemplate restTemplate;

    @Value("${employeeService.http.url}")
    private String serviceUrl;

    @Autowired
    public PostEmployeeRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Employee createNewEmployee(Employee newEmployee) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "EmployeeRestClient");
        headers.add("Accept-Language", "en-US");
        headers.add("Custom-Header", CUSTOM_HEADER);

        return doPost(newEmployee, headers, Employee.class);
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Override
    protected String getBaseUrl() {
        return serviceUrl;
    }

    @Override
    public String getService() {
        return SERVICE;
    }

    @Override
    public String getOperation() {
        return OPERATION;
    }
}
