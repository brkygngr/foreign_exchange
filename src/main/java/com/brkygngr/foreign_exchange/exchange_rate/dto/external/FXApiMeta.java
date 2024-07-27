package com.brkygngr.foreign_exchange.exchange_rate.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record FXApiMeta(@JsonProperty("last_updated_at") Instant lastUpdatedAt) {

}
