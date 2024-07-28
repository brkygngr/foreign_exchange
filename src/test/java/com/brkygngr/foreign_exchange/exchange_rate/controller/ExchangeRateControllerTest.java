package com.brkygngr.foreign_exchange.exchange_rate.controller;

import com.brkygngr.foreign_exchange.exception.AppError;
import com.brkygngr.foreign_exchange.exception.ErrorResponse;
import com.brkygngr.foreign_exchange.exception.InvalidCurrencyException;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.exchange_rate.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static com.brkygngr.foreign_exchange.exception.ErrorCodes.CURRENCY_CODE_INVALID_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.SOURCE_AND_TARGET_CURRENCIES_EQUAL_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.SOURCE_CURRENCY_BLANK_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.SOURCE_CURRENCY_CODE_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.TARGET_CURRENCY_BLANK_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.TARGET_CURRENCY_CODE_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.SOURCE_AND_TARGET_MUST_BE_DIFFERENT;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.SOURCE_CURRENCY_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.SOURCE_CURRENCY_REQUIRED;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.TARGET_CURRENCY_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.TARGET_CURRENCY_REQUIRED;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeRateController.class)
class ExchangeRateControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    void getExchangeRateBetween_whenSourceCurrencyIsNull_thenThrowsAnException() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        final ErrorResponse expectedErrorResponse = new ErrorResponse(instant,
                                                                      new AppError[]{
                                                                              new AppError(SOURCE_CURRENCY_REQUIRED,
                                                                                           SOURCE_CURRENCY_BLANK_ERROR_CODE)});

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("targetCurrency", "EUR")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.content()
                                                   .string(objectMapper.writeValueAsString(expectedErrorResponse)));
        }
    }

    @Test
    void getExchangeRateBetween_whenTargetCurrencyIsNull_thenThrowsAnException() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        final ErrorResponse expectedErrorResponse = new ErrorResponse(instant,
                                                                      new AppError[]{
                                                                              new AppError(TARGET_CURRENCY_REQUIRED,
                                                                                           TARGET_CURRENCY_BLANK_ERROR_CODE)});

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("sourceCurrency", "EUR")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.content()
                                                   .string(objectMapper.writeValueAsString(expectedErrorResponse)));
        }
    }

    @Test
    void getExchangeRateBetween_whenSourceCurrencyLengthIsNotEqualToThree_thenThrowsAnException() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        final ErrorResponse expectedErrorResponse = new ErrorResponse(instant,
                                                                      new AppError[]{
                                                                              new AppError(SOURCE_CURRENCY_CODE,
                                                                                           SOURCE_CURRENCY_CODE_ERROR_CODE)});

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("sourceCurrency", "ABCDE")
                                    .queryParam("targetCurrency", "EUR")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.content()
                                                   .string(objectMapper.writeValueAsString(expectedErrorResponse)));
        }
    }

    @Test
    void getExchangeRateBetween_whenTargetCurrencyLengthIsNotEqualToThree_thenThrowsAnException() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        final ErrorResponse expectedErrorResponse = new ErrorResponse(instant,
                                                                      new AppError[]{
                                                                              new AppError(TARGET_CURRENCY_CODE,
                                                                                           TARGET_CURRENCY_CODE_ERROR_CODE)});

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("sourceCurrency", "EUR")
                                    .queryParam("targetCurrency", "ABCDE")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.content()
                                                   .string(objectMapper.writeValueAsString(expectedErrorResponse)));
        }
    }

    @Test
    void getExchangeRateBetween_whenTargetCurrencyAndSourceCurrencySame_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        final ErrorResponse expectedErrorResponse = new ErrorResponse(instant,
                                                                      new AppError[]{
                                                                              new AppError(SOURCE_AND_TARGET_MUST_BE_DIFFERENT,
                                                                                           SOURCE_AND_TARGET_CURRENCIES_EQUAL_ERROR_CODE)});

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("sourceCurrency", "EUR")
                                    .queryParam("targetCurrency", "EUR")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.content()
                                                   .string(objectMapper.writeValueAsString(expectedErrorResponse)));
        }
    }

    @Test
    void getExchangeRateBetween_whenInvalidCurrencyIsGiven_ThenThrowsAnException() throws Exception {
        final String invalidCurrency = "ABC";
        final String targetCurrency = "EUR";
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        Mockito.when(exchangeRateService.getLatestExchangeRateBetween(invalidCurrency, targetCurrency))
               .thenThrow(new InvalidCurrencyException("invalid currency error"));

        final ErrorResponse expectedErrorResponse = new ErrorResponse(instant,
                                                                      new AppError[]{
                                                                              new AppError("invalid currency error",
                                                                                           CURRENCY_CODE_INVALID_ERROR_CODE)});

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("sourceCurrency", invalidCurrency)
                                    .queryParam("targetCurrency", "EUR")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isUnprocessableEntity())
                   .andExpect(MockMvcResultMatchers.content()
                                                   .string(objectMapper.writeValueAsString(expectedErrorResponse)));

        }
    }

    @Test
    void getExchangeRateBetween_returnsUSDToEURExchangeRate() throws Exception {
        final String sourceCurrency = "USD";
        final String targetCurrency = "EUR";

        ExchangeRate exchangeRate = new ExchangeRate(sourceCurrency, targetCurrency, BigDecimal.ONE);

        Mockito.when(exchangeRateService.getLatestExchangeRateBetween(sourceCurrency, targetCurrency)).thenReturn(
                exchangeRate);

        mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/exchange-rate")
                                .queryParam("sourceCurrency", sourceCurrency)
                                .queryParam("targetCurrency", targetCurrency)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.sourceCurrency").value(exchangeRate.sourceCurrency()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.targetCurrency").value(exchangeRate.targetCurrency()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(exchangeRate.value()));
    }
}
