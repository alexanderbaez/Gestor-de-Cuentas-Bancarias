package com.ab.banco.models;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferRequest {
    private String cbuOrigen;
    private String cbuDestino;
    private BigDecimal monto;
}
