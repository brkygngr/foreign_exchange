package com.brkygngr.foreign_exchange.exchange_rate.dto.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class FXApiExchangeRateResponse {

    private FXApiMeta meta;

    private Map<String, FXApiCurrencyRate> data;
}
