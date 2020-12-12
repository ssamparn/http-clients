package com.configure.restclient.config;

public interface ConnectionProperties {
    String getUrl();
    int getConnectionTimeout();
    int getSocketTimeout();
    int getRequestTimeout();
    int getMaxHttpPoolsize();
    int getMaxHttpRouteConnection();
    int getDefaultKeepAliveTime();
    int getIdleConnectionWaitTime();
}