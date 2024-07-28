package com.brkygngr.foreign_exchange.exchange_conversion.controller;

import com.brkygngr.foreign_exchange.exception.ValidationErrorMessages;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversionRequest;
import com.brkygngr.foreign_exchange.exchange_conversion.service.ExchangeConversionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeConversionController.class)
class ExchangeConversionControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeConversionService exchangeConversionService;

    @Nested
    class PostConversion {
        @Test
        void whenAmountIsNull_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(null, "EUR", "USD");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void whenAmountIsNegative_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.valueOf(-10),
                                                                                        "EUR",
                                                                                        "USD");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void whenAmountIsZero_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.valueOf(0),
                                                                                        "EUR",
                                                                                        "USD");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void whenSourceCurrencyIsNull_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, null, "USD");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void whenSourceCurrencyIsEmptyString_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "", "USD");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void whenTargetCurrencyIsNull_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "EUR", null);

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                       .value(ValidationErrorMessages.TARGET_CURRENCY_REQUIRED));
            }
        }

        @Test
        void whenTargetCurrencyIsEmptyString_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "EUR", "");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void whenSourceCurrencyHasMoreThenThreeLetters_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "ABCDE", "USD");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void whenSourceCurrencyHasLessThenThreeLetters_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "A", "USD");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void whenTargetCurrencyHasMoreThenThreeLetters_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "EUR", "ABCDE");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void whenTargetCurrencyHasLessThenThreeLetters_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "USD", "A");

                mockMvc.perform(MockMvcRequestBuilders
                                        .post("/api/v1/conversions")
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
        void returnsConvertedAmount() throws Exception {
            ExchangeConversion expected = new ExchangeConversion(UUID.randomUUID(), BigDecimal.TEN);

            when(exchangeConversionService.convertAmount(any(BigDecimal.class), anyString(), anyString())).thenReturn(
                    expected);

            final ExchangeConversionRequest request = new ExchangeConversionRequest(BigDecimal.ONE, "USD", "EUR");

            mockMvc.perform(MockMvcRequestBuilders
                                    .post("/api/v1/conversions")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isCreated())
                   .andExpect(MockMvcResultMatchers.header().string("location", "http://localhost/api/v1/conversions/" + expected.id()))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expected.id().toString()))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.convertedAmount").value(expected.convertedAmount()));
        }
    }

    @Nested
    class GetConversionHistory {
        @Test
        void whenIDAndDateIsNull_returnsBadRequestErrorResponse() throws Exception {
            final String timestamp = "2024-01-01T00:00:00Z";

            final Instant instant = Instant.now(Clock.fixed(Instant.parse(timestamp), ZoneId.of("UTC")));

            try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
                mockedStatic.when(Instant::now).thenReturn(instant);

                mockMvc.perform(MockMvcRequestBuilders
                                        .get("/api/v1/conversions/history")
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(timestamp))
                       .andExpect(MockMvcResultMatchers.jsonPath("$.errors")
                                                       .value(ValidationErrorMessages.CONVERSION_HISTORY_REQUIRED));
            }
        }

        @Test
        void whenOnlyIDIsAvailableAndIDIsValid_returnsOkResponse() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/conversions/history")
                                    .queryParam("transactionID", UUID.randomUUID().toString())
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk());
        }

        @Test
        void whenOnlyDateIsAvailableAndDateIsValid_returnsOkResponse() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                                    .get("/api/v1/conversions/history")
                                    .queryParam("transactionDate", LocalDate.of(2024, 1, 1).format(DateTimeFormatter.ISO_DATE))
                                    .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk());
        }
    }
}
