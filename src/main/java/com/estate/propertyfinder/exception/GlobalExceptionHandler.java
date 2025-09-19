package com.estate.propertyfinder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handelAll(Exception ex, WebRequest request){
        return new ResponseEntity<>(new ErrorResponse(LocalDateTime.now(),ex.getMessage(),request.getDescription(false)),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
