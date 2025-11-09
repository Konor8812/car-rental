package com.illia.carrental.auth.commons.exception;

public class ClientSecretAuthException extends RuntimeException {
    public ClientSecretAuthException() {
        super("Client Secret Authentication Failed");
    }
}
