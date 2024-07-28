package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exchange_conversion.dto.ConversionHistoryQuery;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.exchange_conversion.model.Conversion;
import com.brkygngr.foreign_exchange.exchange_conversion.repository.ConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DBConversionHistoryService implements ConversionHistoryService {

    private final ConversionRepository conversionRepository;

    public Page<ExchangeConversion> getHistoryByQuery(final ConversionHistoryQuery conversionHistoryQuery,
                                                      final Pageable pageable) {
        Optional<UUID> optionalID = conversionHistoryQuery.transactionID();
        Optional<LocalDate> optionalDate = conversionHistoryQuery.transactionDate();
        Page<Conversion> conversionPage = Page.empty(pageable);

        if (optionalID.isPresent() && optionalDate.isPresent()) {
            Instant start = optionalDate.get().atStartOfDay(ZoneId.of("UTC")).toInstant();
            Instant end = start.plus(1, ChronoUnit.DAYS);

            conversionPage = conversionRepository.findAllByIdAndCreatedAtBetween(optionalID.get(),
                                                                                 start,
                                                                                 end,
                                                                                 pageable);
        } else if (optionalID.isPresent()) {
            conversionPage = conversionRepository.findAllById(optionalID.get(), pageable);
        } else if (optionalDate.isPresent()) {
            Instant start = optionalDate.get().atStartOfDay(ZoneId.of("UTC")).toInstant();
            Instant end = start.plus(1, ChronoUnit.DAYS);

            conversionPage = conversionRepository.findAllByCreatedAtBetween(start, end, pageable);
        }

        return conversionPage.map(ExchangeConversion::fromConversion);
    }
}
