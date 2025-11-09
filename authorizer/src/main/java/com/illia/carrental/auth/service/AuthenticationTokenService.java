package com.illia.carrental.auth.service;

import com.illia.carrental.auth.data.entity.AuthenticationToken;
import com.illia.carrental.auth.data.entity.User;

public interface AuthenticationTokenService {

    AuthenticationToken createAuthorizationToken(User username);

    void invalidateToken(String authorizationToken);

    String extractEmailFromHeader(String authorizationHeaderValue);
}
