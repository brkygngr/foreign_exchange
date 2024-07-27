package com.brkygngr.foreign_exchange.fx_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record FXApiMeta(@JsonProperty("last_updated_at") Instant lastUpdatedAt) {

}
