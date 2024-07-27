package com.brkygngr.foreign_exchange.exchange_rate.controller;

import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRateQuery;
import com.brkygngr.foreign_exchange.exchange_rate.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/exchange-rate")
    @Operation(summary = "Returns exchange rate from source to target currency")
    public ResponseEntity<ExchangeRate> getExchangeRateBetween(@ParameterObject
                                                               @Valid
                                                               ExchangeRateQuery exchangeRateQuery) {
        return ResponseEntity.ok(exchangeRateService.getLatestExchangeRateBetween(exchangeRateQuery.sourceCurrency(),
                                                                                  exchangeRateQuery.targetCurrency()));
    }
}
