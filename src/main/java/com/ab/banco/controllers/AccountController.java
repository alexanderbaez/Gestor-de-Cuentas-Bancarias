package com.ab.banco.controllers;

import com.ab.banco.dtos.AccountCreateDTO;
import com.ab.banco.dtos.AccountDTO;
import com.ab.banco.mappers.UserMapper;
import com.ab.banco.models.Account;

import com.ab.banco.models.Currency;
import com.ab.banco.models.User;
import com.ab.banco.service.AccountService;
import com.ab.banco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/users/{userId}/accounts")
public class AccountController {


    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    //traemos las cuentas de un usuario en especifico
    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts (@PathVariable Long userId){

        List<Account> accounts = accountService.getAccountByUser(userId);
        List<AccountDTO> accountDTOS = accounts.stream().map(userMapper::accountToAccountDTO).collect(Collectors.toList());
        return ResponseEntity.ok(accountDTOS);
    }

    //traemos una cuenta especifica de un usuario
    @CrossOrigin
    @GetMapping(value = "/{accountId}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long userId, @PathVariable Long accountId){

        Account account = accountService.getAccountByUserAndId(userId,accountId);
        AccountDTO accountDTO = userMapper.accountToAccountDTO(account);
        return ResponseEntity.ok(accountDTO);
    }

    // Crear una nueva cuenta para un usuario
    @CrossOrigin
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@PathVariable Long userId, @RequestBody AccountCreateDTO accountCreateDTO) {
        // Verificamos si el usuario existe
        if (!userService.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // Buscar la divisa por ID y asignarla al DTO
        Currency currency = accountService.findCurrencyById(accountCreateDTO.getCurrencyId());

        // Mapear el AccountCreateDTO a una entidad Account
        Account account = userMapper.accountCreateDTOToAccount(accountCreateDTO);
        account.setCurrency(currency);  // Establecer la divisa en la cuenta
        account.setUser(new User(userId));  // Establecer el usuario

        // Crear la cuenta
        Account savedAccount = accountService.createAccount(account);

        // Mapear la cuenta creada a un AccountDTO
        AccountDTO savedAccountDTO = userMapper.accountToAccountDTO(savedAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccountDTO);
    }


    //modificamos la cuenta de un usuario
    @CrossOrigin
    @PutMapping(value = "/{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long userId, @PathVariable Long accountId, @RequestBody AccountDTO accountDTO){

        Account account = userMapper.accountDTOToAccount(accountDTO);
        Account updateAccount = accountService.updateAccount(userId,accountId,account);
        AccountDTO updateAccountDTO = userMapper.accountToAccountDTO(updateAccount);
        return ResponseEntity.ok(updateAccountDTO);
    }

    //eliminamos una cuenta de un usuario
    @CrossOrigin
    @DeleteMapping(value = "/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long userId, @PathVariable Long accountId){
       accountService.deleteAccount(userId, accountId);
        return ResponseEntity.noContent().build();
    }
}
