package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExchangeConversionRequest(@NotNull(message = "Amount is required!") BigDecimal amount,
                                        String sourceCurrency,
                                        String targetCurrency) {

}
