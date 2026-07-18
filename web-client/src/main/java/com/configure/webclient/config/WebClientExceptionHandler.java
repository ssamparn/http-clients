package com.configure.webclient.config;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class WebClientExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException exception) {
        HttpStatusCode statusCode = exception.getStatusCode();
        String responseBody = exception.getResponseBodyAsString();
        String body = responseBody.isBlank() ? exception.getMessage() : responseBody;
        return ResponseEntity.status(statusCode).body(body);
    }
}

