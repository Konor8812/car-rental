package com.illia.carrental.auth.api.advice;

import com.illia.carrental.auth.commons.exception.AuthException;
import com.illia.carrental.auth.commons.exception.ClientSecretAuthException;
import com.illia.carrental.auth.commons.exception.InvalidTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralControllerAdvice {


    @ExceptionHandler(exception = AuthException.class)
    public ResponseEntity<String> handleAuthException(AuthException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(exception = InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(exception = ClientSecretAuthException.class)
    public ResponseEntity<String> handleClientSecretAuthException(ClientSecretAuthException e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }

    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
