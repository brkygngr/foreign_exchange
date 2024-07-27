package com.brkygngr.foreign_exchange.fx_api.dto;

import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;

import java.math.BigDecimal;

public record FXApiCurrencyRate(String code, BigDecimal value) {

    public ExchangeRate toExchangeRate(final String sourceCurrency) {
        return new ExchangeRate(
                sourceCurrency,
                code,
                value
        );
    }
}
