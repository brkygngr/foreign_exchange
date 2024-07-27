package com.brkygngr.foreign_exchange.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class InvalidCurrencyException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "One or more currency codes are invalid!";

    public InvalidCurrencyException(final String message) {
        super(message);
    }

    public static InvalidCurrencyException withDefaultMessage() {
        return new InvalidCurrencyException(DEFAULT_MESSAGE);
    }
}
