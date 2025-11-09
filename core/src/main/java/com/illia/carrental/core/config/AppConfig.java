package com.illia.carrental.core.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = {"com.illia.carrental.core.config"})
public class AppConfig {
}
