package com.configure.webclient.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
public abstract class AbstractWebClient<T, U> {

    protected Mono<U> doGet(String uriTemplate, Class<U> responseType, Object... uriVariables) {
        // Resilience decorators are intentionally applied last to include HTTP and decoding failures.
        return withResilience(getWebClient()
                .method(HttpMethod.GET)
                .uri(uriTemplate, uriVariables)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.createException().flatMap(Mono::error))
                .bodyToMono(responseType));
    }

    protected Flux<U> doGetMany(String uriTemplate, Class<U> responseType, Object... uriVariables) {
        return withResilience(getWebClient()
                .method(HttpMethod.GET)
                .uri(uriTemplate, uriVariables)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.createException().flatMap(Mono::error))
                .bodyToFlux(responseType));
    }

    protected Mono<U> doPost(String uriTemplate,
                             Mono<T> requestBody,
                             Class<T> requestType,
                             Class<U> responseType,
                             Consumer<HttpHeaders> headersCustomizer,
                             Object... uriVariables) {
        return withResilience(getWebClient()
                .method(HttpMethod.POST)
                .uri(uriTemplate, uriVariables)
                .accept(MediaType.APPLICATION_JSON)
                .headers(headersCustomizer)
                .body(requestBody, requestType)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse.createException().flatMap(Mono::error))
                .bodyToMono(responseType));
    }


    private Mono<U> withResilience(Mono<U> responseMono) {
        return responseMono
                .transformDeferred(CircuitBreakerOperator.of(getCircuitBreaker()))
                .transformDeferred(RetryOperator.of(getRetry()))
                .doOnError(error -> log.error("{} | {} failed", getService(), getOperation(), error));
    }

    private Flux<U> withResilience(Flux<U> responseFlux) {
        return responseFlux
                .transformDeferred(CircuitBreakerOperator.of(getCircuitBreaker()))
                .transformDeferred(RetryOperator.of(getRetry()))
                .doOnError(error -> log.error("{} | {} failed", getService(), getOperation(), error));
    }

    protected abstract WebClient getWebClient();

    protected abstract CircuitBreaker getCircuitBreaker();

    protected abstract Retry getRetry();

    public abstract String getService();

    public abstract String getOperation();
}
