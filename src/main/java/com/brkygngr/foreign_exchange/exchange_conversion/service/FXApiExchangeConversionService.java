package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.fx_api.FXApiAccessor;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FXApiExchangeConversionService implements ExchangeConversionService {

    private final FXApiAccessor fxApiAccessor;

    public ExchangeConversion convertAmount(final BigDecimal amount,
                                            final String sourceCurrency,
                                            final String targetCurrency) {
        FXApiExchangeRateResponse fxApiExchangeRateResponse = fxApiAccessor
                .getLatestExchangeRateBetween(sourceCurrency,
                                              targetCurrency)
                .orElse(null);


        BigDecimal exchangeRate = fxApiExchangeRateResponse.data().get(targetCurrency).value();

        return new ExchangeConversion(UUID.randomUUID(), exchangeRate.multiply(amount));
    }
}
