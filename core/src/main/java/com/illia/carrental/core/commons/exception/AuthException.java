package com.illia.carrental.core.commons.exception;

public class AuthException extends RuntimeException {
    public AuthException() {
        super("Authentication/authorization failed failed");
    }
}
