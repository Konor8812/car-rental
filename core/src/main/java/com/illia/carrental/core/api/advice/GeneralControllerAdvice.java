package com.illia.carrental.core.api.advice;

import com.illia.carrental.core.commons.exception.CarReservationException;
import com.illia.carrental.core.model.dto.response.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralControllerAdvice {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CarReservationException.class)
    public ExceptionResponseDTO handleInvalidAuthenticationException(CarReservationException ex) {
        System.out.println("Car reservation exception: " + ex.getMessage());
        return new ExceptionResponseDTO(400, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponseDTO handleGeneralException(Exception ex) {
        System.err.println("Internal Server Error: " + ex.getMessage());
        ex.printStackTrace();
        return new ExceptionResponseDTO(500,
                "Oops, an unexpected error occurred. Please contact the administrator.");
    }
}
