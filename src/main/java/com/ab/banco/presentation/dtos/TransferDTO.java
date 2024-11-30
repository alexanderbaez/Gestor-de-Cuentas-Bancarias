package com.ab.banco.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    private BigDecimal monto;
    private Long sourceAccountId;
    private Long destinationAccountId;
}
