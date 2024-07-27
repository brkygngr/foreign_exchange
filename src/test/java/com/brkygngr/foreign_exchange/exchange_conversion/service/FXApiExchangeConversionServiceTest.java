package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class FXApiExchangeConversionServiceTest {

    private AutoCloseable autoCloseable;

    private FXApiExchangeConversionService fxApiExchangeConversionService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        fxApiExchangeConversionService = new FXApiExchangeConversionService();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void convertAmount_returnsATransactionId() {
        UUID randomUUID = UUID.fromString("3d98d61f-2b64-4f11-8e05-a2a5ff5b13ec");

        try (MockedStatic<UUID> mockedStatic = mockStatic(UUID.class)) {
            mockedStatic.when(UUID::randomUUID).thenReturn(randomUUID);

            ExchangeConversion result = fxApiExchangeConversionService.convertAmount(BigDecimal.ONE, "EUR", "USD");

            assertEquals(randomUUID, result.id());
        }
    }
}
