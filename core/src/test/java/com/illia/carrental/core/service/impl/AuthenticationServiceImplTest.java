package com.illia.carrental.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.illia.carrental.core.config.AuthConfig;
import com.illia.carrental.core.model.dto.UserDTO;
import com.illia.carrental.core.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    private HttpClient httpClient;
    private AuthConfig authConfig;
    private ObjectMapper objectMapper;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        httpClient = mock(HttpClient.class);
        authConfig = mock(AuthConfig.class);
        objectMapper = new ObjectMapper();

        when(authConfig.authenticationUrl()).thenReturn("http://auth.service/auth");
        when(authConfig.clientSecret()).thenReturn("secret123");

        authenticationService =
                new AuthenticationServiceImpl(httpClient, authConfig, objectMapper);
    }

    @Test
    void testAuthUserByHeader_success() throws Exception {
        // Given
        var userJson = """
                {"id": 1, "email": "john@test.com", "username": "John"}
                """;

        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.body()).thenReturn(userJson);

        when(httpClient.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)
        )).thenReturn(httpResponse);

        // When
        UserDTO user = authenticationService.authUserByHeader("Bearer abc");

        // Then
        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.email()).isEqualTo("john@test.com");

        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).send(captor.capture(), any());
        HttpRequest sent = captor.getValue();

        assertThat(sent.uri()).isEqualTo(URI.create("http://auth.service/auth"));
        assertThat(sent.headers().firstValue("client-secret").get()).isEqualTo("secret123");
    }

    @Test
    void testAuthUserByHeader_fails_SetsNoUserInContextHolder() throws Exception {
        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.statusCode()).thenReturn(401);

        when(httpClient.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)
        )).thenReturn(httpResponse);

        assertThat(authenticationService.authUserByHeader("Bearer abc"))
                .isNull();
    }
}
