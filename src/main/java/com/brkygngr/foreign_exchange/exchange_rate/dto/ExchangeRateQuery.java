package com.brkygngr.foreign_exchange.exchange_rate.dto;

import com.brkygngr.foreign_exchange.exception.ValidationErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExchangeRateQuery(
        @NotBlank(message = ValidationErrorMessages.SOURCE_CURRENCY_REQUIRED)
        @Size(min = 3, max = 3, message = ValidationErrorMessages.SOURCE_CURRENCY_LENGTH)
        String sourceCurrency,
        @NotBlank(message = ValidationErrorMessages.TARGET_CURRENCY_REQUIRED)
        @Size(min = 3, max = 3, message = ValidationErrorMessages.TARGET_CURRENCY_LENGTH)
        String targetCurrency) {
}
