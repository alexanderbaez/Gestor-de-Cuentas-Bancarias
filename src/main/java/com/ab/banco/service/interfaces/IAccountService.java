package com.ab.banco.service.interfaces;

import com.ab.banco.persistence.models.Account;
import com.ab.banco.persistence.models.BankMovements;
import com.ab.banco.persistence.models.Currency;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IAccountService {
    //traemos las cuentas del usuarios
    public List<Account> getAccountByUser(Long userId);

    //traemos una cuenta especifica de un usuario
    public Account getAccountByUserAndId(Long userId, Long accountId);

    //traemos los movimientos historicos de cada cuenta
    public List<BankMovements> getMovementsByAccount(Long accountId);

    // metodo para crear una nueva cuenta
    public Account createAccount(Account account);

    //buscamos por id
    public Currency findCurrencyById(Long currencyId);

    // Eliminar una cuenta
    public void deleteAccount(Long userId, Long accountId);

    // Modificar una cuenta
    public Account updateAccount(Long userId, Long accountId, Account account);

    //realizar un deposito
    public BankMovements deposit(Long accountId, BigDecimal monto);

    //realizamos un retiro
    public BankMovements withdraw (Long accountId, BigDecimal monto);

    //realizamos transderencia
    public List<BankMovements> transfer(Long  sourceAccountId, Long destinationAccountId, BigDecimal monto);


}
