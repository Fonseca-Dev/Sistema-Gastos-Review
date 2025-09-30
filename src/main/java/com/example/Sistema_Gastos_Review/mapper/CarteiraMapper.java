package com.example.Sistema_Gastos_Review.mapper;

import com.example.Sistema_Gastos_Review.dto.request.AlterarCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.response.AlterarCarteiraResponse;
import com.example.Sistema_Gastos_Review.dto.response.CriarCarteiraResponse;
import com.example.Sistema_Gastos_Review.dto.response.DeletarCarteiraResponse;
import com.example.Sistema_Gastos_Review.entity.Carteira;

public class CarteiraMapper {
    public static Carteira toEntity(CriarCarteiraRequest request){
        return Carteira.builder()
                .nome(request.nome().toUpperCase())
                .descricao(request.descricao())
                .meta(request.meta())
                .saldo(request.saldo())
                .estado("ATIVA")
                .build();
    }

    public static CriarCarteiraResponse toCriarCarteiraResponse(Carteira carteira){
        return new CriarCarteiraResponse(
                carteira.getNome(),
                carteira.getDescricao(),
                carteira.getMeta(),
                carteira.getSaldo()
        );
    }

    public static AlterarCarteiraResponse toAlterarCarteiraResponse(Carteira carteira){
        return new AlterarCarteiraResponse(
                carteira.getNome(),
                carteira.getDescricao(),
                carteira.getMeta(),
                carteira.getSaldo()
        );
    }

    public static void atualizarCarteira(Carteira carteira, AlterarCarteiraRequest request) {
        carteira.setNome(request.nome());
        carteira.setDescricao(request.descricao());
        carteira.setMeta(request.meta());
    }

    public static void deletarCarteira(Carteira carteira) {
        carteira.setEstado("DELETADA");
    }

    public static DeletarCarteiraResponse toDeletarCarteiraResponse(Carteira carteira){
        return new DeletarCarteiraResponse(
                carteira.getNome(),
                carteira.getDescricao(),
                carteira.getEstado()
        );
    }
}
