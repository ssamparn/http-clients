package com.configure.restservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Configuration
public class RedisLocalConfiguration {

    private final redis.embedded.RedisServer redisServer;

    public RedisLocalConfiguration(@Value("${spring.redis.port}") final int redisPort) {
        this.redisServer = new redis.embedded.RedisServer(redisPort);
    }

    @PostConstruct
    public void startRedis() {
        this.redisServer.start();
    }

    @PreDestroy
    public void destroyRedis() {
        this.redisServer.stop();
    }
}
