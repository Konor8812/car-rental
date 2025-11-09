package com.illia.carrental.auth.dto.request;

public record RegisterUserRequest(String email,
                                  String password,
                                  String username) {
}
