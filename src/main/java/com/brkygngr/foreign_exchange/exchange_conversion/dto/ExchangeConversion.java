package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import com.brkygngr.foreign_exchange.exchange_conversion.model.Conversion;

import java.math.BigDecimal;
import java.util.UUID;

public record ExchangeConversion(UUID id, BigDecimal convertedAmount, String sourceCurrency, String targetCurrency) {

    public static ExchangeConversion fromConversion(Conversion conversion) {
        return new ExchangeConversion(conversion.getId(),
                                      conversion.convertedAmount(),
                                      conversion.getSourceCurrency(),
                                      conversion.getTargetCurrency());
    }
}
