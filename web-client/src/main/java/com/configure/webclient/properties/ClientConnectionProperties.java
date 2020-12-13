package com.configure.webclient.properties;

import lombok.Data;

@Data
public class ClientConnectionProperties {

    private String url;
    private int readTimeout;
    private int writeTimeout;
    private int connectionTimeout;

}
