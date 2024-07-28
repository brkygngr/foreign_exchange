package com.brkygngr.foreign_exchange.exception;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, AppError[] errors) {

}
