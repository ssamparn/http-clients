package com.configure.webclient.config;

import com.configure.webclient.exception.ConfigurationException;
import com.configure.webclient.properties.KeyStoreProperties;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

@Slf4j
@Configuration
public class SslConfig {

    private final KeyStoreProperties keystoreProperties;

    @Autowired
    public SslConfig(KeyStoreProperties keystoreProperties) {
        this.keystoreProperties = keystoreProperties;
    }

    @Bean
    public SslContext sslContext(KeyManagerFactory keyManagerFactory,
                                 TrustManagerFactory trustManagerFactory) throws SSLException {
        return SslContextBuilder.forClient()
                .keyManager(keyManagerFactory)
                .trustManager(trustManagerFactory)
                .build();
    }

    @Bean("keyStore")
    public KeyStore keyStore() throws ConfigurationException {
        return KeyStoreUtil.loadKeystore(keystoreProperties.getKeyStore(), keystoreProperties.getKeyStorePassword());
    }

    @Bean
    public KeyManagerFactory keyManagerFactory(KeyStore keyStore) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keystoreProperties.getKeyStorePassword().toCharArray());
        return keyManagerFactory;
    }

    @Bean("trustStore")
    public KeyStore trustStore() throws ConfigurationException {
        return KeyStoreUtil.loadKeystore(keystoreProperties.getTrustStore(), keystoreProperties.getTrustStorePassword());
    }

    @Bean
    public TrustManagerFactory trustManagerFactory(KeyStore trustStore) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);
        return trustManagerFactory;
    }
}
