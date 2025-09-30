package com.example.Sistema_Gastos_Review.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Setter
@Getter
@DiscriminatorValue("SAQUE")
public class Saque extends Transacao {
    //ManyToOne para criação do relacionamento entre as entidades
    //JoinColumn para criação de uma coluna para identificar a chave estrangeira
    @ManyToOne
    @JoinColumn(name = "id_conta",nullable = false)
    private Conta conta;
}
