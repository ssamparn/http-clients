package com.configure.restclient.client.resttemplate;

import com.configure.restclient.client.RestClient;
import com.configure.restclient.model.Employee;
import com.configure.restclient.properties.HttpClientConnectionProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PutEmployeeRestClient extends RestClient<Employee, Employee> {

    private static final String CUSTOM_HEADER = "Custom-Header";
    private static final String SERVICE = "EmployeeService";
    private static final String OPERATION = "UpdateEmployee";

    private final RestTemplate restTemplate;

    private final HttpClientConnectionProperties employeeServiceClientConnectionProperties;

    @Autowired
    public PutEmployeeRestClient(@Qualifier("employeeServiceRestTemplate") RestTemplate restTemplate,
                                 @Qualifier("employeeServiceHttpClientConnectionProperties") HttpClientConnectionProperties employeeServiceClientConnectionProperties) {
        this.restTemplate = restTemplate;
        this.employeeServiceClientConnectionProperties = employeeServiceClientConnectionProperties;
    }

    public Employee updateEmployee(Employee employeeToBeUpdated, Long employeeId) {
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", employeeId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "EmployeeRestClient");
        headers.add("Accept-Language", "en-US");
        headers.add("Custom-Header", CUSTOM_HEADER);

        return doPut(employeeToBeUpdated, pathVariables, headers, Employee.class);
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Override
    protected String getBaseUrl() {
        return employeeServiceClientConnectionProperties.getUrl() + "/{id}";
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
