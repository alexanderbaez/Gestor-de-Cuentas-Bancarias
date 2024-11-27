package com.ab.banco.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDTO {
    private String cbu;
    private String alias;
    private BigDecimal balance;
    private Long currencyId;
}
