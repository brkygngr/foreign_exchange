package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exception.ResponseNullException;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.fx_api.FXApiAccessor;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiCurrencyRate;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FXApiExchangeRateService implements ExchangeRateService {

    private final FXApiAccessor fxApiAccessor;

    @Cacheable(value = "exchange_rate", key = "#sourceCurrency + '_to_' + #targetCurrency")
    public ExchangeRate getLatestExchangeRateBetween(final String sourceCurrency, final String targetCurrency) {
        FXApiExchangeRateResponse fxApiExchangeRateResponse = fxApiAccessor
                .getLatestExchangeRateBetween(sourceCurrency,
                                              targetCurrency)
                .orElseThrow(
                        () -> new ResponseNullException(
                                String.format(
                                        "For source %s and target %s currency API responded with null!",
                                        sourceCurrency,
                                        targetCurrency))
                );

        FXApiCurrencyRate currencyRate = fxApiExchangeRateResponse.data().get(targetCurrency);

        return currencyRate.toExchangeRate(sourceCurrency);
    }

    @CacheEvict(value = "exchange_rate", allEntries = true)
    @Scheduled(cron = "${app.cache.exchange-rate.ttl}")
    public void emptyExchangeRateCacheEveryday() {
        log.info("Clearing exchange rate cache");
    }
}
