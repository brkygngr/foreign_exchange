package com.brkygngr.foreign_exchange.exchange_rate.controller;

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
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("Source currency is required!"));
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
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("Target currency is required!"));
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
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("Source currency must be a three letter source currency code!"));
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
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("Target currency must be a three letter source currency code!"));
        }
    }
}
