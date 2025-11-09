package com.illia.carrental.auth.data.repository;

import com.illia.carrental.auth.data.entity.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, Long> {


    void deleteByTokenAndSeed(String token, String seed);
}
