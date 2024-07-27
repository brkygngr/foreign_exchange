package com.brkygngr.foreign_exchange.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
public class FXApiException extends RuntimeException {

    public FXApiException(final String message) {
        super(message);
    }
}
