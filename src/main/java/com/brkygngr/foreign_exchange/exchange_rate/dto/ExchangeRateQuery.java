package com.brkygngr.foreign_exchange.exchange_rate.dto;

import com.brkygngr.foreign_exchange.exception.ValidationErrorMessages;
import com.brkygngr.foreign_exchange.validation.CurrencyCode;
import com.brkygngr.foreign_exchange.validation.CurrencyNotBlank;
import com.brkygngr.foreign_exchange.validation.FirstGroup;
import com.brkygngr.foreign_exchange.validation.SecondGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.AssertTrue;

import static com.brkygngr.foreign_exchange.exception.ErrorCodes.SOURCE_CURRENCY_BLANK_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.SOURCE_CURRENCY_CODE_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.TARGET_CURRENCY_BLANK_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ErrorCodes.TARGET_CURRENCY_CODE_ERROR_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.SOURCE_CURRENCY_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.SOURCE_CURRENCY_REQUIRED;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.TARGET_CURRENCY_CODE;
import static com.brkygngr.foreign_exchange.exception.ValidationErrorMessages.TARGET_CURRENCY_REQUIRED;

@GroupSequence({ExchangeRateQuery.class, FirstGroup.class, SecondGroup.class})
public record ExchangeRateQuery(@CurrencyNotBlank(message = SOURCE_CURRENCY_REQUIRED,
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

    @AssertTrue(message = ValidationErrorMessages.SOURCE_AND_TARGET_MUST_BE_DIFFERENT, groups = SecondGroup.class)
    private boolean isSourceAndTargetDifferent() {
        return !sourceCurrency.equals(targetCurrency);
    }
}
