package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exchange_conversion.dto.ConversionHistoryQuery;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConversionHistoryService {

    Page<ExchangeConversion> getHistoryByQuery(final ConversionHistoryQuery conversionHistoryQuery,
                                               final Pageable pageable);
}
