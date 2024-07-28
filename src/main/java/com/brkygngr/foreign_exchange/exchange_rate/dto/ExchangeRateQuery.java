package com.brkygngr.foreign_exchange.exchange_rate.dto;

import com.brkygngr.foreign_exchange.exception.ValidationErrorMessages;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversionRequest;
import com.brkygngr.foreign_exchange.validation.FirstGroup;
import com.brkygngr.foreign_exchange.validation.SecondGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@GroupSequence({ExchangeRateQuery.class, FirstGroup.class, SecondGroup.class})
public record ExchangeRateQuery(@NotBlank(message = ValidationErrorMessages.SOURCE_CURRENCY_REQUIRED,
                                          groups = FirstGroup.class)
                                @Size(min = 3,
                                      max = 3,
                                      message = ValidationErrorMessages.SOURCE_CURRENCY_LENGTH,
                                      groups = FirstGroup.class) String sourceCurrency,
                                @NotBlank(message = ValidationErrorMessages.TARGET_CURRENCY_REQUIRED,
                                          groups = FirstGroup.class)
                                @Size(min = 3,
                                      max = 3,
                                      message = ValidationErrorMessages.TARGET_CURRENCY_LENGTH,
                                      groups = FirstGroup.class) String targetCurrency) {

    @AssertTrue(message = ValidationErrorMessages.SOURCE_AND_TARGET_MUST_BE_DIFFERENT, groups = SecondGroup.class)
    private boolean isSourceAndTargetDifferent() {
        return !sourceCurrency.equals(targetCurrency);
    }
}
