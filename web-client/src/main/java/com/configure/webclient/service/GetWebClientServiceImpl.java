package com.configure.webclient.service;

import com.configure.webclient.client.GetWebClient;
import com.configure.webclient.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWebClientServiceImpl implements GetWebClientService {

    private final GetWebClient getWebClient;

    @Override
    public Mono<ResponseEntity<Employee>> getEmployeeById(String employeeId) {
        return getWebClient.doGetById(employeeId);
    }

    @Override
    public Mono<ResponseEntity<List<Employee>>> getAllEmployees() {
        return getWebClient.doGetAll();
    }
}
