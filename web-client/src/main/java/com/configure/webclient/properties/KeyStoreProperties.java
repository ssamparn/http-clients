package com.configure.webclient.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "client.ssl")
public class KeyStoreProperties {

    private ClassPathResource keyStore;

    private String keyStorePassword;

    private ClassPathResource trustStore;

    private String trustStorePassword;

    private String type;

}
