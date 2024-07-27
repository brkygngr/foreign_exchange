package com.brkygngr.foreign_exchange.fx_api;

import com.brkygngr.foreign_exchange.fx_api.dto.FXApiCurrencyRate;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiExchangeRateResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FXApiAccessorTest {

    private static final String API_URL = "http://test.url.com";

    private static final String EXCHANGE_RATE_URL = API_URL + "/latest";

    private static final String API_KEY = "test.key";

    private AutoCloseable autoCloseable;

    @Captor
    private ArgumentCaptor<HttpEntity<Void>> httpEntityArgumentCaptor;

    @Mock
    private RestTemplate restTemplate;

    private FXApiAccessor fxApiAccessor;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        fxApiAccessor = new FXApiAccessor(API_URL, API_KEY, restTemplate);
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

        fxApiAccessor.getLatestExchangeRateBetween(sourceCurrency, targetCurrency);

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

        fxApiAccessor.getLatestExchangeRateBetween("USD", targetCurrency);

        verify(restTemplate, times(1)).exchange(anyString(),
                                                any(),
                                                httpEntityArgumentCaptor.capture(),
                                                eq(FXApiExchangeRateResponse.class));

        HttpEntity<Void> httpEntity = httpEntityArgumentCaptor.getValue();

        HttpHeaders headers = httpEntity.getHeaders();

        assertEquals(headers.getFirst("apiKey"), API_KEY);
    }

    @Test
    void getLatestExchangeRateBetween_returnsUSDToEURExchangeRate() {
        final String sourceCurrency = "USD";
        final String targetCurrency = "EUR";
        final BigDecimal value = new BigDecimal(10);

        final FXApiExchangeRateResponse exchangeRateResponse = new FXApiExchangeRateResponse(
                null,
                Map.of(targetCurrency, new FXApiCurrencyRate(targetCurrency, value))
        );

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), eq(FXApiExchangeRateResponse.class)))
               .thenReturn(ResponseEntity
                                   .ok(exchangeRateResponse));

        final FXApiExchangeRateResponse result = fxApiAccessor.getLatestExchangeRateBetween(sourceCurrency,
                                                                                            targetCurrency)
                                                              .orElseThrow();

        final FXApiCurrencyRate currencyRate = result.data().get(targetCurrency);

        assertEquals(value, currencyRate.value());
    }
}