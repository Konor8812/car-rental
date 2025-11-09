package com.illia.carrental.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.illia.carrental.auth")
public record AuthConfig(String authenticationUrl,
                         String clientSecret) {
}
