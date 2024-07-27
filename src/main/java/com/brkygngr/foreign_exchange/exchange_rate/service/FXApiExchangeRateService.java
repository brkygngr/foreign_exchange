package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exception.ResponseNullException;
import com.brkygngr.foreign_exchange.exception.InvalidCurrencyException;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.exchange_rate.dto.external.FXApiCurrencyRate;
import com.brkygngr.foreign_exchange.exchange_rate.dto.external.FXApiExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class FXApiExchangeRateService implements ExchangeRateService {

    @Value("${app.fx.apiUrl}")
    private final String apiUrl;

    @Value("${app.fx.apiKey}")
    private final String apiKey;

    private final RestTemplate restTemplate;

    public ExchangeRate getLatestExchangeRateBetween(final String sourceCurrency, final String targetCurrency) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "/latest")
                                         .queryParam("base_currency", sourceCurrency)
                                         .queryParam("currencies", targetCurrency)
                                         .build()
                                         .toUriString();

        log.info("getLatestExchangeRateBetween request url: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", apiKey);

        ResponseEntity<FXApiExchangeRateResponse> responseEntity;

        try {
            responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<Void>(headers),
                    FXApiExchangeRateResponse.class
            );
        } catch (HttpClientErrorException.UnprocessableEntity exception) {
            log.error(String.format("For source %s and target %s currency API responded with unprocessable!",
                                    sourceCurrency,
                                    targetCurrency), exception);

            throw new InvalidCurrencyException("One or more currency codes are invalid!");
        }

        FXApiExchangeRateResponse fxApiExchangeRateResponse = responseEntity.getBody();

        if (fxApiExchangeRateResponse == null) {
            throw new ResponseNullException(String.format("For source %s and target %s currency API responded with null!",
                                                          sourceCurrency,
                                                          targetCurrency));
        }

        FXApiCurrencyRate currencyRate = fxApiExchangeRateResponse.data().get(targetCurrency);

        return currencyRate.toExchangeRate(sourceCurrency);
    }
}
