package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ExchangeConversionRequest(@NotNull(message = "Amount is required!")
                                        @Positive(message = "Amount must be positive!")
                                        BigDecimal amount,
                                        @NotBlank(message = "Source currency is required!")
                                        @Size(message = "Source currency must be a three letter currency code!",
                                              min = 3,
                                              max = 3)
                                        String sourceCurrency,
                                        @NotBlank(message = "Target currency is required!")
                                        String targetCurrency) {

}
