package com.illia.carrental.auth.service.impl;

import com.illia.carrental.auth.commons.exception.AuthenticationException;
import com.illia.carrental.auth.commons.mapper.UserMapper;
import com.illia.carrental.auth.data.entity.AuthenticationToken;
import com.illia.carrental.auth.data.entity.User;
import com.illia.carrental.auth.dto.UserDTO;
import com.illia.carrental.auth.dto.request.AuthenticateUserRequest;
import com.illia.carrental.auth.dto.request.LoginUserRequest;
import com.illia.carrental.auth.dto.request.RegisterUserRequest;
import com.illia.carrental.auth.dto.response.AuthenticationResponse;
import com.illia.carrental.auth.service.AuthenticationTokenService;
import com.illia.carrental.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationTokenService authenticationTokenService;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("user@test.com");
        user.setPassword("encodedPass");
    }

    // --- LOGIN ---

    @Test
    @DisplayName("login() should return token when credentials valid")
    void login_shouldReturnToken_whenValidCredentials() {
        // given
        var loginRequest = new LoginUserRequest("user@test.com", "plainPass");
        var authToken = new AuthenticationToken(1L, "jwt-token", "seed", 1L);

        when(userService.findUserByEmail("user@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPass", "encodedPass"))
                .thenReturn(true);
        when(authenticationTokenService.createAuthorizationToken(user))
                .thenReturn(authToken);

        // when
        AuthenticationResponse response = authenticationService.login(loginRequest);

        // then
        assertThat(response.token()).isEqualTo("jwt-token");
        verify(authenticationTokenService).createAuthorizationToken(user);
    }

    @Test
    @DisplayName("login() should throw AuthenticationException when user not found")
    void login_shouldThrow_whenUserNotFound() {
        // given
        var loginRequest = new LoginUserRequest("missing@test.com", "plainPass");
        when(userService.findUserByEmail("missing@test.com"))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("login() should throw AuthenticationException when password invalid")
    void login_shouldThrow_whenPasswordInvalid() {
        // given
        var loginRequest = new LoginUserRequest("user@test.com", "wrongPass");
        when(userService.findUserByEmail("user@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedPass"))
                .thenReturn(false);

        // then
        assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(AuthenticationException.class);
        verify(authenticationTokenService, never()).createAuthorizationToken(any());
    }

    // --- REGISTER ---

    @Test
    @DisplayName("createUser() should encode password, save user, and return token")
    void createUser_shouldReturnToken() {
        // given
        var registerRequest = new RegisterUserRequest("user@test.com", "plainPass", "Doe");
        var newUser = new User();
        newUser.setEmail("user@test.com");
        newUser.setPassword("encodedPass");
        var authToken = new AuthenticationToken(1L, "new-token", "newSeed", 1L);

        when(userMapper.toUser(registerRequest)).thenReturn(newUser);
        when(passwordEncoder.encode("encodedPass")).thenReturn("encodedPass"); // mock password encoding
        when(userService.saveUser(newUser)).thenReturn(newUser);
        when(authenticationTokenService.createAuthorizationToken(newUser)).thenReturn(authToken);

        // when
        var response = authenticationService.createUser(registerRequest);

        // then
        assertThat(response.token()).isEqualTo("new-token");
        verify(passwordEncoder).encode(anyString());
        verify(userService).saveUser(newUser);
        verify(authenticationTokenService).createAuthorizationToken(newUser);
    }

    // --- LOGOUT ---

    @Test
    @DisplayName("logout() should invalidate token")
    void logout_shouldCallInvalidateToken() {
        // given
        String token = "Bearer abc";
        doNothing().when(authenticationTokenService).invalidateToken(token);

        // when
        authenticationService.logout(token);

        // then
        verify(authenticationTokenService).invalidateToken(token);
    }

    // --- AUTHENTICATE ---

    @Test
    @DisplayName("authenticate() should return UserDTO when token valid")
    void authenticate_shouldReturnUserDto() {
        // given
        var request = new AuthenticateUserRequest("Bearer token-123");
        when(authenticationTokenService.extractEmailFromHeader("Bearer token-123"))
                .thenReturn("user@test.com");
        when(userService.findUserByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        var userDTO = new UserDTO(1L, "user@test.com", "John");
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // when
        var result = authenticationService.authenticate(request);

        // then
        assertThat(result).isEqualTo(userDTO);
    }

    @Test
    @DisplayName("authenticate() should throw when user not found")
    void authenticate_shouldThrow_whenUserMissing() {
        // given
        var request = new AuthenticateUserRequest("Bearer token-123");
        when(authenticationTokenService.extractEmailFromHeader("Bearer token-123"))
                .thenReturn("missing@test.com");
        when(userService.findUserByEmail("missing@test.com"))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> authenticationService.authenticate(request))
                .isInstanceOf(AuthenticationException.class);
    }
}
