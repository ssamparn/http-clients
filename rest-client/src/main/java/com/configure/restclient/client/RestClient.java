package com.configure.restclient.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
public abstract class RestClient<REQUEST, RESPONSE> {

    public RESPONSE doGet(Map<String, ?> uriVariables, MultiValueMap<String, String> queryParams, HttpHeaders requestHeaders, Class<RESPONSE> returnType) {
        RESPONSE response = null;
        ResponseEntity<RESPONSE> responseEntity;
        String getUrl = createUri(getBaseUrl(), uriVariables, queryParams);
        try {
            HttpEntity<REQUEST> httpEntity = new HttpEntity<>(requestHeaders);
            responseEntity = getRestTemplate().exchange(getUrl, HttpMethod.GET, httpEntity, returnType);
            if (!responseEntity.hasBody()) {
                return response;
            }
            response = responseEntity.getBody();
            return response;
        } catch (RestClientException | IllegalArgumentException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public RESPONSE doPost(REQUEST requestBody, HttpHeaders requestHeaders, Class<RESPONSE> returnType) {
        RESPONSE response = null;
        ResponseEntity<RESPONSE> responseEntity;
        String postUrl = UriComponentsBuilder.fromHttpUrl(getBaseUrl()).toUriString();

        try {
            HttpEntity<REQUEST> requestHttpEntity = new HttpEntity<>(requestBody, requestHeaders);
            responseEntity = getRestTemplate().exchange(postUrl, HttpMethod.POST, requestHttpEntity, returnType);

            if (!responseEntity.hasBody()) {
                return response;
            }
            response = responseEntity.getBody();
            return response;
        } catch (RestClientException | IllegalArgumentException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public RESPONSE doPut(REQUEST requestBody, Map<String, ?> uriVariables, HttpHeaders requestHeaders, Class<RESPONSE> returnType) {
        RESPONSE response = null;
        ResponseEntity<RESPONSE> responseEntity;
        String putUrl = createUri(getBaseUrl(), uriVariables, null);

        try {
            HttpEntity<REQUEST> requestHttpEntity = new HttpEntity<>(requestBody, requestHeaders);
            responseEntity = getRestTemplate().exchange(putUrl, HttpMethod.PUT, requestHttpEntity, returnType);

            if (!responseEntity.hasBody()) {
                return response;
            }
            response = responseEntity.getBody();
            return response;
        } catch (RestClientException | IllegalArgumentException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public RESPONSE doDelete(Map<String, ?> uriVariables, HttpHeaders requestHeaders, Class<RESPONSE> returnType) {
        RESPONSE response = null;
        ResponseEntity<RESPONSE> responseEntity;
        String deleteUrl = createUri(getBaseUrl(), uriVariables, null);
        try {
            HttpEntity requestHttpEntity = new HttpEntity<>(requestHeaders);
            responseEntity = getRestTemplate().exchange(deleteUrl, HttpMethod.DELETE, requestHttpEntity, returnType);
            if (!responseEntity.hasBody()) {
                return response;
            }
            response = responseEntity.getBody();
            return response;
        } catch (RestClientException | IllegalArgumentException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


    private String createUri(String baseUrl, Map<String, ?> uriVariables, MultiValueMap<String, String> queryParams) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl).queryParams(queryParams);
        return uriComponentsBuilder.buildAndExpand(uriVariables).toUriString();
    }

    protected abstract RestTemplate getRestTemplate();
    protected abstract String getBaseUrl();
    public abstract String getService();
    public abstract String getOperation();

}
