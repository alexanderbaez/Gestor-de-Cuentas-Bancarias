package com.ab.banco.service.implementation;

import com.ab.banco.persistence.models.Account;
import com.ab.banco.persistence.models.BankMovements;
import com.ab.banco.persistence.models.Currency;
import com.ab.banco.persistence.models.User;
import com.ab.banco.persistence.repository.AccountRepository;
import com.ab.banco.persistence.repository.BankMovementsRepository;
import com.ab.banco.persistence.repository.CurrencyRepository;
import com.ab.banco.service.interfaces.IAccountService;
import com.ab.banco.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BankMovementsRepository bankMovementsRepository;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private CurrencyRepository currencyRepository;


    //traemos las cuentas del usuarios
    public List<Account> getAccountByUser(Long userId){

        if (!iUserService.existsById(userId)){
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
        if (!iUserService.existsById(userId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con Id: "+userId);
        }
        return accountRepository.findByIdAndUserId(accountId,userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta con Id "+accountId+" no fue encontrada"));
    }

    //traemos los movimientos historicos de una cuenta bancaria
    public List<BankMovements> getMovementsByAccount(Long accountId){

        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

        List<BankMovements> movements = bankMovementsRepository.findByAccountId(accountId);
        if (movements.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta con Id: "+accountId+" no tiene movimientos historicos");
        }
        return movements;
    }

    // metodo para crear una nueva cuenta
    public Account createAccount(Account account) {
        // Verificamos el usuario
        if (!iUserService.existsById(account.getUser().getId())) {
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
        return currencyOpt.orElse(null);  // retorna null si no se encuentra la divisa
    }

    // Eliminar una cuenta
    public void deleteAccount(Long userId, Long accountId) {
        if (!iUserService.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + userId);
        }
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta con ID " + accountId + " no fue encontrada"));
        accountRepository.delete(account); // rliminamos la cuenta
    }

    // Modificar una cuenta
    public Account updateAccount(Long userId, Long accountId, Account account) {
        if (!iUserService.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + userId);
        }
        Account existingAccount = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La cuenta con ID " + accountId + " no fue encontrada"));

        existingAccount.setBalance(account.getBalance()); // actualizamos el balance
        existingAccount.setCurrency(account.getCurrency()); // actualizamos la divisa (si es necesario)

        return accountRepository.save(existingAccount);
    }

    //realizar un deposito
    public BankMovements deposit(Long accountId, BigDecimal monto){
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

        //actualizamos saldo
        account.setBalance(account.getBalance().add(monto));
        accountRepository.save(account);

        //registamos el movimiento del deposito
        BankMovements movement = new BankMovements();

        movement.setMonto(monto);
        movement.setFecha(LocalDateTime.now());
        movement.setAccount(account);
        movement.setUserOrigen(account.getUser());
        movement.setUserDestino(null);
        movement.setCurrency(account.getCurrency());
        movement.setTipo(BankMovements.MovimientoTipo.DEPOSITO);

        return bankMovementsRepository.save(movement);
    }

    //realizamos un retiro
    public BankMovements withdraw (Long accountId, BigDecimal monto){
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

        //verificamos el saldo de la cuenta
        if (account.getBalance().compareTo(monto)<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para realizar el retiro");
        }
        //actualizamos saldo
        account.setBalance(account.getBalance().subtract(monto));
        accountRepository.save(account);

        //registramos el movimiento del retiro
        BankMovements movement = new BankMovements();
        movement.setMonto(monto.negate());
        movement.setFecha(LocalDateTime.now());
        movement.setAccount(account);
        movement.setUserOrigen(account.getUser());
        movement.setUserDestino(null);
        movement.setCurrency(account.getCurrency());
        movement.setTipo(BankMovements.MovimientoTipo.RETIRO);

        return bankMovementsRepository.save(movement);
    }

    //realizamos transderencia
    public List<BankMovements> transfer(Long  sourceAccountId, Long destinationAccountId, BigDecimal monto){
        Account sourceAccount = accountRepository.findById(sourceAccountId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta de origen no encontrada"));
        Account destinationAccount = accountRepository.findById(destinationAccountId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta de destino no encontrada"));

        //vamos a verificar si la cuenta origen tiene saldo para realizar la transferencia
        if (sourceAccount.getBalance().compareTo(monto)<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Saldo insuficiente para realizar la transferencia");
        }

        //actualizamos el saldo de las cuentas
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(monto));
        destinationAccount.setBalance(destinationAccount.getBalance().add(monto));

        //guardamos
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        //registramoe el movimiento de la transferencia para la cuenta origen
        BankMovements movementFromSource = new BankMovements();
        movementFromSource.setMonto(monto.negate());
        movementFromSource.setFecha(LocalDateTime.now());
        movementFromSource.setAccount(sourceAccount);
        movementFromSource.setUserOrigen(sourceAccount.getUser());
        movementFromSource.setUserDestino(destinationAccount.getUser());
        movementFromSource.setCurrency(sourceAccount.getCurrency());
        movementFromSource.setTipo(BankMovements.MovimientoTipo.TRANSFERENCIA);

        //registramos el movimineto de la transferencia para la cuenta destino
        BankMovements movementToDestination = new BankMovements();
        movementToDestination.setMonto(monto);
        movementToDestination.setFecha(LocalDateTime.now());
        movementToDestination.setAccount(destinationAccount);
        movementToDestination.setUserOrigen(sourceAccount.getUser());
        movementToDestination.setUserDestino(destinationAccount.getUser());
        movementToDestination.setCurrency(destinationAccount.getCurrency());
        movementToDestination.setTipo(BankMovements.MovimientoTipo.TRANSFERENCIA);

        //guardamos los movimientos en la base de datos
        bankMovementsRepository.save(movementFromSource);
        bankMovementsRepository.save(movementToDestination);

        //devolvemos la lista de movimiento
        return List.of(movementFromSource,movementToDestination);
    }
}
