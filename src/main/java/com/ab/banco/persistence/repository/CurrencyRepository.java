package com.ab.banco.persistence.repository;

import com.ab.banco.persistence.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    Optional<Currency> findByNameOrSymbol(String name,String symbol);
}
