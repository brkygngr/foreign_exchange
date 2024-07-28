package com.brkygngr.foreign_exchange.exchange_conversion.repository;

import com.brkygngr.foreign_exchange.exchange_conversion.model.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConversionRepository extends JpaRepository<Conversion, UUID> {
}
