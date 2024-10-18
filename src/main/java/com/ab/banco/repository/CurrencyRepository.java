package com.ab.banco.repository;

import com.ab.banco.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    Optional<Currency> findByNameOrSymbol(String name,String symbol);
}
