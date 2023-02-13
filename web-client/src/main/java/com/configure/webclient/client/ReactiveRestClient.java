package com.configure.webclient.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public abstract class ReactiveRestClient<REQUEST, RESPONSE> {

    public Mono<RESPONSE> doGet(HttpHeaders requestHeaders, Map<String, ?> pathVariables, MultiValueMap<String, String> queryParams, Class<RESPONSE> responseType) {
        return getWebClient()
                .method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.queryParams(queryParams).build(pathVariables))
                .headers(httpHeaders -> httpHeaders.addAll(requestHeaders))
                .retrieve()
                .bodyToMono(responseType);
    }

    public Mono<RESPONSE> doPost(REQUEST requestBody, HttpHeaders requestHeaders, Class<RESPONSE> responseType) {
        return getWebClient()
                .method(HttpMethod.POST)
                .uri("")
                .headers(httpHeaders -> httpHeaders.addAll(requestHeaders))
                .body(Mono.fromSupplier(() -> requestBody), requestBody.getClass())
                .retrieve()
                .bodyToMono(responseType);
    }

    protected abstract WebClient getWebClient();
    protected abstract String getBaseUrl();
    public abstract String getService();
    public abstract String getOperation();
}
