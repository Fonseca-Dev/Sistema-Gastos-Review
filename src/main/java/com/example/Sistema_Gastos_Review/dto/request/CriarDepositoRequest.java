package com.example.Sistema_Gastos_Review.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarDepositoRequest(
        @NotNull BigDecimal valor
) {
}
