package com.illia.carrental.auth.dto;

public record UserDTO(Long id,
                      String email,
                      String username,
                      Long accountId) {
}