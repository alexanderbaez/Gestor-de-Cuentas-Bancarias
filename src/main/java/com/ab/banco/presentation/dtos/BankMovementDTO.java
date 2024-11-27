package com.ab.banco.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BankMovementDTO {
    private Long id;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private CurrencyDTO currency;
    private String accountAlias;
    private String userOrigenName;
    private String userDestinoName;
}
