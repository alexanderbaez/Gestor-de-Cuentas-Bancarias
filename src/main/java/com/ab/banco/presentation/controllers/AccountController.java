package com.ab.banco.presentation.controllers;

import com.ab.banco.persistence.models.BankMovements;
import com.ab.banco.presentation.dtos.*;
import com.ab.banco.util.mapper.UserMapper;
import com.ab.banco.persistence.models.Account;

import com.ab.banco.persistence.models.Currency;
import com.ab.banco.persistence.models.User;
import com.ab.banco.service.interfaces.IAccountService;
import com.ab.banco.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/users/{userId}/accounts")
public class AccountController {

    @Autowired
    private IAccountService iAccountService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private UserMapper userMapper;

    //traemos las cuentas de un usuario en especifico
    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts (@PathVariable Long userId){

        List<Account> accounts = iAccountService.getAccountByUser(userId);
        List<AccountDTO> accountDTOS = accounts.stream().map(userMapper::accountToAccountDTO).collect(Collectors.toList());
        return ResponseEntity.ok(accountDTOS);
    }

    //traemos una cuenta especifica de un usuario
    @CrossOrigin
    @GetMapping(value = "/{accountId}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long userId, @PathVariable Long accountId){

        Account account = iAccountService.getAccountByUserAndId(userId,accountId);
        AccountDTO accountDTO = userMapper.accountToAccountDTO(account);
        return ResponseEntity.ok(accountDTO);
    }

    // Crear una nueva cuenta para un usuario
    @CrossOrigin
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@PathVariable Long userId, @RequestBody AccountCreateDTO accountCreateDTO) {
        // Verificamos si el usuario existe
        if (!iUserService.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // Buscar la divisa por ID y asignarla al DTO
        Currency currency = iAccountService.findCurrencyById(accountCreateDTO.getCurrencyId());

        // Mapear el AccountCreateDTO a una entidad Account
        Account account = userMapper.accountCreateDTOToAccount(accountCreateDTO);
        account.setCurrency(currency);  // Establecer la divisa en la cuenta
        account.setUser(new User(userId));  // Establecer el usuario

        // Crear la cuenta
        Account savedAccount = iAccountService.createAccount(account);

        // Mapear la cuenta creada a un AccountDTO
        AccountDTO savedAccountDTO = userMapper.accountToAccountDTO(savedAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccountDTO);
    }


    //modificamos la cuenta de un usuario
    @CrossOrigin
    @PutMapping(value = "/{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long userId, @PathVariable Long accountId, @RequestBody AccountDTO accountDTO){

        Account account = userMapper.accountDTOToAccount(accountDTO);
        Account updateAccount = iAccountService.updateAccount(userId,accountId,account);
        AccountDTO updateAccountDTO = userMapper.accountToAccountDTO(updateAccount);
        return ResponseEntity.ok(updateAccountDTO);
    }

    //eliminamos una cuenta de un usuario
    @CrossOrigin
    @DeleteMapping(value = "/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long userId, @PathVariable Long accountId){
       iAccountService.deleteAccount(userId, accountId);
        return ResponseEntity.noContent().build();
    }

    //realizamos un deposito
    @CrossOrigin
    @PostMapping(value = "/{accountId}/deposit")
    public ResponseEntity<BankMovementDTO> deposit (@PathVariable Long userId, @PathVariable Long accountId, @RequestBody DepositDTO depositDTO){
        if (!iUserService.existsById(userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        BankMovements movement = iAccountService.deposit(accountId, depositDTO.getMonto());

        //convertimos la entidad a dto
       BankMovementDTO movementDTO = userMapper.bankmovementToBankMovementDTO(movement);

        return ResponseEntity.status(HttpStatus.CREATED).body(movementDTO);
    }

    //realizamos un retiro
    @CrossOrigin
    @PostMapping(value = "/{accountId}/withdraw")
    public ResponseEntity<BankMovementDTO> withdraw(@PathVariable Long userId, @PathVariable Long accountId, @RequestBody WithdrawDTO withdrawDTO){
        if (!iUserService.existsById(userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        BankMovements movement = iAccountService.withdraw(accountId, withdrawDTO.getMonto());
        //convertimos de entidad a dto
        BankMovementDTO bankMovementDTO = userMapper.bankmovementToBankMovementDTO(movement);

        return ResponseEntity.status(HttpStatus.CREATED).body(bankMovementDTO);
    }

    //realizamos una transferencia
    @CrossOrigin
    @PostMapping(value = "{sourceAccountId}/transfer/{destinationAccountId}")
    public ResponseEntity<List<BankMovementDTO>> transfer (@PathVariable Long userId, @PathVariable Long sourceAccountId,
                                                           @PathVariable Long destinationAccountId, @RequestBody TransferDTO transferDTO){
        if (!iUserService.existsById(userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<BankMovements> movements = iAccountService.transfer(sourceAccountId,destinationAccountId, transferDTO.getMonto());

        //convertimos de entidad a dtos
        List<BankMovementDTO> bankMovementDTOS = movements.stream().map(userMapper::bankmovementToBankMovementDTO).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(bankMovementDTOS);
    }
}
