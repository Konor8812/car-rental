package com.illia.carrental.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication

public class CarRentalCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalCoreApplication.class, args);
    }
}
