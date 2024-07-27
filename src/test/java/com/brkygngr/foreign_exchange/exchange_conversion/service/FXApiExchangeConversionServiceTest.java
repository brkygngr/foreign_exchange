package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exception.ResponseNullException;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.fx_api.FXApiAccessor;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiCurrencyRate;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiExchangeRateResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class FXApiExchangeConversionServiceTest {

    private AutoCloseable autoCloseable;

    @Mock
    private FXApiAccessor fxApiAccessor;

    private FXApiExchangeConversionService fxApiExchangeConversionService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        fxApiExchangeConversionService = new FXApiExchangeConversionService(fxApiAccessor);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void convertAmount_returnsATransactionId() {
        final UUID randomUUID = UUID.fromString("3d98d61f-2b64-4f11-8e05-a2a5ff5b13ec");
        final String targetCurrency = "USD";

        when(fxApiAccessor.getLatestExchangeRateBetween(anyString(),
                                                        anyString())).thenReturn(Optional.of(new FXApiExchangeRateResponse(
                null,
                Map.of(targetCurrency, new FXApiCurrencyRate(targetCurrency, BigDecimal.TWO)))));

        try (MockedStatic<UUID> mockedStatic = mockStatic(UUID.class)) {
            mockedStatic.when(UUID::randomUUID).thenReturn(randomUUID);

            final ExchangeConversion result = fxApiExchangeConversionService.convertAmount(BigDecimal.ONE, "EUR", targetCurrency);

            assertEquals(randomUUID, result.id());
        }
    }

    @Test
    void convertAmount_whenAmountIsFiveAndConversionRateIsTwo_returnsConvertedAmountAsTen() {
        final String targetCurrency = "USD";

        when(fxApiAccessor.getLatestExchangeRateBetween(anyString(),
                                                        anyString())).thenReturn(Optional.of(new FXApiExchangeRateResponse(
                null,
                Map.of(targetCurrency, new FXApiCurrencyRate(targetCurrency, BigDecimal.TWO)))));

        ExchangeConversion result = fxApiExchangeConversionService.convertAmount(BigDecimal.valueOf(5),
                                                                                 "EUR",
                                                                                 targetCurrency);

        assertEquals(BigDecimal.valueOf(10), result.convertedAmount());
    }
}
