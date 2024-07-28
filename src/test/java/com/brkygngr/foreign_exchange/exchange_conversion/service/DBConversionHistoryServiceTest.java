package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exchange_conversion.dto.ConversionHistoryQuery;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.exchange_conversion.model.Conversion;
import com.brkygngr.foreign_exchange.exchange_conversion.repository.ConversionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DBConversionHistoryServiceTest {

    private AutoCloseable autoCloseable;

    @Mock
    private ConversionRepository conversionRepository;

    private DBConversionHistoryService dbConversionHistoryService;

    @BeforeEach
    void setUp() {
        autoCloseable = openMocks(this);

        dbConversionHistoryService = new DBConversionHistoryService(conversionRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getHistoryByQuery_whenTransactionIDMatches_returnsExistingElement() {
        final UUID id = UUID.randomUUID();

        final Conversion mockExchangeConversion = new Conversion();
        mockExchangeConversion.setId(id);
        mockExchangeConversion.setAmount(BigDecimal.ONE);
        mockExchangeConversion.setExchangeRate(BigDecimal.ONE);

        final Page<Conversion> mockPage = new PageImpl<>(List.of(mockExchangeConversion));

        when(conversionRepository.findAllById(eq(id), any(Pageable.class))).thenReturn(mockPage);

        final ConversionHistoryQuery conversionHistoryQuery = new ConversionHistoryQuery(Optional.of(id),
                                                                                         Optional.empty());


        final Page<ExchangeConversion> exchangeConversionPage = dbConversionHistoryService.getHistoryByQuery(
                conversionHistoryQuery,
                mockPage.getPageable());

        assertEquals(1, exchangeConversionPage.getTotalElements());
        assertEquals(mockExchangeConversion.getId(), exchangeConversionPage.getContent().getFirst().id());
        assertEquals(mockExchangeConversion.convertedAmount(),
                     exchangeConversionPage.getContent().getFirst().convertedAmount());
    }

    @Test
    void getHistoryByQuery_whenTransactionDateMatches_returnsExistingElement() {
        final UUID id = UUID.randomUUID();

        final Conversion conversion = new Conversion();
        conversion.setId(id);
        conversion.setAmount(BigDecimal.ONE);
        conversion.setExchangeRate(BigDecimal.ONE);

        final Page<Conversion> mockPage = new PageImpl<>(List.of(conversion));

        when(conversionRepository.findAllByCreatedAtBetween(eq(Instant.parse("2024-01-01T00:00:00Z")),
                                                            eq(Instant.parse("2024-01-02T00:00:00Z")),
                                                            any(Pageable.class))).thenReturn(mockPage);

        final ConversionHistoryQuery conversionHistoryQuery = new ConversionHistoryQuery(Optional.empty(),
                                                                                         Optional.of(LocalDate.of(2024,
                                                                                                                  1,
                                                                                                                  1)));


        final Page<ExchangeConversion> exchangeConversionPage = dbConversionHistoryService.getHistoryByQuery(
                conversionHistoryQuery,
                mockPage.getPageable());

        assertEquals(1, exchangeConversionPage.getTotalElements());
        assertEquals(conversion.getId(), exchangeConversionPage.getContent().getFirst().id());
        assertEquals(conversion.convertedAmount(), exchangeConversionPage.getContent().getFirst().convertedAmount());
    }

    @Test
    void getHistoryByQuery_whenTransactionIDAndTransactionDateMatches_returnsExistingElement() {
        final UUID id = UUID.randomUUID();

        final Conversion conversion = new Conversion();
        conversion.setId(id);
        conversion.setAmount(BigDecimal.ONE);
        conversion.setExchangeRate(BigDecimal.ONE);

        final Page<Conversion> mockPage = new PageImpl<>(List.of(conversion));

        when(conversionRepository.findAllByIdAndCreatedAtBetween(eq(conversion.getId()),
                                                                 eq(Instant.parse("2024-01-01T00:00:00Z")),
                                                                 eq(Instant.parse("2024-01-02T00:00:00Z")),
                                                                 any(Pageable.class))).thenReturn(mockPage);

        final ConversionHistoryQuery conversionHistoryQuery = new ConversionHistoryQuery(Optional.of(conversion.getId()),
                                                                                         Optional.of(LocalDate.of(2024,
                                                                                                                  1,
                                                                                                                  1)));


        final Page<ExchangeConversion> exchangeConversionPage = dbConversionHistoryService.getHistoryByQuery(
                conversionHistoryQuery,
                mockPage.getPageable());

        assertEquals(1, exchangeConversionPage.getTotalElements());
        assertEquals(conversion.getId(), exchangeConversionPage.getContent().getFirst().id());
        assertEquals(conversion.convertedAmount(), exchangeConversionPage.getContent().getFirst().convertedAmount());
    }
}