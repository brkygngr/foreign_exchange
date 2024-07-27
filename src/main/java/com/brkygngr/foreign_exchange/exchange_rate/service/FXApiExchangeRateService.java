package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exception.FXApiException;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.exchange_rate.dto.external.FXApiExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class FXApiExchangeRateService implements ExchangeRateService {

    @Value("${app.fx.apiUrl]")
    private final String apiUrl;

    @Value("${app.fx.apiKey}")
    private final String apiKey;

    private final RestTemplate restTemplate;

    public ExchangeRate getLatestExchangeRateBetween(final String sourceCurrency, final String targetCurrency) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", apiKey);

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + "/latest")
                .queryParam("base_currency", sourceCurrency)
                .queryParam("currencies", targetCurrency)
                .build()
                .toUriString();

        ResponseEntity<FXApiExchangeRateResponse> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<Void>(headers),
                FXApiExchangeRateResponse.class
        );

        FXApiExchangeRateResponse fxApiExchangeRateResponse = responseEntity.getBody();

        if (fxApiExchangeRateResponse == null) {
            throw new FXApiException(String.format("For source %s and target %s currency API responded with null!", sourceCurrency, targetCurrency));
        }

        return null;
    }
}
