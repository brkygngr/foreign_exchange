package com.brkygngr.foreign_exchange.exchange_rate.dto;

import java.math.BigDecimal;

public record ExchangeRate(String sourceCurrency, String targetCurrency, BigDecimal value) {
}
