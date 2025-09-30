package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;

public record AlterarCarteiraResponse(
        String nome,
        String descricao,
        BigDecimal meta,
        BigDecimal saldo
) {
}
