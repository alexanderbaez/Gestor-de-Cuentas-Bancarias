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
    private BigDecimal monto;
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "currency_id")//relacion con la tabla de monedas
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore // evitamos la recursividad al serializar
    private Account account; // Relación con la cuenta
    @ManyToOne
    @JoinColumn(name = "user_origen_id")//relacion con el usuario
    @JsonIgnore
    private User userOrigen;
    @ManyToOne
    @JoinColumn(name = "user_destino_id")
    @JsonIgnore
    private User userDestino;
}
