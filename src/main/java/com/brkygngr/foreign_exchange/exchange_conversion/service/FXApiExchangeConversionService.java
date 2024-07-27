package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FXApiExchangeConversionService implements ExchangeConversionService {

    public ExchangeConversion convertAmount(final BigDecimal amount,
                                            final String sourceCurrency,
                                            final String targetCurrency) {
        return new ExchangeConversion(UUID.randomUUID(), BigDecimal.ZERO);
    }
}
