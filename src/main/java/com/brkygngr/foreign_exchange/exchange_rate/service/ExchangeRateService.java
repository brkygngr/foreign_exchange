package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;

public interface ExchangeRateService {
    ExchangeRate getLatestExchangeRateBetween(final String sourceCurrency, final String targetCurrency);
}
