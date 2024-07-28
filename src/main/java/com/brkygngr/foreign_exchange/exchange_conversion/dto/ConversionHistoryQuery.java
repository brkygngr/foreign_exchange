package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import com.brkygngr.foreign_exchange.exception.ValidationErrorMessages;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record ConversionHistoryQuery(Optional<UUID> transactionID,
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                     Optional<LocalDate> transactionDate) {

    @AssertTrue(message = ValidationErrorMessages.CONVERSION_HISTORY_REQUIRED)
    private boolean isIDOrDateNotNull() {
        return transactionID.isPresent() || transactionDate.isPresent();
    }
}
