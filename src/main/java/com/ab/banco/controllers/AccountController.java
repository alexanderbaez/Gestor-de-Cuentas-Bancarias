package com.ab.banco.controllers;

import com.ab.banco.models.Account;
import com.ab.banco.models.TransferRequest;
import com.ab.banco.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/accounts")
public class AccountController {


    @Autowired
    private AccountService accountService;

    //traemos todos las cuentas bancarias
    @CrossOrigin
    @GetMapping
    public List<Account> getAccounts(){
        return accountService.getAllAccounts();
    }

    // Buscamos cuenta bancaria por CBU o Alias
    @CrossOrigin
    @GetMapping(value = "/account")
    public ResponseEntity<Account> getAccount( @RequestParam(required = false) String cbu,
            @RequestParam(required = false) String alias) {

        Account account;//creamos un objeto cuenta

        if (cbu != null) {
            account = accountService.getAccountByCbu(cbu);
        } else if (alias != null) {
            account = accountService.getAccountByAlias(alias);
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(account);
    }

    //Creamos una cuenta bancaria
    @CrossOrigin
    @PostMapping(value = "/account")
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        Account saveAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveAccount);
    }

    //Eliminamos una cuenta bancaria
    @CrossOrigin
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable long id){
        if (!accountService.deleteAccount(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    //Mofidicamoa una cuenta bancaria
    @CrossOrigin
    @PutMapping(value = "/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable long id, @RequestBody Account account){
        Optional<Account> accountOptional = accountService.updateAccount(id, account);
        return accountOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @CrossOrigin
    @PostMapping(value = "/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        accountService.transfer(request.getCbuOrigen(), request.getCbuDestino(), request.getMonto());
        return "Transferencia realizada con éxito.";
    }

}
