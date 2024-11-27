package com.ab.banco.persistence.repository;

import com.ab.banco.persistence.models.BankMovements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankMovementsRepository extends JpaRepository<BankMovements, Long> {
}
