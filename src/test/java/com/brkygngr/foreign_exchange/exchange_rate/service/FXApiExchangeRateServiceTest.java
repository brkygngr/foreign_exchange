package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exception.ResponseNullException;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.exchange_rate.dto.external.FXApiCurrencyRate;
import com.brkygngr.foreign_exchange.exchange_rate.dto.external.FXApiExchangeRateResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FXApiExchangeRateServiceTest {

    private static final String API_URL = "http://test.url.com";

    private static final String EXCHANGE_RATE_URL = API_URL + "/latest";

    private static final String API_KEY = "test.key";

    private AutoCloseable autoCloseable;

    @Captor
    private ArgumentCaptor<HttpEntity<Void>> httpEntityArgumentCaptor;

    @Mock
    private RestTemplate restTemplate;

    private FXApiExchangeRateService fxApiExchangeRateService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        fxApiExchangeRateService = new FXApiExchangeRateService(API_URL, API_KEY, restTemplate);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getLatestExchangeRateBetween_callsRestTemplateWithGivenUrl() {
        final String sourceCurrency = "USD";
        final String targetCurrency = "EUR";
        final String url = EXCHANGE_RATE_URL + "?base_currency=" + sourceCurrency + "&currencies=" + targetCurrency;

        final FXApiCurrencyRate currencyRate = new FXApiCurrencyRate(targetCurrency, BigDecimal.valueOf(10));

        final FXApiExchangeRateResponse exchangeRateResponse = new FXApiExchangeRateResponse(
                null,
                Map.of(targetCurrency, currencyRate)
        );

        final ResponseEntity<FXApiExchangeRateResponse> responseEntity = ResponseEntity.ok(exchangeRateResponse);

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), eq(FXApiExchangeRateResponse.class)))
               .thenReturn(responseEntity);

        fxApiExchangeRateService.getLatestExchangeRateBetween(sourceCurrency, targetCurrency);

        verify(restTemplate, times(1)).exchange(eq(url), any(), any(), eq(FXApiExchangeRateResponse.class));
    }

    @Test
    void getLatestExchangeRateBetween_callsRestTemplateWithHeaderApiKey() {
        final String targetCurrency = "EUR";

        final FXApiCurrencyRate currencyRate = new FXApiCurrencyRate(targetCurrency, BigDecimal.valueOf(10));

        final FXApiExchangeRateResponse exchangeRateResponse = new FXApiExchangeRateResponse(null,
                                                                                             Map.of(targetCurrency,
                                                                                                    currencyRate));

        final ResponseEntity<FXApiExchangeRateResponse> responseEntity = ResponseEntity.ok(exchangeRateResponse);

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), eq(FXApiExchangeRateResponse.class)))
               .thenReturn(responseEntity);

        fxApiExchangeRateService.getLatestExchangeRateBetween("USD", targetCurrency);

        verify(restTemplate, times(1)).exchange(anyString(),
                                                any(),
                                                httpEntityArgumentCaptor.capture(),
                                                eq(FXApiExchangeRateResponse.class));

        HttpEntity<Void> httpEntity = httpEntityArgumentCaptor.getValue();

        HttpHeaders headers = httpEntity.getHeaders();

        assertEquals(headers.getFirst("apiKey"), API_KEY);
    }

    @Test
    void getLatestExchangeRateBetween_whenResponseBodyIsNull_thenThrowsAnException() {
        final String sourceCurrency = "USD";
        final String targetCurrency = "EUR";

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), eq(FXApiExchangeRateResponse.class)))
               .thenReturn(ResponseEntity.ok(null));

        Exception exception = assertThrows(ResponseNullException.class,
                                           () -> fxApiExchangeRateService.getLatestExchangeRateBetween(sourceCurrency,
                                                                                                       targetCurrency));

        assertEquals(String.format("For source %s and target %s currency API responded with null!",
                                   sourceCurrency,
                                   targetCurrency), exception.getMessage());
    }

    @Test
    void getLatestExchangeRateBetween_returnsUSDToEURExchangeRate() {
        final String sourceCurrency = "USD";
        final String targetCurrency = "EUR";
        final BigDecimal value = new BigDecimal(10);

        final FXApiCurrencyRate fxApiCurrencyRate = new FXApiCurrencyRate(targetCurrency, value);

        final FXApiExchangeRateResponse exchangeRateResponse = new FXApiExchangeRateResponse(
                null,
                Map.of(targetCurrency, fxApiCurrencyRate)
        );

        final ResponseEntity<FXApiExchangeRateResponse> responseEntity = ResponseEntity.ok(exchangeRateResponse);

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), eq(FXApiExchangeRateResponse.class)))
               .thenReturn(responseEntity);

        final ExchangeRate result = fxApiExchangeRateService.getLatestExchangeRateBetween(sourceCurrency,
                                                                                          targetCurrency);

        assertEquals(sourceCurrency, result.sourceCurrency());
        assertEquals(targetCurrency, result.targetCurrency());
        assertEquals(value, result.value());
    }
}
