package com.illia.carrental.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "com.illia.carrental.auth")
public record ClientSecretAuthConfig(List<String> clientSecrets) {
}
