package com.brkygngr.foreign_exchange.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.brkygngr.foreign_exchange.exception.ErrorCodes.CURRENCY_CODE_INVALID_ERROR_CODE;

@Getter
@EqualsAndHashCode(callSuper = true)
public class InvalidCurrencyException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "One or more currency codes are invalid!";

    private final String errorCode = CURRENCY_CODE_INVALID_ERROR_CODE;

    public InvalidCurrencyException(final String message) {
        super(message);
    }

    public static InvalidCurrencyException withDefaultMessage() {
        return new InvalidCurrencyException(DEFAULT_MESSAGE);
    }
}
