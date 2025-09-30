package com.example.Sistema_Gastos_Review.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@DiscriminatorValue("DEBITO")
public class Deposito extends Transacao {
    //ManyToOne para criação do relacionamento entre as entidades
    //JoinColumn para criação de uma coluna para identificar a chave estrangeira
    @ManyToOne
    @JoinColumn(name = "id_conta",nullable = false)
    private Conta conta;
}
