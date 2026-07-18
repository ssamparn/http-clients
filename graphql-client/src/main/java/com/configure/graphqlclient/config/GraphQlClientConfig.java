package com.configure.graphqlclient.config;

import com.configure.graphqlclient.properties.GraphqlClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQlClientConfig {

    @Bean
    @ConfigurationProperties(prefix = "graphql.client")
    public GraphqlClientProperties graphqlClientProperties() {
        return new GraphqlClientProperties();
    }

    @Bean
    public WebClient graphqlWebClient(GraphqlClientProperties graphqlClientProperties) {
        return WebClient.builder()
                .baseUrl(graphqlClientProperties.getUrl())
                .build();
    }

    @Bean
    public HttpGraphQlClient httpGraphQlClient(WebClient graphqlWebClient) {
        return HttpGraphQlClient.builder(graphqlWebClient)
                .build();
    }
}

