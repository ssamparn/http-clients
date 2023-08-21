package com.configure.restclient.config;

import com.configure.restclient.exception.ConfigurationException;
import com.configure.restclient.exception.SslConfigException;
import com.configure.restclient.properties.KeyStoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

@Slf4j
@Configuration
public class SslConfig {

    private final KeyStoreProperties keyStoreProperties;

    public SslConfig(KeyStoreProperties keyStoreProperties) {
        this.keyStoreProperties = keyStoreProperties;
    }

    @Bean
    public Registry<ConnectionSocketFactory> registry(SSLContext sslContext) {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContext))
                .build();
    }

    @Bean
    public Registry<ConnectionSocketFactory> registryForIpi(
            SSLContext sslContextForIpi) {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContextForIpi))
                .build();
    }

    @Bean
    public SSLContext sslContext(
            KeyStoreProperties keyStoreProperties,
            @Qualifier("keyStore") KeyStore keyStore,
            @Qualifier("trustStore") KeyStore trustStore) throws ConfigurationException {

        try {
            return new SSLContextBuilder()
                    .loadKeyMaterial(keyStore, keyStoreProperties.getKeyStorePassword().toCharArray())
                    .loadTrustMaterial(trustStore, TrustSelfSignedStrategy.INSTANCE)
                    .build();
        } catch (NoSuchAlgorithmException | KeyStoreException |
                 UnrecoverableKeyException | KeyManagementException e) {
            throw new ConfigurationException("Failed to create SSL Context", e);
        }
    }

    @Bean
    public SSLContext sslContextForIpi(
            KeyStoreProperties keyStoreProperties,
            @Qualifier("keyStore") KeyStore keyStore,
            @Qualifier("trustStore") KeyStore trustStore) throws ConfigurationException {

        try {
            return new SSLContextBuilder()
                    .setProtocol("TLSv1.2")
                    .loadKeyMaterial(keyStore, keyStoreProperties.getKeyStorePassword().toCharArray())
                    .loadTrustMaterial(trustStore, TrustSelfSignedStrategy.INSTANCE)
                    .build();
        } catch (NoSuchAlgorithmException | KeyStoreException |
                 UnrecoverableKeyException | KeyManagementException e) {
            throw new ConfigurationException("Failed to create SSL Context", e);
        }
    }

    @Bean(name = "keyStore")
    public KeyStore getKeyStore() throws SslConfigException {
        log.info("loading keystore");

        return createKeyStoreFromBase64EncodedString(
                keyStoreProperties.getKeyStore(),
                keyStoreProperties.getKeyStorePassword(),
                keyStoreProperties.getType()
        );
    }

    @Bean(name = "trustStore")
    public KeyStore getTrustStore() throws SslConfigException {
        log.info("loading truststore");

        return createKeyStoreFromBase64EncodedString(
                keyStoreProperties.getTrustStore(),
                keyStoreProperties.getTrustStorePassword(),
                keyStoreProperties.getType()
        );
    }

    private KeyStore createKeyStoreFromBase64EncodedString(
            String base64EncodedKeyStore,
            String keyStorePassword,
            String keyStoreType
    ) throws SslConfigException {

        try (InputStream inputStream = new ByteArrayInputStream(
                base64EncodedKeyStore.getBytes(StandardCharsets.UTF_8));
             Base64InputStream keystoreInputStream = new Base64InputStream(inputStream)) {
            KeyStore keystore = KeyStore.getInstance(keyStoreType);
            keystore.load(keystoreInputStream, keyStorePassword.toCharArray());

            return keystore;
        } catch (GeneralSecurityException | IOException e) {
            throw new SslConfigException("Failed to load keystore/truststore ", e);
        }
    }
}
