package com.ab.banco.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movimientos")
public class BankMovements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origen; // Nombre de la cuenta de origen
    private String destino; // Nombre de la cuenta de destino
    private BigDecimal monto; // Monto transferido
    private LocalDateTime fecha; // Fecha de la transferencia

    @ManyToOne
    @JoinColumn(name = "currency_id")//relacion con la tabla de monedas
    private Currency currency; //la moneda del movimiento bancario

    @ManyToOne
    @JoinColumn(name = "account_id") // Cambia el nombre según tu necesidad
    @JsonIgnore // Evitamos la recursividad al serializar
    private Account account; // Relación con la cuenta
}
