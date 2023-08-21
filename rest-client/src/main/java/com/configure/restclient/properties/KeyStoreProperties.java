package com.configure.restclient.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "certstore")
public class KeyStoreProperties {

    private String trustStore;

    private String trustStorePassword;

    private String keyStore;

    private String keyStorePassword;

    private String type;
}

