package com.brkygngr.foreign_exchange.exchange_rate.dto.external;

import java.util.Map;

public record FXApiExchangeRateResponse(FXApiMeta meta, Map<String, FXApiCurrencyRate> data) {

}
