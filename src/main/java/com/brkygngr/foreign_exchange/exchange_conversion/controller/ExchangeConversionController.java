package com.brkygngr.foreign_exchange.exchange_conversion.controller;

import com.brkygngr.foreign_exchange.exception.ErrorResponse;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ConversionHistoryQuery;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversionRequest;
import com.brkygngr.foreign_exchange.exchange_conversion.service.ExchangeConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/conversions")
@RequiredArgsConstructor
public class ExchangeConversionController {

    private final ExchangeConversionService exchangeConversionService;

    @PostMapping
    @Operation(summary = "Converts given amount from source to target currency")
    @ApiResponses(value = {@ApiResponse(responseCode = "201",
                                        description = "Successful response",
                                        content = @Content(mediaType = "application/json",
                                                           schema = @Schema(implementation = ExchangeConversion.class)),
                                        headers = @Header(name = "Location",
                                                          description = "URL of the created conversion transaction")),
                           @ApiResponse(responseCode = "400",
                                        description = "Validation errors",
                                        content = @Content(mediaType = "application/json",
                                                           schema = @Schema(implementation = ErrorResponse.class))),
                           @ApiResponse(responseCode = "422",
                                        description = "Currency invalid errors",
                                        content = @Content(mediaType = "application/json",
                                                           schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<ExchangeConversion> postConversion(
            @RequestBody @Valid ExchangeConversionRequest exchangeConversionRequest) {

        ExchangeConversion exchangeConversion = exchangeConversionService.convertAmount(exchangeConversionRequest.amount(),
                                                                                        exchangeConversionRequest.sourceCurrency(),
                                                                                        exchangeConversionRequest.targetCurrency());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}")
                                                  .buildAndExpand(exchangeConversion.id())
                                                  .toUri();

        return ResponseEntity.created(location).body(exchangeConversion);
    }

    @GetMapping("/history")
    @Operation(summary = "Returns conversion history(es) by id or given date")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
                                        description = "Successful response",
                                        content = @Content(mediaType = "application/json",
                                                           schema = @Schema(implementation = ExchangeConversion[].class)),
                                        headers = @Header(name = "Location",
                                                          description = "URL of the created conversion transaction")),
                           @ApiResponse(responseCode = "400",
                                        description = "Validation errors",
                                        content = @Content(mediaType = "application/json",
                                                           schema = @Schema(implementation = ErrorResponse.class))),
                           @ApiResponse(responseCode = "422",
                                        description = "Currency invalid errors",
                                        content = @Content(mediaType = "application/json",
                                                           schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<List<ExchangeConversion>> getConversionHistory(
            @ParameterObject @Valid ConversionHistoryQuery conversionHistoryQuery) {
        return null;
    }
}
