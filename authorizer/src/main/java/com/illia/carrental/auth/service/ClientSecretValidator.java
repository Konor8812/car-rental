package com.illia.carrental.auth.service;

import com.illia.carrental.auth.commons.exception.ClientSecretAuthException;
import com.illia.carrental.auth.config.ClientSecretAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientSecretValidator {
    private final ClientSecretAuthConfig clientSecretAuthConfig;

    public void validate(String clientSecret) {
        if (!clientSecretAuthConfig.clientSecrets().contains(clientSecret)) {
            throw new ClientSecretAuthException();
        }

    }
}
