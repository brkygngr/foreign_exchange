package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exchange_rate.dto.external.FXApiExchangeRateResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FXApiExchangeRateServiceTest {

    private static final String API_URL = "test.url.com";

    private static final String API_KEY = "test.key";

    private AutoCloseable autoCloseable;

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
}
