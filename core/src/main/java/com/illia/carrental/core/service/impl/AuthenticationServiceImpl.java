package com.illia.carrental.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.illia.carrental.core.commons.exception.AuthException;
import com.illia.carrental.core.config.AuthConfig;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.model.dto.request.AuthenticateUserRequestBody;
import com.illia.carrental.core.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        System.out.println("Sending auth request");
        try {
            var body = createBody(authorizationHeaderValue);

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(authConfig.authenticationUrl()))
                    .header("Content-Type", "application/json")
                    .header("client-secret", authConfig.clientSecret())
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response: " + response.body());
            return objectMapper.readValue(response.body(), UserDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthException();
        }

    }

    private String createBody(String authorizationHeaderValue) throws JsonProcessingException {
        var requestBody = new AuthenticateUserRequestBody(authorizationHeaderValue);
        return objectMapper.writeValueAsString(requestBody);
    }
}
