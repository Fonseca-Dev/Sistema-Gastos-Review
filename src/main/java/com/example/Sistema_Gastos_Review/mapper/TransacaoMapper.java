package com.example.Sistema_Gastos_Review.mapper;

import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoNaCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoContaRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarTransferenciaRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarSaqueContaRequest;
import com.example.Sistema_Gastos_Review.dto.response.*;
import com.example.Sistema_Gastos_Review.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransacaoMapper {
    public static Saque toSaqueEntity(CriarSaqueContaRequest request, Conta conta) {
        return Saque.builder()
                .conta(conta)
                .tipo("SAQUE_CONTA")
                .valor(request.valor())
                .data(LocalDateTime.now())
                .build();
    }

    public static void sacar(Conta conta, CriarSaqueContaRequest request) {
        conta.setSaldo(conta.getSaldo().subtract(request.valor()));
    }

    public static CriarSaqueResponse toCriarSaqueResponse(Conta conta, Saque saque) {
        return new CriarSaqueResponse(
                saque.getId(),
                saque.getValor(),
                saque.getData(),
                conta.getId()
        );
    }

    public static Deposito toDepositoEntity(CriarDepositoContaRequest request, Conta conta) {
        return Deposito.builder()
                .conta(conta)
                .tipo("DEPOSITO_CONTA")
                .valor(request.valor())
                .data(LocalDateTime.now())
                .build();
    }

    public static void depositar(Conta conta, CriarDepositoContaRequest request) {
        conta.setSaldo(conta.getSaldo().add(request.valor()));
    }

    public static CriarDepositoResponse toCriarDepositoResponse(Conta conta, Deposito deposito) {
        return new CriarDepositoResponse(
                deposito.getId(),
                deposito.getValor(),
                deposito.getData(),
                conta.getId()
        );
    }

    public static Conta_Carteira toContaCarteiraDepositoEntity(Conta conta, Carteira carteira, CriarDepositoNaCarteiraRequest request) {
        return Conta_Carteira.builder()
                .conta(conta)
                .carteira(carteira)
                .tipo("DEPÓSITO_CARTEIRA")
                .valor(request.valor())
                .data(LocalDateTime.now())
                .build();
    }

    public static Conta_Carteira toContaCarteiraSaqueEntity(Conta conta, Carteira carteira, CriarDepositoNaCarteiraRequest request) {
        return Conta_Carteira.builder()
                .conta(conta)
                .carteira(carteira)
                .tipo("SAQUE_CARTEIRA")
                .valor(request.valor())
                .data(LocalDateTime.now())
                .build();
    }

    public static void depositarNaCarteira(Conta_Carteira contaCarteira) {
        contaCarteira.getConta().setSaldo(contaCarteira.getConta().getSaldo().subtract(contaCarteira.getValor()));
        contaCarteira.getCarteira().setSaldo(contaCarteira.getCarteira().getSaldo().add(contaCarteira.getValor()));
    }

    public static CriarDepositoNaCarteiraResponse toCriarDepositoNaCarteiraResponse(Conta_Carteira contaCarteira) {
        return new CriarDepositoNaCarteiraResponse(
                contaCarteira.getId(),
                contaCarteira.getValor(),
                contaCarteira.getData(),
                contaCarteira.getCarteira().getId(),
                contaCarteira.getConta().getId()
        );
    }

    public static void sacarDaCarteira(Conta_Carteira contaCarteira) {
        contaCarteira.getConta().setSaldo(contaCarteira.getConta().getSaldo().add(contaCarteira.getValor()));
        contaCarteira.getCarteira().setSaldo(contaCarteira.getCarteira().getSaldo().subtract(contaCarteira.getValor()));
    }

    public static CriarSaqueNaCarteiraResponse toCriarSaqueNaCarteiraResponse(Conta_Carteira contaCarteira) {
        return new CriarSaqueNaCarteiraResponse(
                contaCarteira.getId(),
                contaCarteira.getValor(),
                contaCarteira.getData(),
                contaCarteira.getCarteira().getId(),
                contaCarteira.getConta().getId()
        );
    }

    public static Transferencia toTransferenciaEntityInterna(Conta contaOrigem, Conta contaDestino, CriarTransferenciaRequest request) {
        return Transferencia.builder()
                .contaOrigem(contaOrigem)
                .contaDestino(contaDestino)
                .tipo("TRANSFERENCIA_INTERNA")
                .valor(request.valor())
                .categoria(request.categoria())
                .data(LocalDateTime.now())
                .build();
    }

    public static Transferencia toTransferenciaEntityExterna(Conta contaOrigem, Long numeroContaDestinoExterna, CriarTransferenciaRequest request) {
        return Transferencia.builder()
                .contaOrigem(contaOrigem)
                .numeroContaDestino(numeroContaDestinoExterna)
                .tipo("TRANSFERENCIA_EXTERNA")
                .valor(request.valor())
                .categoria(request.categoria())
                .data(LocalDateTime.now())
                .build();
    }

    public static void aplicarTransferenciaInterna(Conta contaOrigem, Conta contaDestino, CriarTransferenciaRequest request) {
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.valor()));
    }

    public static void aplicarTransferenciaExterna(Conta contaOrigem, CriarTransferenciaRequest request) {
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
    }

    public static CriarTransferenciaResponse toCriarTransferenciaResponse(Transferencia pagamento) {
        return new CriarTransferenciaResponse(
                pagamento.getId(),
                pagamento.getValor(),
                pagamento.getData(),
                pagamento.getContaOrigem().getId(),
                pagamento.getContaDestino() != null ? pagamento.getContaDestino().getId() : pagamento.getNumeroContaDestino().toString(),
                pagamento.getCategoria()
        );
    }

    public static List<TransacaoContaResponse> listarTransacoesContaResponse(
            List<Deposito> depositos,
            List<Saque> saques,
            List<Conta_Carteira> contaCarteiraList,
            List<Transferencia> pagamentos
    ) {
        List<TransacaoContaResponse> lista = new ArrayList<>();

        lista.addAll(
                depositos.stream()
                        .map(d -> new TransacaoContaResponse(
                                d.getTipo(),
                                d.getData(),
                                new DepositoResponse(d.getData(), d.getConta().getId(), d.getConta().getNumero(), d.getValor())
                        ))
                        .toList()
        );

        lista.addAll(
                saques.stream()
                        .map(s -> new TransacaoContaResponse(
                                s.getTipo(),
                                s.getData(),
                                new SaqueResponse(s.getData(), s.getConta().getId(), s.getConta().getNumero(), s.getValor().negate())
                        ))
                        .toList()
        );

        lista.addAll(
                contaCarteiraList.stream()
                        .map(cc -> new TransacaoContaResponse(
                                cc.getTipo(),
                                cc.getData(),
                                new ContaCarteiraResponse(cc.getData(), cc.getConta().getId(), cc.getConta().getNumero(), cc.getValor(), cc.getCarteira().getId())
                        ))
                        .toList()
        );

        lista.addAll(
                pagamentos.stream()
                        .map(p -> new TransacaoContaResponse(
                                p.getTipo(),
                                p.getData(),
                                new PagamentoTransferenciaResponse(
                                        p.getData(),
                                        p.getContaOrigem().getId(),
                                        p.getContaOrigem().getNumero(),
                                        p.getValor(),
                                        p.getContaDestino() != null ? p.getContaDestino().getId() : null,
                                        p.getNumeroContaDestino(),
                                        p.getCategoria()
                                )
                        ))
                        .toList()
        );

        lista.sort(Comparator.comparing(TransacaoContaResponse::data));
        return lista;
    }

    public static List<TransacaoCarteiraResponse> listarTransacoesCarteiraResponse(
            List<Conta_Carteira> contaCarteiraList
    ) {
        return new ArrayList<>(
                contaCarteiraList.stream().map(cc -> new TransacaoCarteiraResponse(
                        cc.getConta().getId(),
                        cc.getConta().getNumero(),
                        cc.getCarteira().getId(),
                        cc.getTipo(),
                        cc.getData(),
                        cc.getValor()
                )).toList());
    }
}
