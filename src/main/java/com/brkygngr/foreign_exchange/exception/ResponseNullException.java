package com.brkygngr.foreign_exchange.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ResponseNullException extends RuntimeException {

    public ResponseNullException(final String message) {
        super(message);
    }
}
