package com.configure.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RestServiceApplication {

	static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
	}

}
