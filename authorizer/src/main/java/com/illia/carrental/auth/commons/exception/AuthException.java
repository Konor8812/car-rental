package com.illia.carrental.auth.commons.exception;

public class AuthException extends RuntimeException {
    public AuthException() {
        super("Authentication failed");
    }

    public AuthException(String message) {
        super(message);
    }
}
