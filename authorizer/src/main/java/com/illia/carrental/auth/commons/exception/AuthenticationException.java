package com.illia.carrental.auth.commons.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Authentication failed");
    }
}
