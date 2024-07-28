package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exception.ResponseNullException;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.exchange_conversion.model.Conversion;
import com.brkygngr.foreign_exchange.exchange_conversion.repository.ConversionRepository;
import com.brkygngr.foreign_exchange.fx_api.FXApiAccessor;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiCurrencyRate;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiExchangeRateResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FXApiExchangeConversionServiceTest {

    private AutoCloseable autoCloseable;

    @Captor
    ArgumentCaptor<Conversion> conversionArgumentCaptor;

    @Mock
    private FXApiAccessor fxApiAccessor;

    @Mock
    private ConversionRepository conversionRepository;

    private FXApiExchangeConversionService fxApiExchangeConversionService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        fxApiExchangeConversionService = new FXApiExchangeConversionService(fxApiAccessor, conversionRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void convertAmount_returnsATransactionId() {
        final String sourceCurrency = "EUR";
        final String targetCurrency = "USD";

        when(fxApiAccessor.getLatestExchangeRateBetween(anyString(),
                                                        anyString())).thenReturn(Optional.of(new FXApiExchangeRateResponse(
                null,
                Map.of(targetCurrency, new FXApiCurrencyRate(targetCurrency, BigDecimal.TWO)))));

        final Conversion conversion = new Conversion();
        conversion.setId(UUID.randomUUID());
        conversion.setAmount(BigDecimal.ONE);
        conversion.setSourceCurrency(sourceCurrency);
        conversion.setTargetCurrency(targetCurrency);
        conversion.setExchangeRate(BigDecimal.TWO);

        when(conversionRepository.save(any(Conversion.class))).thenReturn(conversion);


        final ExchangeConversion result = fxApiExchangeConversionService.convertAmount(BigDecimal.ONE,
                                                                                       sourceCurrency,
                                                                                       targetCurrency);

        assertEquals(conversion.getId(), result.id());
    }

    @Test
    void convertAmount_whenAmountIsFiveAndConversionRateIsTwo_returnsConvertedAmountAsTen() {
        final BigDecimal amount = BigDecimal.valueOf(5);
        final String sourceCurrency = "EUR";
        final String targetCurrency = "USD";

        when(fxApiAccessor.getLatestExchangeRateBetween(anyString(),
                                                        anyString())).thenReturn(Optional.of(new FXApiExchangeRateResponse(
                null,
                Map.of(targetCurrency, new FXApiCurrencyRate(targetCurrency, BigDecimal.TWO)))));

        final Conversion conversion = new Conversion();
        conversion.setId(UUID.randomUUID());
        conversion.setAmount(amount);
        conversion.setSourceCurrency(sourceCurrency);
        conversion.setTargetCurrency(targetCurrency);
        conversion.setExchangeRate(BigDecimal.TWO);

        when(conversionRepository.save(any(Conversion.class))).thenReturn(conversion);

        final ExchangeConversion result = fxApiExchangeConversionService.convertAmount(amount,
                                                                                       sourceCurrency,
                                                                                       targetCurrency);

        assertEquals(BigDecimal.valueOf(10), result.convertedAmount());
    }

    @Test
    void convertAmount_whenAPIResponseIsNull_thenThrowsResponseNullException() {
        final String sourceCurrency = "USD";
        final String targetCurrency = "EUR";

        Mockito.when(fxApiAccessor.getLatestExchangeRateBetween(anyString(), anyString()))
               .thenReturn(Optional.empty());

        final Exception exception = assertThrows(ResponseNullException.class,
                                                 () -> fxApiExchangeConversionService.convertAmount(BigDecimal.ONE,
                                                                                                    sourceCurrency,
                                                                                                    targetCurrency));

        assertEquals(String.format("For source %s and target %s currency API responded with null!",
                                   sourceCurrency,
                                   targetCurrency), exception.getMessage());
    }

    @Test
    void convertAmount_savesTheTransaction() {
        final BigDecimal amount = BigDecimal.valueOf(5);
        final String sourceCurrency = "EUR";
        final String targetCurrency = "USD";

        when(fxApiAccessor.getLatestExchangeRateBetween(anyString(),
                                                        anyString())).thenReturn(Optional.of(new FXApiExchangeRateResponse(
                null,
                Map.of(targetCurrency, new FXApiCurrencyRate(targetCurrency, BigDecimal.TWO)))));

        final Conversion conversion = new Conversion();
        conversion.setAmount(amount);
        conversion.setSourceCurrency(sourceCurrency);
        conversion.setTargetCurrency(targetCurrency);
        conversion.setExchangeRate(BigDecimal.TWO);

        when(conversionRepository.save(any(Conversion.class))).thenReturn(conversion);

        fxApiExchangeConversionService.convertAmount(amount,
                                                     sourceCurrency,
                                                     targetCurrency);

        verify(conversionRepository, times(1)).save(conversionArgumentCaptor.capture());

        final Conversion result = conversionArgumentCaptor.getValue();

        assertEquals(BigDecimal.valueOf(5), result.getAmount());
        assertEquals(BigDecimal.TWO, result.getExchangeRate());
        assertEquals(sourceCurrency, result.getSourceCurrency());
        assertEquals(targetCurrency, result.getTargetCurrency());
    }
}
