package com.illia.carrental.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.illia.carrental.core.commons.exception.DownstreamServiceUnavailableException;
import com.illia.carrental.core.config.AuthConfig;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.request.AuthenticateUserRequestBody;
import com.illia.carrental.core.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final HttpClient httpClient;
    private final AuthConfig authConfig;
    private final ObjectMapper objectMapper;

    @Override
    public UserDTO authUserByHeader(String authorizationHeaderValue) {
        try {
            var body = createBody(authorizationHeaderValue);

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(authConfig.authenticationUrl()))
                    .header("Content-Type", "application/json")
                    .header("client-secret", authConfig.clientSecret())
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status >= 400 && status < 500) {
                return null;
            }

            if (status >= 500) {
                throw new DownstreamServiceUnavailableException("Authentication service unavailable");
            }

            return objectMapper.readValue(response.body(), UserDTO.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new DownstreamServiceUnavailableException("Authentication service unavailable");
        }
    }

    private String createBody(String authorizationHeaderValue) throws JsonProcessingException {
        var requestBody = new AuthenticateUserRequestBody(authorizationHeaderValue);
        return objectMapper.writeValueAsString(requestBody);
    }
}
