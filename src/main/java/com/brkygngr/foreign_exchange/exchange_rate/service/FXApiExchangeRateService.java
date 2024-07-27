package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exception.ResponseNullException;
import com.brkygngr.foreign_exchange.exception.InvalidCurrencyException;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.fx_api.FXApiAccessor;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiCurrencyRate;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class FXApiExchangeRateService implements ExchangeRateService {

    private final FXApiAccessor fxApiAccessor;

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
}
