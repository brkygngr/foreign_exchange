package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ExchangeConversionRequest(@NotNull(message = "Amount is required!")
                                        @Positive(message = "Amount must be positive!")
                                        BigDecimal amount,
                                        String sourceCurrency,
                                        String targetCurrency) {

}
