package com.ab.banco.service.interfaces;

import com.ab.banco.persistence.models.Account;
import com.ab.banco.persistence.models.Currency;

import java.util.List;

public interface IAccountService {
    //traemos las cuentas del usuarios
    public List<Account> getAccountByUser(Long userId);

    //traemos una cuenta especifica de un usuario
    public Account getAccountByUserAndId(Long userId, Long accountId);

    // metodo para crear una nueva cuenta
    public Account createAccount(Account account);


    public Currency findCurrencyById(Long currencyId);


    // Eliminar una cuenta
    public void deleteAccount(Long userId, Long accountId);

    // Modificar una cuenta
    public Account updateAccount(Long userId, Long accountId, Account account);
}
