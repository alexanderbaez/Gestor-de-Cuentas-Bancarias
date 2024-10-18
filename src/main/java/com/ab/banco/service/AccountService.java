package com.ab.banco.service;

import com.ab.banco.models.Account;
import com.ab.banco.models.BankMovements;
import com.ab.banco.repository.AccountRepository;
import com.ab.banco.repository.BankMovementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankMovementsRepository bankMovementsRepository;


    //traemos todas las cuentas bancarias
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }
//----------------------------------------------------------------------------------------------------------

    //Buscamos cuenta bancaria por CBU
    public Account getAccountByCbu(String cbu) {
        Optional<Account> optionalAccount = accountRepository.findByCbu(cbu);

        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la cuenta con CBU " + cbu);
        }
    }
//----------------------------------------------------------------------------------------------------------
    //Buscamos cuenta bancaria por Alias
    public Account getAccountByAlias(String alias) {
        Optional<Account> optionalAccount = accountRepository.findByAlias(alias);

        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la cuenta con CBU " + alias);
        }
    }
//---------------------------------------------------------------------------------------------------------
    //Creamos una cuenta bancaria
    public Account createAccount(Account account){
        return accountRepository.save(account);
    }
//---------------------------------------------------------------------------------------------------------
    //Eliminamos una cuenta bancaria
    public Boolean deleteAccount(long id){
        if (!accountRepository.existsById(id)){
            return false;
        }
        accountRepository.deleteById(id);
        return true;
    }
//---------------------------------------------------------------------------------------------------------
    //Modificamos la cuenta bancaria
    public Optional<Account> updateAccount(long id, Account account){
        if (!accountRepository.existsById(id)){
            return Optional.empty();
        }
        account.setId((long) id);
        return Optional.of(accountRepository.save(account));
    }
//---------------------------------------------------------------------------------------------------------
    //Realizamos la tranferencia
    public boolean transfer(String identificadorOrigen, String identificadorDestino, BigDecimal monto) {
        // Busca las cuentas por su CBU o alias
        Optional<Account> accountOrigen = accountRepository.findByCbu(identificadorOrigen);
        if (accountOrigen.isEmpty()) {
            accountOrigen = accountRepository.findByAlias(identificadorOrigen);
        }

        Optional<Account> accountDestino = accountRepository.findByCbu(identificadorDestino);
        if (accountDestino.isEmpty()) {
            accountDestino = accountRepository.findByAlias(identificadorDestino);
        }

        // Validaciones
        if (accountOrigen.isEmpty() || accountDestino.isEmpty()) {
            throw new RuntimeException("Una de las cuentas no existe.");
        }

        //verificamos que ambas cuentas tengan la misma moneda
        if (!accountOrigen.get().getCurrency().getId().equals(accountDestino.get().getCurrency().getId())){
            throw new RuntimeException("Las cuentas deben estar en la misma moneda para realizar la transferencia.");
        }

        if (accountOrigen.get().getBalance().compareTo(monto) < 0) {
            throw new RuntimeException("Saldo insuficiente en la cuenta de origen.");
        }

        // realizamos la transferencia
        accountOrigen.get().setBalance(accountOrigen.get().getBalance().subtract(monto));
        accountDestino.get().setBalance(accountDestino.get().getBalance().add(monto));

        //actualizamos la ultima actualizacion
        accountOrigen.get().setLastUdate(LocalDateTime.now());
        accountDestino.get().setLastUdate(LocalDateTime.now());

        // guardamos las cuentas actualizadas
        accountRepository.save(accountOrigen.get());
        accountRepository.save(accountDestino.get());

       //registramos los movimientos
        registerMovement(accountOrigen.get(),accountDestino.get(), monto);
        registerMovement(accountDestino.get(), accountOrigen.get(), monto);

        return true;//transferencia realizada exitosamente
    }
//------------------------------------------------------------------------------------------------------------
    //metodo que realiza los registros de movimiento
    private void registerMovement(Account account, Account relatedAccount, BigDecimal monto){
        //creamos un objeto movimiento

        BankMovements movement = new BankMovements();
        movement.setOrigen(account.getFirstNameAndLastName());
        movement.setDestino(relatedAccount.getFirstNameAndLastName());
        movement.setMonto(monto);
        movement.setFecha(LocalDateTime.now());
        movement.setCurrency(account.getCurrency()); //asociamos la moneda
        movement.setAccount(account);//asociamos el movimiento con la cuenta

        //agregamos el movimiento a la lista de movimeintos
        List<BankMovements> movementsList = account.getMovements();
        movementsList.add(movement);
        account.setMovements(movementsList);

        //ahora guardamos los movimientos en la base de datos
        bankMovementsRepository.save(movement);
    }


}
