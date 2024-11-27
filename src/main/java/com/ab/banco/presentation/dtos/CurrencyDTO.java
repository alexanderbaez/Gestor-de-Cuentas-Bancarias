package com.ab.banco.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDTO {
    private Long id;
    private String name;
    private String symbol;
}
