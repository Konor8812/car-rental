package com.illia.carrental.core.service;

import com.illia.carrental.core.model.dto.UserDTO;

public interface AuthenticationService {

    UserDTO authUserByHeader(String authorizationHeaderValue);
}
