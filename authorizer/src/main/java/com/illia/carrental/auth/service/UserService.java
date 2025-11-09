package com.illia.carrental.auth.service;

import com.illia.carrental.auth.data.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByEmailAndPassword(String email, String password);

    User saveUser(User user);

    User findUserByEmail(String email);
}
