package com.brkygngr.foreign_exchange.exchange_rate.controller;

import com.brkygngr.foreign_exchange.exception.InvalidCurrencyException;
import com.brkygngr.foreign_exchange.exception.ValidationErrorMessages;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.exchange_rate.service.ExchangeRateService;
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

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeRateController.class)
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Test
    void getExchangeRateBetween_whenSourceCurrencyIsNull_thenThrowsAnException() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("targetCurrency", "EUR")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(ValidationErrorMessages.SOURCE_CURRENCY_REQUIRED));
        }
    }

    @Test
    void getExchangeRateBetween_whenTargetCurrencyIsNull_thenThrowsAnException() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("sourceCurrency", "EUR")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(ValidationErrorMessages.TARGET_CURRENCY_REQUIRED));
        }
    }

    @Test
    void getExchangeRateBetween_whenSourceCurrencyLengthIsNotEqualToThree_thenThrowsAnException() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("sourceCurrency", "ABCDE")
                                    .queryParam("targetCurrency", "EUR")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.SOURCE_CURRENCY_LENGTH));
        }
    }

    @Test
    void getExchangeRateBetween_whenTargetCurrencyLengthIsNotEqualToThree_thenThrowsAnException() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("sourceCurrency", "EUR")
                                    .queryParam("targetCurrency", "ABCDE")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.TARGET_CURRENCY_LENGTH));
        }
    }

    @Test
    void getExchangeRateBetween_whenTargetCurrencyAndSourceCurrencySame_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/exchange-rate")
                                    .queryParam("sourceCurrency", "EUR")
                                    .queryParam("targetCurrency", "EUR")
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.SOURCE_AND_TARGET_MUST_BE_DIFFERENT));
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

    @Test
    void getExchangeRateBetween_whenInvalidCurrencyIsGiven_ThenThrowsAnException() throws Exception {
        final String invalidCurrency = "ABC";
        final String targetCurrency = "EUR";

        Mockito.when(exchangeRateService.getLatestExchangeRateBetween(invalidCurrency, targetCurrency))
               .thenThrow(new InvalidCurrencyException("invalid currency error"));

        mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/exchange-rate")
                                .queryParam("sourceCurrency", invalidCurrency)
                                .queryParam("targetCurrency", "EUR")
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isUnprocessableEntity())
               .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
               .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                               .value("invalid currency error"));
    }
}
