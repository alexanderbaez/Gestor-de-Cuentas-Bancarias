package com.ab.banco.repository;

import com.ab.banco.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByCbu(String cbu); //buscamos una cuenta con el cbu
    Optional<Account> findByAlias(String alias);

}
