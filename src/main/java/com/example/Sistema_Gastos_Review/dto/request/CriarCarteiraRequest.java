package com.example.Sistema_Gastos_Review.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarCarteiraRequest(
        @NotBlank String nome,
        @NotBlank String descricao,
        @NotNull BigDecimal meta,
        @NotNull BigDecimal saldo
) {
}
