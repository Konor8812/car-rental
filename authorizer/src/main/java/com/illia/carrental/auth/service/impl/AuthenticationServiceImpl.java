package com.illia.carrental.auth.service.impl;

import com.illia.carrental.auth.commons.exception.AuthException;
import com.illia.carrental.auth.commons.mapper.UserMapper;
import com.illia.carrental.auth.data.entity.AuthenticationToken;
import com.illia.carrental.auth.dto.UserDTO;
import com.illia.carrental.auth.dto.request.AuthenticateUserRequest;
import com.illia.carrental.auth.dto.request.LoginUserRequest;
import com.illia.carrental.auth.dto.request.RegisterUserRequest;
import com.illia.carrental.auth.dto.response.AuthenticationResponse;
import com.illia.carrental.auth.service.AuthenticationService;
import com.illia.carrental.auth.service.AuthenticationTokenService;
import com.illia.carrental.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationTokenService authenticationTokenService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse login(LoginUserRequest loginUserRequest) {
        var token = userService.findUserByEmail(loginUserRequest.email())
                .stream()
                .filter(u -> passwordEncoder.matches(loginUserRequest.password(), u.getPassword()))
                .map(authenticationTokenService::createAuthorizationToken)
                .map(AuthenticationToken::getToken)
                .findFirst();
        return new AuthenticationResponse(token
                .orElseThrow(AuthException::new));
    }

    @Override
    public AuthenticationResponse createUser(RegisterUserRequest registerUserRequest) {
        var user = userMapper.toUser(registerUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userService.saveUser(user);
        var token = authenticationTokenService.createAuthorizationToken(user).getToken();
        return new AuthenticationResponse(token);
    }

    @Override
    public void logout(String authorizationToken) {
        authenticationTokenService.invalidateToken(authorizationToken);
    }

    @Override
    public UserDTO authenticate(AuthenticateUserRequest authenticateUserRequest) {
        var email = authenticationTokenService.extractEmailFromHeader(authenticateUserRequest.authorizationHeaderValue());
        var user = userService.findUserByEmail(email)
                .orElseThrow(AuthException::new);
        return userMapper.toDTO(user);
    }
}
