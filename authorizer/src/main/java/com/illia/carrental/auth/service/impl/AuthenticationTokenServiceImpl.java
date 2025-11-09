package com.illia.carrental.auth.service.impl;

import com.illia.carrental.auth.commons.exception.InvalidTokenException;
import com.illia.carrental.auth.data.entity.AuthenticationToken;
import com.illia.carrental.auth.data.entity.User;
import com.illia.carrental.auth.data.repository.AuthenticationTokenRepository;
import com.illia.carrental.auth.service.AuthenticationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.illia.carrental.auth.commons.constant.AuthorizationConstants.BEARER_PREFIX;

@Service
@RequiredArgsConstructor
public class AuthenticationTokenServiceImpl implements AuthenticationTokenService {

    private final AuthenticationTokenRepository authenticationTokenRepository;

    @Override
    public AuthenticationToken createAuthorizationToken(User user) {
        var seed = UUID.randomUUID();
        var token = BEARER_PREFIX + user.getEmail() + "." + seed;
        var authorizationToken = AuthenticationToken.builder()
                .user_id(user.getId())
                .token(token)
                .seed(seed.toString())
                .build();
        return authenticationTokenRepository.save(authorizationToken);
    }

    @Override
    public void invalidateToken(String authorizationHeaderValue) {
        checkTokenValid(authorizationHeaderValue);

        var token = extractToken(authorizationHeaderValue);
        var seed = extractSeed(token);

        authenticationTokenRepository.deleteByTokenAndSeed(token, seed);
    }

    @Override
    public String extractEmailFromHeader(String authorizationHeaderValue) {
        checkTokenValid(authorizationHeaderValue);

        return extractEmail(authorizationHeaderValue);
    }

    private void checkTokenValid(String authorizationHeaderValue) {
        if (authorizationHeaderValue == null
                || !authorizationHeaderValue.startsWith(BEARER_PREFIX)
                || !authorizationHeaderValue.contains(".")) {
            throw new InvalidTokenException();
        }
    }

    private String extractToken(String authorizationHeaderValue) {
        // format: Bearer token
        return authorizationHeaderValue.substring(BEARER_PREFIX.length());
    }

    private String extractEmail(String authenticationToken) {
        // format: email.seed
        return authenticationToken.substring(0, authenticationToken.lastIndexOf("."));
    }

    private String extractSeed(String authenticationToken) {
        // format: email.seed
        return authenticationToken.substring(authenticationToken.lastIndexOf("."));
    }

}
