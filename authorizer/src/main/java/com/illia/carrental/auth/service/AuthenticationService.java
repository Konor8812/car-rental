package com.illia.carrental.auth.service;


import com.illia.carrental.auth.dto.UserDTO;
import com.illia.carrental.auth.dto.request.AuthenticateUserRequest;
import com.illia.carrental.auth.dto.request.LoginUserRequest;
import com.illia.carrental.auth.dto.request.RegisterUserRequest;
import com.illia.carrental.auth.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse login(LoginUserRequest loginUserRequest);

    AuthenticationResponse createUser(RegisterUserRequest registerUserRequest);

    void logout(String authorizationToken);

    UserDTO authenticate(AuthenticateUserRequest authenticateUserRequest);
}
