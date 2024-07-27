package com.brkygngr.foreign_exchange.exchange_rate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExchangeRateQuery(
        @NotBlank(message = "Source currency is required!")
        @Size(message = "Source currency must be a three letter source currency code!", min = 3, max = 3)
        String sourceCurrency,
        @NotBlank(message = "Target currency is required!")
        @Size(message = "Target currency must be a three letter source currency code!", min = 3, max = 3)
        String targetCurrency) {
}
