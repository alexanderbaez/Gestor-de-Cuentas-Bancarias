package com.ab.banco.dtos;

import com.ab.banco.models.Account;
import com.ab.banco.models.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDTO {
    private Long id;
    private String name;
    private String symbol;
}
