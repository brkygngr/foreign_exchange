package com.brkygngr.foreign_exchange.exchange_rate.service;

import com.brkygngr.foreign_exchange.exception.InvalidCurrencyException;
import com.brkygngr.foreign_exchange.exception.ResponseNullException;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.fx_api.FXApiAccessor;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiCurrencyRate;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiExchangeRateResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

class FXApiExchangeRateServiceTest {

    private AutoCloseable autoCloseable;

    @Mock
    private FXApiAccessor fxApiAccessor;

    private FXApiExchangeRateService fxApiExchangeRateService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        fxApiExchangeRateService = new FXApiExchangeRateService(fxApiAccessor);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getLatestExchangeRateBetween_whenResponseBodyIsNull_thenThrowsAnResponseNullException() {
        final String sourceCurrency = "USD";
        final String targetCurrency = "EUR";

        Mockito.when(fxApiAccessor.getLatestExchangeRateBetween(anyString(), anyString()))
               .thenReturn(Optional.empty());

        final Exception exception = assertThrows(ResponseNullException.class,
                                           () -> fxApiExchangeRateService.getLatestExchangeRateBetween(sourceCurrency,
                                                                                                       targetCurrency));

        assertEquals(String.format("For source %s and target %s currency API responded with null!",
                                   sourceCurrency,
                                   targetCurrency), exception.getMessage());
    }

    @Test
    void getLatestExchangeRateBetween_whenFXApiThrowsInvalidCurrencyException_thenThrowsAnInvalidCurrencyException() {
        final String sourceCurrency = "USD";
        final String targetCurrency = "EUR";

        Mockito.when(fxApiAccessor.getLatestExchangeRateBetween(anyString(), anyString()))
               .thenThrow(InvalidCurrencyException.withDefaultMessage());

        final Exception exception = assertThrows(InvalidCurrencyException.class,
                                           () -> fxApiExchangeRateService.getLatestExchangeRateBetween(sourceCurrency,
                                                                                                       targetCurrency));

        assertEquals(InvalidCurrencyException.DEFAULT_MESSAGE, exception.getMessage());
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

        Mockito.when(fxApiAccessor.getLatestExchangeRateBetween(anyString(), anyString()))
               .thenReturn(Optional.of(exchangeRateResponse));

        final ExchangeRate result = fxApiExchangeRateService.getLatestExchangeRateBetween(sourceCurrency,
                                                                                          targetCurrency);

        assertEquals(sourceCurrency, result.sourceCurrency());
        assertEquals(targetCurrency, result.targetCurrency());
        assertEquals(value, result.value());
    }
}
