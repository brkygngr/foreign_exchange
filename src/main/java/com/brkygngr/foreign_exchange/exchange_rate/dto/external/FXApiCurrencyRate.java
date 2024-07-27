package com.brkygngr.foreign_exchange.exchange_rate.dto.external;

import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class FXApiCurrencyRate {

    private String code;

    private BigDecimal value;

    public ExchangeRate toExchangeRate(final String sourceCurrency) {
        return new ExchangeRate(
                sourceCurrency,
                this.code,
                this.value
        );
    }
}
