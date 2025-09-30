package com.example.Sistema_Gastos_Review.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carteira {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank
    private String nome;
    @NotBlank
    private String descricao;
    @NotNull
    private BigDecimal meta;
    @NotNull
    private BigDecimal saldo;
    @NotBlank
    private String estado;

    @OneToMany(mappedBy = "carteira")
    @JsonIgnoreProperties("carteira")
    List<Conta_Carteira> transacoes;

    @ManyToOne
    @JoinColumn(name = "id_conta")
    Conta conta;
}
