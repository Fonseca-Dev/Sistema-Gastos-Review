package com.example.Sistema_Gastos_Review.mapper;

import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoNaCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarPagTansfRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarSaqueRequest;
import com.example.Sistema_Gastos_Review.dto.response.*;
import com.example.Sistema_Gastos_Review.entity.*;

import java.time.LocalDateTime;

public class TransacaoMapper {
    public static Saque toSaqueEntity(CriarSaqueRequest request, Conta conta) {
        return Saque.builder()
                .conta(conta)
                .tipo("SAQUE")
                .valor(request.valor())
                .data(LocalDateTime.now())
                .build();
    }

    public static void sacar(Conta conta, CriarSaqueRequest request) {
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

    public static Deposito toDepositoEntity(CriarDepositoRequest request, Conta conta){
        return Deposito.builder()
                .conta(conta)
                .tipo("DEPOSITO")
                .valor(request.valor())
                .data(LocalDateTime.now())
                .build();
    }

    public static void depositar(Conta conta, CriarDepositoRequest request) {
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

    public static Conta_Carteira toContaCarteiraEntity (Conta conta, Carteira carteira, CriarDepositoNaCarteiraRequest request){
        return Conta_Carteira.builder()
                .conta(conta)
                .carteira(carteira)
                .tipo("DEPÓSITO_CARTEIRA")
                .valor(request.valor())
                .data(LocalDateTime.now())
                .build();
    }

    public static void depositarNaCarteira (Conta_Carteira contaCarteira){
        contaCarteira.getConta().setSaldo(contaCarteira.getConta().getSaldo().subtract(contaCarteira.getValor()));
        contaCarteira.getCarteira().setSaldo(contaCarteira.getCarteira().getSaldo().add(contaCarteira.getValor()));
    }

    public static CriarDepositoNaCarteiraResponse toCriarDepositoNaCarteiraResponse (Conta_Carteira contaCarteira){
        return new CriarDepositoNaCarteiraResponse(
                contaCarteira.getId(),
                contaCarteira.getValor(),
                contaCarteira.getData(),
                contaCarteira.getCarteira().getId(),
                contaCarteira.getConta().getId()
        );
    }

    public static void sacarDaCarteira (Conta_Carteira contaCarteira){
        contaCarteira.getConta().setSaldo(contaCarteira.getConta().getSaldo().add(contaCarteira.getValor()));
        contaCarteira.getCarteira().setSaldo(contaCarteira.getCarteira().getSaldo().subtract(contaCarteira.getValor()));
    }

    public static CriarSaqueNaCarteiraResponse toCriarSaqueNaCarteiraResponse (Conta_Carteira contaCarteira){
        return new CriarSaqueNaCarteiraResponse(
                contaCarteira.getId(),
                contaCarteira.getValor(),
                contaCarteira.getData(),
                contaCarteira.getCarteira().getId(),
                contaCarteira.getConta().getId()
        );
    }

    public static Pagamento_Transferencia toPagTransfEntityInterna(Conta contaOrigem, Conta contaDestino, CriarPagTansfRequest request){
        return Pagamento_Transferencia.builder()
                .contaOrigem(contaOrigem)
                .contaDestino(contaDestino)
                .tipo("PAGAMENTO_TRANSFERENCIA_INTERNA")
                .valor(request.valor())
                .categoria(request.categoria())
                .data(LocalDateTime.now())
                .build();
    }

    public static Pagamento_Transferencia toPagTransfEntityEXterna(Conta contaOrigem, Long numeroContaDestinoExterna, CriarPagTansfRequest request){
        return Pagamento_Transferencia.builder()
                .contaOrigem(contaOrigem)
                .numeroContaDestino(numeroContaDestinoExterna)
                .tipo("PAGAMENTO_TRANSFERENCIA_INTERNA")
                .valor(request.valor())
                .categoria(request.categoria())
                .data(LocalDateTime.now())
                .build();
    }

    public static void aplicarTransferenciaInterna(Conta contaOrigem, Conta contaDestino, CriarPagTansfRequest request) {
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.valor()));
    }

    public static void aplicarTransferenciaExterna(Conta contaOrigem, CriarPagTansfRequest request) {
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
    }

    public static CriarPagamentoTransferenciaResponse toCriarPagamentoTransferenciaResponse(Pagamento_Transferencia pagamento) {
        return new CriarPagamentoTransferenciaResponse(
                pagamento.getId(),
                pagamento.getValor(),
                pagamento.getData(),
                pagamento.getContaOrigem().getId(),
                pagamento.getContaDestino() != null ? pagamento.getContaDestino().getId() : pagamento.getNumeroContaDestino().toString(),
                pagamento.getCategoria()
        );
    }

}
