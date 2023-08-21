package com.configure.restclient.client.resttemplate;

import com.configure.restclient.client.RestClient;
import com.configure.restclient.model.Employee;
import com.configure.restclient.properties.HttpClientConnectionProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GetEmployeeRestClient extends RestClient<String, Employee> {

    private static final String CUSTOM_HEADER = "Custom-Header";
    private static final String SERVICE = "EmployeeService";
    private static final String OPERATION = "GetEmployee";

    private final RestTemplate restTemplate;
    private final HttpClientConnectionProperties employeeServiceClientConnectionProperties;

    @Autowired
    public GetEmployeeRestClient(@Qualifier("employeeServiceRestTemplate") RestTemplate restTemplate,
                                 @Qualifier("employeeServiceHttpClientConnectionProperties") HttpClientConnectionProperties employeeServiceClientConnectionProperties) {
        this.restTemplate = restTemplate;
        this.employeeServiceClientConnectionProperties = employeeServiceClientConnectionProperties;
    }

    public Employee getEmployeeById(Long employeeId) {
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", employeeId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "EmployeeRestClient");
        headers.add("Accept-Language", "en-US");
        headers.add("Custom-Header", CUSTOM_HEADER);

        Employee employee = doGet(pathVariables, null, headers, Employee.class);
        return employee;
    }

    public List<Employee> getAllEmployees(int pageNumber, int pageSize) {
        String requestUri = employeeServiceClientConnectionProperties.getUrl() + "/all?pageNumber={pageNumber}&pageSize={pageSize}";

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("pageNumber", Integer.toString(pageNumber));
        queryParams.put("pageSize", Long.toString(pageSize));

        ResponseEntity<Employee[]> allEmployees = restTemplate.getForEntity(requestUri, Employee[].class, queryParams);

        return allEmployees.getBody() != null ? Arrays.asList(allEmployees.getBody()) : Collections.emptyList();
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    @Override
    protected String getBaseUrl() {
        return this.employeeServiceClientConnectionProperties.getUrl() + "/{id}";
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
