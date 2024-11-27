package com.ab.banco.dtos;

import com.ab.banco.models.Account;
import com.ab.banco.models.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private Long id;
    private String cbu;
    private String alias;
    private BigDecimal balance;
    private Currency currency;

    //constructor para pasar el objeto
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.cbu = account.getCbu();
        this.alias = account.getAlias();
        this.balance = account.getBalance();
        this.currency = account.getCurrency();
    }
}
