package com.configure.webclient.config;

import com.configure.webclient.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Slf4j
public class KeyStoreUtil {

    private static final String JAVA_KEY_STORE = "JKS";

    public static KeyStore loadKeystore(Resource keyStoreResource, String keyStorePassword) throws ConfigurationException {
        KeyStore keyStore;

        try (InputStream inputStream = keyStoreResource.getInputStream()) {
            keyStore = KeyStore.getInstance(JAVA_KEY_STORE);
            keyStore.load(inputStream, keyStorePassword.toCharArray());
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            throw new ConfigurationException("Failed to load keystore " + keyStoreResource.getFilename(), e);
        }
        log.info("Loaded keystore: {}", keyStoreResource.getFilename());

        return keyStore;
    }
}
