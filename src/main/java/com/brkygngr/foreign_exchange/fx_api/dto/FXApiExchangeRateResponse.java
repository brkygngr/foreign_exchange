package com.brkygngr.foreign_exchange.fx_api.dto;

import java.util.Map;

public record FXApiExchangeRateResponse(FXApiMeta meta, Map<String, FXApiCurrencyRate> data) {

}
