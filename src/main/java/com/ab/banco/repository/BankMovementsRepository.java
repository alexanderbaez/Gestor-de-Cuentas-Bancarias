package com.ab.banco.repository;

import com.ab.banco.models.BankMovements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankMovementsRepository extends JpaRepository<BankMovements, Long> {
}
