package com.illia.carrental.auth.service.impl;

import com.illia.carrental.auth.commons.exception.InvalidTokenException;
import com.illia.carrental.auth.data.entity.AuthenticationToken;
import com.illia.carrental.auth.data.entity.User;
import com.illia.carrental.auth.data.repository.AuthenticationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationTokenServiceImplTest {

    @Mock
    private AuthenticationTokenRepository authenticationTokenRepository;

    @InjectMocks
    private AuthenticationTokenServiceImpl authenticationTokenService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1L)
                .email("user@test.com")
                .build();
    }

    // --- createAuthorizationToken ---

    @Test
    @DisplayName("createAuthorizationToken() should save token with user id and seed")
    void createAuthorizationToken_shouldSaveToken() {
        // given
        ArgumentCaptor<AuthenticationToken> captor = ArgumentCaptor.forClass(AuthenticationToken.class);
        var savedToken = AuthenticationToken.builder()
                .id(10L)
                .token("Bearer user@test.com.123")
                .seed("123")
                .user_id(1L)
                .build();

        when(authenticationTokenRepository.save(any(AuthenticationToken.class))).thenReturn(savedToken);

        // when
        var result = authenticationTokenService.createAuthorizationToken(user);

        // then
        verify(authenticationTokenRepository).save(captor.capture());
        var captured = captor.getValue();

        assertThat(captured.getUser_id()).isEqualTo(1L);
        assertThat(captured.getToken()).startsWith("Bearer user@test.com.");
        assertThat(UUID.fromString(captured.getSeed())).isInstanceOf(UUID.class); // valid UUID format

        assertThat(result).isEqualTo(savedToken);
    }

    // --- invalidateToken ---

    @Test
    @DisplayName("invalidateToken() should delete by token and seed when valid")
    void invalidateToken_shouldDeleteByTokenAndSeed() {
        // given
        String email = "user@test.com";
        String seed = "abc-123";
        String header = "Bearer " + email + "." + seed;

        // when
        authenticationTokenService.invalidateToken(header);

        // then
        verify(authenticationTokenRepository).deleteByTokenAndSeed(email + "." + seed, "." + seed);
    }

    @Test
    @DisplayName("invalidateToken() should throw InvalidTokenException when header invalid")
    void invalidateToken_shouldThrowOnInvalidHeader() {
        assertThatThrownBy(() -> authenticationTokenService.invalidateToken("InvalidHeader"))
                .isInstanceOf(InvalidTokenException.class);
        verify(authenticationTokenRepository, never()).deleteByTokenAndSeed(any(), any());
    }

    // --- extractEmailFromHeader ---

    @Test
    @DisplayName("extractEmailFromHeader() should return correct email when header valid")
    void extractEmailFromHeader_shouldReturnEmail() {
        // given
        String email = "user@test.com";
        String seed = "abc-123";
        String header = "Bearer " + email + "." + seed;

        // when
        String result = authenticationTokenService.extractEmailFromHeader(header);

        // then
        assertThat(result).isEqualTo(email);
    }

    @Test
    @DisplayName("extractEmailFromHeader() should throw InvalidTokenException when header invalid")
    void extractEmailFromHeader_shouldThrowOnInvalidHeader() {
        assertThatThrownBy(() -> authenticationTokenService.extractEmailFromHeader("Bearer invalid"))
                .isInstanceOf(InvalidTokenException.class);
    }

    // --- internal helpers indirectly tested above ---
}
