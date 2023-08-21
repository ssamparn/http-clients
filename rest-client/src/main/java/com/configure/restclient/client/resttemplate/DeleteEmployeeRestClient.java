package com.configure.restclient.client.resttemplate;

import com.configure.restclient.client.RestClient;
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
public class DeleteEmployeeRestClient extends RestClient<Void, Void> {

    private static final String CUSTOM_HEADER = "Custom-Header";
    private static final String SERVICE = "EmployeeService";
    private static final String OPERATION = "DeleteEmployee";

    private final RestTemplate restTemplate;
    private final HttpClientConnectionProperties employeeServiceClientConnectionProperties;


    @Autowired
    public DeleteEmployeeRestClient(@Qualifier("employeeServiceRestTemplate") RestTemplate restTemplate,
                                    @Qualifier("employeeServiceHttpClientConnectionProperties") HttpClientConnectionProperties employeeServiceClientConnectionProperties) {
        this.restTemplate = restTemplate;
        this.employeeServiceClientConnectionProperties = employeeServiceClientConnectionProperties;
    }

    public void deleteEmployee(Long employeeId) {
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", employeeId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "EmployeeRestClient");
        headers.add("Accept-Language", "en-US");
        headers.add("Custom-Header", CUSTOM_HEADER);

        doDelete(pathVariables, headers, Void.class);
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
