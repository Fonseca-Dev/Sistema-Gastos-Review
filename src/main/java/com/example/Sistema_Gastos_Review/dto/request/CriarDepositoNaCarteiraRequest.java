package com.example.Sistema_Gastos_Review.dto.request;

import com.example.Sistema_Gastos_Review.entity.Carteira;
import com.example.Sistema_Gastos_Review.entity.Conta;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarDepositoNaCarteiraRequest(
        @NotNull BigDecimal valor
) {
}
