package com.illia.carrental.auth.commons.exception;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException() {
        super("Invalid token");
    }
}
