package com.example.Sistema_Gastos_Review.dto.response;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record BuscarContasReponse(
        Long numero,
        BigDecimal saldo,
        String tipo,
        String estado
) {
}
