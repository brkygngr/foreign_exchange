package com.brkygngr.foreign_exchange.exchange_rate.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class FXApiMeta {

    @JsonProperty("last_updated_at")
    private Instant lastUpdatedAt;
}
