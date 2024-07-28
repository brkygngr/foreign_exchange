package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ExchangeConversion(UUID id, BigDecimal convertedAmount) {
}
