package com.brkygngr.foreign_exchange.exchange_conversion.controller;

import com.brkygngr.foreign_exchange.exception.ValidationErrorMessages;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeConversionController.class)
class ExchangeConversionControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getConversion_whenAmountIsNull_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(null, "EUR", "USD");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.CONVERSION_AMOUNT_REQUIRED));
        }
    }

    @Test
    void getConversion_whenAmountIsNegative_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.valueOf(-10),
                                                                                    "EUR",
                                                                                    "USD");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.CONVERSION_AMOUNT_POSITIVE));
        }
    }

    @Test
    void getConversion_whenAmountIsZero_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.valueOf(0),
                                                                                    "EUR",
                                                                                    "USD");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.CONVERSION_AMOUNT_POSITIVE));
        }
    }

    @Test
    void getConversion_whenSourceCurrencyIsNull_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, null, "USD");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.SOURCE_CURRENCY_REQUIRED));
        }
    }

    @Test
    void getConversion_whenSourceCurrencyIsEmptyString_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "", "USD");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(containsInAnyOrder(ValidationErrorMessages.SOURCE_CURRENCY_REQUIRED,
                                                                             ValidationErrorMessages.SOURCE_CURRENCY_LENGTH)));
        }
    }

    @Test
    void getConversion_whenTargetCurrencyIsNull_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "EUR", null);

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(ValidationErrorMessages.TARGET_CURRENCY_REQUIRED));
        }
    }

    @Test
    void getConversion_whenTargetCurrencyIsEmptyString_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "EUR", "");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(containsInAnyOrder(ValidationErrorMessages.TARGET_CURRENCY_REQUIRED,
                                                                             ValidationErrorMessages.TARGET_CURRENCY_LENGTH)));
        }
    }

    @Test
    void getConversion_whenSourceCurrencyHasMoreThenThreeLetters_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "ABCDE", "USD");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.SOURCE_CURRENCY_LENGTH));
        }
    }

    @Test
    void getConversion_whenSourceCurrencyHasLessThenThreeLetters_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "A", "USD");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.SOURCE_CURRENCY_LENGTH));
        }
    }

    @Test
    void getConversion_whenTargetCurrencyHasMoreThenThreeLetters_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "EUR", "ABCDE");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.TARGET_CURRENCY_LENGTH));
        }
    }

    @Test
    void getConversion_whenTargetCurrencyHasLessThenThreeLetters_returnsBadRequestErrorResponse() throws Exception {
        final String timestamp = "2024-01-01T00:00:00Z";

        final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "USD", "A");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversion")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isBadRequest())
                   .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                   .value(ValidationErrorMessages.TARGET_CURRENCY_LENGTH));
        }
    }
}
