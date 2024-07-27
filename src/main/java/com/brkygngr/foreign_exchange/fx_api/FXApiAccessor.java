package com.brkygngr.foreign_exchange.fx_api;

import com.brkygngr.foreign_exchange.exception.InvalidCurrencyException;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiExchangeRateResponse;
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

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class FXApiAccessor {

    @Value("${app.fx.apiUrl}")
    private final String apiUrl;

    @Value("${app.fx.apiKey}")
    private final String apiKey;

    private final RestTemplate restTemplate;

    public Optional<FXApiExchangeRateResponse> getLatestExchangeRateBetween(final String sourceCurrency, final String targetCurrency) throws InvalidCurrencyException {
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

            throw InvalidCurrencyException.withDefaultMessage();
        }

        return Optional.ofNullable(responseEntity.getBody());
    }
}
