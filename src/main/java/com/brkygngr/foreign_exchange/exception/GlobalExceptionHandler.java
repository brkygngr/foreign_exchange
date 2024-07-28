package com.brkygngr.foreign_exchange.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
        List<AppError> errors = new ArrayList<>();

        exception.getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            Object foundErrorCode = error.unwrap(ConstraintViolation.class)
                                    .getConstraintDescriptor()
                                    .getAttributes()
                                    .get("errorCode");

            String code = foundErrorCode == null ? "" : foundErrorCode.toString();

            errors.add(new AppError(message, code));
        });


        return ResponseEntity.badRequest().body(new ErrorResponse(Instant.now(), errors.toArray(AppError[]::new)));
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
        AppError[] errors = new AppError[]{
                new AppError(exception.getMessage(), exception.getErrorCode())
        };

        return ResponseEntity.unprocessableEntity().body(new ErrorResponse(Instant.now(), errors));
    }
}
