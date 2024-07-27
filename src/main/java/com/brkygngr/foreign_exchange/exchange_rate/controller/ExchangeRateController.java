package com.brkygngr.foreign_exchange.exchange_rate.controller;

import com.brkygngr.foreign_exchange.exception.ErrorResponse;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRate;
import com.brkygngr.foreign_exchange.exchange_rate.dto.ExchangeRateQuery;
import com.brkygngr.foreign_exchange.exchange_rate.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                         content = @Content(
                                 mediaType = "application/json",
                                 schema = @Schema(implementation = ExchangeRate.class))),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                         content = @Content(
                                 mediaType = "application/json",
                                 schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Currency invalid errors",
                         content = @Content(
                                 mediaType = "application/json",
                                 schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ExchangeRate> getExchangeRateBetween(@ParameterObject
                                                               @Valid
                                                               ExchangeRateQuery exchangeRateQuery) {
        return ResponseEntity.ok(exchangeRateService.getLatestExchangeRateBetween(exchangeRateQuery.sourceCurrency(),
                                                                                  exchangeRateQuery.targetCurrency()));
    }
}
