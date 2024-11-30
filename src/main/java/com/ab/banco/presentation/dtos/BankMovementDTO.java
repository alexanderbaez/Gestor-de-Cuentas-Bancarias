package com.ab.banco.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankMovementDTO {
    private Long id;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private CurrencyDTO currency;
    private String accountAlias;
    private String userOrigenName;
    private String userDestinoName;
    private String movimientoTipo;//DEPOSITO, RETIRO, TRANSFERENCIA
}
