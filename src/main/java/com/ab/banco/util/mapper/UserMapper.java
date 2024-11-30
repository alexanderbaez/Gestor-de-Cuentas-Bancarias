package com.ab.banco.util.mapper;

import com.ab.banco.persistence.models.Account;
import com.ab.banco.persistence.models.BankMovements;
import com.ab.banco.persistence.models.Currency;
import com.ab.banco.persistence.models.User;
import com.ab.banco.presentation.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")//creamos un bean de Spring para que podamos autoinyectarlo donde lo necesitemos.
public interface UserMapper {

    // mapear de User a UserDTO
    UserDTO userToUserDTO(User user);

    // mapear de UserDTO a User
    User userDTOToUser(UserDTO userDTO);

    // mapear de Account a AccountDTO
    AccountDTO accountToAccountDTO(Account account);

    // mapear de AccountDTO a Account
    Account accountDTOToAccount(AccountDTO accountDTO);

    // mapear de Currency a CurrencyDTO
    CurrencyDTO currencyToCurrencyDTO(Currency currency);

    // mapear de CurrencyDTO a Currency
    Currency currencyDTOToCurrency(CurrencyDTO currencyDTO);

    // mapear de BankMovement a BankMovementDTO
    @Mapping(target = "userOrigenName", source = "userOrigen.name")//mapeamos el nombre del usuario origen
    @Mapping(target = "userDestinoName", source = "userDestino.name")//mapeamos el nombre del usuario destino
    @Mapping(target = "movimientoTipo", source = "tipo")//mapeamos el tipo de movimiento
    @Mapping(target = "accountAlias", expression = "java(bankMovements.getAccount().getAlias() != null && bankMovements.getAccount().getAlias() != null ? " +
            "bankMovements.getAccount().getAlias() : \"\")")//verificamos que el alias no sea null
    BankMovementDTO bankmovementToBankMovementDTO(BankMovements bankMovements);

    // metodo para mapear AccountCreateDTO a Account
    @Mapping(target = "currency", ignore = true)  //no mapeamos la moneda
    @Mapping(target = "user", ignore = true)     //no mapeamos el usuario
    Account accountCreateDTOToAccount(AccountCreateDTO accountCreateDTO);
}
