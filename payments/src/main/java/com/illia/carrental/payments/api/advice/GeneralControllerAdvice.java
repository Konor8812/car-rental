package com.illia.carrental.payments.api.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralControllerAdvice {


//    @ExceptionHandler(exception = AuthenticationException.class)
//    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
//        e.printStackTrace();
//        return ResponseEntity.status(401).body(e.getMessage());
//
//    }

    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
