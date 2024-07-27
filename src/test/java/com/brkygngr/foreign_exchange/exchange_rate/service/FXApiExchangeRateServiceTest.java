package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exchange_rate.dto.external.FXApiExchangeRateResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FXApiExchangeRateServiceTest {

    private static final String API_URL = "test.url.com";

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
        fxApiExchangeRateService.getLatestExchangeRateBetween("USD", "EUR");

        verify(restTemplate, times(1)).exchange(eq(API_URL), any(), any(), eq(FXApiExchangeRateResponse.class));
    }

    @Test
    void getLatestExchangeRateBetween_callsRestTemplateWithHeaderApiKey() {
        fxApiExchangeRateService.getLatestExchangeRateBetween("USD", "EUR");

        verify(restTemplate, times(1)).exchange(eq(API_URL), any(), httpEntityArgumentCaptor.capture(), eq(FXApiExchangeRateResponse.class));

        HttpEntity<Void> httpEntity = httpEntityArgumentCaptor.getValue();

        HttpHeaders headers = httpEntity.getHeaders();

        assertEquals(headers.getFirst("apiKey"), API_KEY);
    }
}
