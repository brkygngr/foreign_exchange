package com.brkygngr.foreign_exchange.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                         description = "Request validation errors",
                         content = @Content(
                                 mediaType = "application/json",
                                 schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(MethodArgumentNotValidException exception) {
        String[] errors = exception.getAllErrors()
                                   .stream()
                                   .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                   .toArray(String[]::new);

        return ResponseEntity.badRequest().body(new ErrorResponse(Instant.now(), errors));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "422",
                         description = "Currency invalid errors",
                         content = @Content(
                                 mediaType = "application/json",
                                 schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ExceptionHandler({InvalidCurrencyException.class, ResponseNullException.class})
    public ResponseEntity<ErrorResponse> handleUnprocessableEntity(InvalidCurrencyException exception) {
        String[] errors = new String[]{
                exception.getMessage()
        };

        return ResponseEntity.unprocessableEntity().body(new ErrorResponse(Instant.now(), errors));
    }
}
