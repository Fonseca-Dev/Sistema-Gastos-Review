package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;

public record CriarCarteiraResponse(
        String nome,
        String descricao,
        BigDecimal meta,
        BigDecimal saldo
) {
}
