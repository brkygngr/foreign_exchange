package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exchange_conversion.dto.ConversionHistoryQuery;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.exchange_conversion.model.Conversion;
import com.brkygngr.foreign_exchange.exchange_conversion.repository.ConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DBConversionHistoryService implements ConversionHistoryService {

    private final ConversionRepository conversionRepository;

    public Page<ExchangeConversion> getHistoryByQuery(final ConversionHistoryQuery conversionHistoryQuery,
                                                      final Pageable pageable) {
        Page<Conversion> conversionPage = new PageImpl<>(List.of());

        if (conversionHistoryQuery.transactionID().isPresent()) {
            conversionPage = conversionRepository.findAllById(conversionHistoryQuery.transactionID().get(),
                                                              pageable);
        }

        if (conversionHistoryQuery.transactionDate().isPresent()) {
            LocalDate date = conversionHistoryQuery.transactionDate().get();
            Instant start = date.atStartOfDay(ZoneId.of("UTC")).toInstant();
            Instant end = date.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();

            conversionPage = conversionRepository.findAllByCreatedAtBetween(start,
                                                                            end,
                                                                            pageable);
        }

        return conversionPage.map(ExchangeConversion::fromConversion);
    }
}
