package com.illia.carrental.auth.api._public;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.illia.carrental.auth.dto.request.LoginUserRequest;
import com.illia.carrental.auth.dto.request.RegisterUserRequest;
import com.illia.carrental.auth.dto.response.AuthenticationResponse;
import com.illia.carrental.auth.service.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorizerController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorizerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /v1/auth/login should return 200 and AuthenticationResponse")
    void login_shouldReturnOkResponse() throws Exception {
        // given
        var loginRequest = new LoginUserRequest("user@test.com", "password123");
        var response = new AuthenticationResponse("jwt-token-123");

        Mockito.when(authenticationService.login(any(LoginUserRequest.class)))
                .thenReturn(response);

        // when / then
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("jwt-token-123"));
    }

    @Test
    @DisplayName("POST /v1/auth/register should return 200 and AuthenticationResponse")
    void register_shouldReturnOkResponse() throws Exception {
        // given
        var registerRequest = new RegisterUserRequest("user@test.com", "password123", "John");
        var response = new AuthenticationResponse("new-jwt-token");

        Mockito.when(authenticationService.createUser(any(RegisterUserRequest.class)))
                .thenReturn(response);

        // when / then
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("new-jwt-token"));
    }

    @Test
    @DisplayName("POST /v1/auth/logout should call logout and return 204")
    void logout_shouldReturnNoContent() throws Exception {
        // given
        String token = "Bearer some-token";

        // no return, just verify it’s called
        Mockito.doNothing().when(authenticationService).logout(eq(token));

        // when / then
        mockMvc.perform(post("/v1/auth/logout")
                        .header("Authorization", token))
                .andExpect(status().isNoContent());

        Mockito.verify(authenticationService).logout(eq(token));
    }
}
