package com.illia.carrental.auth.service;

import com.illia.carrental.auth.data.entity.User;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    Optional<User> findUserByEmail(String email);
}
