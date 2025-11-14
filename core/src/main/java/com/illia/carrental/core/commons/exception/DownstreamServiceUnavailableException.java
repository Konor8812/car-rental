package com.illia.carrental.core.commons.exception;

public class DownstreamServiceUnavailableException extends RuntimeException {
    public DownstreamServiceUnavailableException(String message) {
        super(message);
    }
}
