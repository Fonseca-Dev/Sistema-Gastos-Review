package com.example.Sistema_Gastos_Review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarPagTansfRequest(
        @NotNull
        BigDecimal valor,
        @NotBlank
        String categoria,
        @NotBlank
        Long numeroContaDestino
) {
}
