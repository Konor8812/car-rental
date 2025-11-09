package com.illia.carrental.auth.service.impl;

import com.illia.carrental.auth.commons.exception.AuthenticationException;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationTokenService authenticationTokenService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public AuthenticationResponse login(LoginUserRequest loginUserRequest) {
        var user = userService.findUserByEmailAndPassword(
                loginUserRequest.email(),
                loginUserRequest.password());
        var token = user.map(authenticationTokenService::createAuthorizationToken)
                .map(AuthenticationToken::getToken)
                .orElseThrow(AuthenticationException::new);
        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse createUser(RegisterUserRequest registerUserRequest) {
        var user = userMapper.toUser(registerUserRequest);
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
        var user = userService.findUserByEmail(email);
        return userMapper.toDTO(user);
    }
}
