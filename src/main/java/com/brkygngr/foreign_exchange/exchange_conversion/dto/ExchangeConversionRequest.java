package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import com.brkygngr.foreign_exchange.exception.ValidationErrorMessages;

public record ExchangeConversionRequest(@NotNull(message = ValidationErrorMessages.CONVERSION_AMOUNT_REQUIRED)
                                        @Positive(message = ValidationErrorMessages.CONVERSION_AMOUNT_POSITIVE)
                                        BigDecimal amount,
                                        @NotBlank(message = ValidationErrorMessages.SOURCE_CURRENCY_REQUIRED)
                                        @Size(min = 3,
                                              max = 3,
                                              message = ValidationErrorMessages.SOURCE_CURRENCY_LENGTH)
                                        String sourceCurrency,
                                        @NotBlank(message = ValidationErrorMessages.TARGET_CURRENCY_REQUIRED)
                                        @Size(min = 3,
                                              max = 3,
                                              message = ValidationErrorMessages.TARGET_CURRENCY_LENGTH
                                        )
                                        String targetCurrency) {

}
