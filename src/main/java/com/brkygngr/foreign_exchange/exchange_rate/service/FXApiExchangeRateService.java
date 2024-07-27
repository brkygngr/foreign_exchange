package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.exchange_rate.dto.external.FXApiExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FXApiExchangeRateService implements ExchangeRateService {

    @Value("${app.fx.apiUrl]")
    private final String apiUrl;

    @Value("${app.fx.apiKey}")
    private final String apiKey;

    private final RestTemplate restTemplate;

    public ExchangeRate getLatestExchangeRateBetween(final String sourceCurrency, final String targetCurrency) {
        restTemplate.exchange(apiUrl, HttpMethod.GET, null, FXApiExchangeRateResponse.class);

        return null;
    }
}
