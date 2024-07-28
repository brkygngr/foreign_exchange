package com.brkygngr.foreign_exchange.exchange_conversion.repository;

import com.brkygngr.foreign_exchange.exchange_conversion.model.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface ConversionRepository extends JpaRepository<Conversion, UUID> {

    Page<Conversion> findAllById(UUID id, Pageable pageable);

    Page<Conversion> findAllByCreatedAtBetween(Instant start, Instant end, Pageable pageable);
}
