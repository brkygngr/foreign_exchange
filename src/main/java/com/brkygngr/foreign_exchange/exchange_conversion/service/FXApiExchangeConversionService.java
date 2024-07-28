package com.brkygngr.foreign_exchange.exchange_conversion.service;

import com.brkygngr.foreign_exchange.exception.ResponseNullException;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.exchange_conversion.model.Conversion;
import com.brkygngr.foreign_exchange.exchange_conversion.repository.ConversionRepository;
import com.brkygngr.foreign_exchange.fx_api.FXApiAccessor;
import com.brkygngr.foreign_exchange.fx_api.dto.FXApiExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FXApiExchangeConversionService implements ExchangeConversionService {

    private final FXApiAccessor fxApiAccessor;

    private final ConversionRepository conversionRepository;

    public ExchangeConversion convertAmount(final BigDecimal amount,
                                            final String sourceCurrency,
                                            final String targetCurrency) {
        FXApiExchangeRateResponse fxApiExchangeRateResponse = fxApiAccessor
                .getLatestExchangeRateBetween(sourceCurrency,
                                              targetCurrency)
                .orElseThrow(
                        () -> new ResponseNullException(
                                String.format(
                                        "For source %s and target %s currency API responded with null!",
                                        sourceCurrency,
                                        targetCurrency))
                );


        BigDecimal exchangeRate = fxApiExchangeRateResponse.data().get(targetCurrency).value();

        Conversion conversion = new Conversion();
        conversion.setAmount(amount);
        conversion.setSourceCurrency(sourceCurrency);
        conversion.setTargetCurrency(targetCurrency);
        conversion.setExchangeRate(exchangeRate);

        Conversion savedConversion = conversionRepository.save(conversion);

        return ExchangeConversion.fromConversion(savedConversion);
    }
}
