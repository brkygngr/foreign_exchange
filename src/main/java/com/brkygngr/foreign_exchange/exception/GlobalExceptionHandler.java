package com.brkygngr.foreign_exchange.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(MethodArgumentNotValidException exception) {
        String[] errors = exception.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);

        return ResponseEntity.badRequest().body(new ErrorResponse(Instant.now(), errors));
    }

    @ExceptionHandler({InvalidCurrencyException.class, ResponseNullException.class})
    public ResponseEntity<ErrorResponse> handleUnprocessableEntity(InvalidCurrencyException exception) {
        String[] errors = new String[] {
                exception.getMessage()
        };

        return ResponseEntity.unprocessableEntity().body(new ErrorResponse(Instant.now(), errors));
    }
}
