package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import com.brkygngr.foreign_exchange.exception.ValidationErrorMessages;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record ConversionHistoryQuery(UUID transactionID,
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                     LocalDate transactionDate) {

    @AssertTrue(message = ValidationErrorMessages.CONVERSION_HISTORY_REQUIRED)
    private boolean isIDOrDateNotNull() {
        return transactionID != null || transactionDate != null;
    }
}
