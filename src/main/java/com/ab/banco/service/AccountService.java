package com.ab.banco.service;

import com.ab.banco.dtos.AccountCreateDTO;
import com.ab.banco.models.Account;
import com.ab.banco.models.BankMovements;
import com.ab.banco.models.Currency;
import com.ab.banco.models.User;
import com.ab.banco.repository.AccountRepository;
import com.ab.banco.repository.BankMovementsRepository;
import com.ab.banco.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BankMovementsRepository bankMovementsRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CurrencyRepository currencyRepository;


    //traemos las cuentas del usuarios
    public List<Account> getAccountByUser(Long userId){

        if (!userService.existsById(userId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con Id: "+ userId);
        }
        List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con Id "+ userId + " no tiene cuentas asociadas");
        }
        return accounts;
    }

    //traemos una cuenta especifica de un usuario
    public Account getAccountByUserAndId(Long userId, Long accountId){
        if (!userService.existsById(userId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con Id: "+userId);
        }
        return accountRepository.findByIdAndUserId(accountId,userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta con Id "+accountId+" no fue encontrada"));
    }

    // metodo para crear una nueva cuenta
    public Account createAccount(Account account) {
        // Verificamos el usuario
        if (!userService.existsById(account.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con Id: "+ account.getUser().getId());
        }
        // Verificamos la divisa
        Long currencyId = account.getCurrency().getId();
        Optional<Currency> currencyOpt = currencyRepository.findById(currencyId);

        if (!currencyOpt.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Divisa no encontrada con Id: "+ currencyId);
        }

        // Asignamos el usuario y la divisa de la cuenta
        account.setCurrency(currencyOpt.get());
        account.setUser(new User(account.getUser().getId()));

        // Guardamos
        return accountRepository.save(account);
    }


    public Currency findCurrencyById(Long currencyId) {
        Optional<Currency> currencyOpt = currencyRepository.findById(currencyId);
        return currencyOpt.orElse(null);  // Retorna null si no se encuentra la divisa
    }


    // Eliminar una cuenta
    public void deleteAccount(Long userId, Long accountId) {
        if (!userService.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + userId);
        }
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta con ID " + accountId + " no fue encontrada"));
        accountRepository.delete(account); // Eliminamos la cuenta
    }

    // Modificar una cuenta
    public Account updateAccount(Long userId, Long accountId, Account account) {
        if (!userService.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + userId);
        }
        Account existingAccount = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta con ID " + accountId + " no fue encontrada"));

        existingAccount.setBalance(account.getBalance()); // actualizamos el balance
        existingAccount.setCurrency(account.getCurrency()); // actualizamos la divisa (si es necesario)

        return accountRepository.save(existingAccount);
    }
}
