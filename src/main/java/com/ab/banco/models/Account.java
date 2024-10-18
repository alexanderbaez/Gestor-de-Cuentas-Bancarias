package com.ab.banco.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cuentas")
public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "nombre_Y_apellido")
        private String firstNameAndLastName;
        private String cbu; // codigo bancario único
        private String alias; //nombre de la cuenta unica
        private BigDecimal balance; // saldo de la cuenta

        @ManyToOne
        @JoinColumn(name = "currency_id")//relacion con la tabla de monedas
        private Currency currency;
        @Column(name = "ultima_actualizacion")
        private LocalDateTime lastUdate; // Última vez que se actualizó el saldo

        @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<BankMovements> movements = new ArrayList<>();
        
    }


