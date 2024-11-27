package com.ab.banco.mappers;

import com.ab.banco.dtos.*;
import com.ab.banco.models.Account;
import com.ab.banco.models.BankMovements;
import com.ab.banco.models.Currency;
import com.ab.banco.models.User;
import com.ab.banco.repository.CurrencyRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")//creamos un bean de Spring para que podamos autoinyectarlo donde lo necesitemos.
public interface UserMapper {

    // Mapear de User a UserDTO
    UserDTO userToUserDTO(User user);

    // Mapear de UserDTO a User
    User userDTOToUser(UserDTO userDTO);

    // Mapear de Account a AccountDTO
    AccountDTO accountToAccountDTO(Account account);

    // Mapear de AccountDTO a Account
    Account accountDTOToAccount(AccountDTO accountDTO);

    // Mapear de Currency a CurrencyDTO
    CurrencyDTO currencyToCurrencyDTO(Currency currency);

    // Mapear de CurrencyDTO a Currency
    Currency currencyDTOToCurrency(CurrencyDTO currencyDTO);

    // Mapear de BankMovement a BankMovementDTO
    BankMovementDTO bankmovementToBankMovementDTO(BankMovements bankMovements);

    // metodo para mapear AccountCreateDTO a Account
    @Mapping(target = "currency", ignore = true)  //no mapeamos la moneda aquí
    @Mapping(target = "user", ignore = true)     //no mapeamos el usuario aquí
    Account accountCreateDTOToAccount(AccountCreateDTO accountCreateDTO);
}
