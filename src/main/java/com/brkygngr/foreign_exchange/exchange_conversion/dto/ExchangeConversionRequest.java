package com.brkygngr.foreign_exchange.exchange_conversion.dto;

import com.brkygngr.foreign_exchange.validation.conversion.ConversionAmountNotNull;
import com.brkygngr.foreign_exchange.validation.conversion.ConversionAmountPositive;
import com.brkygngr.foreign_exchange.validation.currency.CurrenciesNotEqual;
import com.brkygngr.foreign_exchange.validation.currency.CurrencyCode;
import com.brkygngr.foreign_exchange.validation.FirstGroup;
import com.brkygngr.foreign_exchange.validation.SecondGroup;
import com.brkygngr.foreign_exchange.validation.currency.CurrencyNotBlank;
import jakarta.validation.GroupSequence;

import java.math.BigDecimal;

import static com.brkygngr.foreign_exchange.exception.ErrorCodes.CONVERSION_AMOUNT_NEGATIVE_OR_ZERO_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.CONVERSION_AMOUNT_NULL_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.SOURCE_AND_TARGET_CURRENCIES_EQUAL_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.SOURCE_CURRENCY_BLANK_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.SOURCE_CURRENCY_CODE_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.TARGET_CURRENCY_BLANK_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.TARGET_CURRENCY_CODE_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.CONVERSION_AMOUNT_POSITIVE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.CONVERSION_AMOUNT_REQUIRED;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.SOURCE_AND_TARGET_MUST_BE_DIFFERENT;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.SOURCE_CURRENCY_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.SOURCE_CURRENCY_REQUIRED;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.TARGET_CURRENCY_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.TARGET_CURRENCY_REQUIRED;

@GroupSequence({ExchangeConversionRequest.class, FirstGroup.class, SecondGroup.class})
public record ExchangeConversionRequest(@ConversionAmountNotNull(message = CONVERSION_AMOUNT_REQUIRED,
                                                                 errorCode = CONVERSION_AMOUNT_NULL_ERROR_CODE)
                                        @ConversionAmountPositive(message = CONVERSION_AMOUNT_POSITIVE,
                                                                  errorCode = CONVERSION_AMOUNT_NEGATIVE_OR_ZERO_ERROR_CODE)
                                        BigDecimal amount,
                                        @CurrencyNotBlank(message = SOURCE_CURRENCY_REQUIRED,
                                                          errorCode = SOURCE_CURRENCY_BLANK_ERROR_CODE,
                                                          groups = FirstGroup.class)
                                        @CurrencyCode(message = SOURCE_CURRENCY_CODE,
                                                      errorCode = SOURCE_CURRENCY_CODE_ERROR_CODE,
                                                      groups = FirstGroup.class)
                                        String sourceCurrency,
                                        @CurrencyNotBlank(message = TARGET_CURRENCY_REQUIRED,
                                                          errorCode = TARGET_CURRENCY_BLANK_ERROR_CODE,
                                                          groups = FirstGroup.class)
                                        @CurrencyCode(message = TARGET_CURRENCY_CODE,
                                                      errorCode = TARGET_CURRENCY_CODE_ERROR_CODE,
                                                      groups = FirstGroup.class)
                                        String targetCurrency) {

    @CurrenciesNotEqual(message = SOURCE_AND_TARGET_MUST_BE_DIFFERENT,
                        errorCode = SOURCE_AND_TARGET_CURRENCIES_EQUAL_ERROR_CODE,
                        groups = SecondGroup.class)
    private boolean isSourceAndTargetDifferent() {
        return !sourceCurrency.equals(targetCurrency);
    }
}
