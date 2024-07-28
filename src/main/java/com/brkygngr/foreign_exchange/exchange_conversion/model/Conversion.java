package com.brkygngr.foreign_exchange.exchange_conversion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Conversion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String sourceCurrency;

    @Column(nullable = false)
    private String targetCurrency;

    @Column(nullable = false, precision = 24, scale = 6)
    private BigDecimal amount;

    @Column(nullable = false, precision = 24, scale = 6)
    private BigDecimal exchangeRate;

    @CreationTimestamp
    private Instant createdAt;

    public BigDecimal convertedAmount() {
        return exchangeRate.multiply(amount);
    }
}
