package com.brkygngr.foreign_exchange.exchange_conversion.controller;

import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversion;
import com.brkygngr.foreign_exchange.exchange_conversion.dto.ExchangeConversionRequest;
import com.brkygngr.foreign_exchange.exchange_conversion.service.ExchangeConversionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExchangeConversionController {

    private final ExchangeConversionService exchangeConversionService;

    @PostMapping("/conversion")
    public ResponseEntity<ExchangeConversion> getConversion(
            @RequestBody @Valid ExchangeConversionRequest exchangeConversionRequest) {

        ExchangeConversion exchangeConversion = exchangeConversionService.convertAmount(
                exchangeConversionRequest.amount(),
                exchangeConversionRequest.sourceCurrency(),
                exchangeConversionRequest.targetCurrency());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}")
                                                  .buildAndExpand(exchangeConversion.id())
                                                  .toUri();

        return ResponseEntity.created(location)
                             .body(exchangeConversion);
    }
}
