package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import com.brkygngr.foreign_exchange.validation.conversion.ConversionIDOrDateNotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.brkygngr.foreign_exchange.exception.ErrorCodes.CONVERSION_HISTORY_ID_AND_DATE_NULL_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.CONVERSION_HISTORY_ID_OR_DATE_REQUIRED;

public record ConversionHistoryQuery(Optional<UUID> transactionID,
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                     Optional<LocalDate> transactionDate) {

    @ConversionIDOrDateNotNull(message = CONVERSION_HISTORY_ID_OR_DATE_REQUIRED, errorCode = CONVERSION_HISTORY_ID_AND_DATE_NULL_ERROR_CODE)
    private boolean isIDOrDateNotNull() {
        return transactionID.isPresent() || transactionDate.isPresent();
    }
}
