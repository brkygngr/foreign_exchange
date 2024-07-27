package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;

import java.math.BigDecimal;

public interface ExchangeConversionService {
    ExchangeConversion convertAmount(final BigDecimal amount, final String sourceCurrency, final String targetCurrency);
}
