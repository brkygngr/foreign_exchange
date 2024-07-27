package com.brkygngr.foreign_exchange.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class InvalidCurrencyException extends RuntimeException {

    public InvalidCurrencyException(final String message) {
        super(message);
    }
}
