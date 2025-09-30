package com.example.Sistema_Gastos_Review.service;

import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoNaCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarPagTansfRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarSaqueRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.entity.*;
import com.example.Sistema_Gastos_Review.mapper.TransacaoMapper;
import com.example.Sistema_Gastos_Review.repository.CarteiraRepository;
import com.example.Sistema_Gastos_Review.repository.ContaRepository;
import com.example.Sistema_Gastos_Review.repository.TransacaoRepository;
import com.example.Sistema_Gastos_Review.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransacaoService {
    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final CarteiraRepository carteiraRepository;
    private final TransacaoRepository transacaoRepository;

    public TransacaoService(UsuarioRepository usuarioRepository,
                            ContaRepository contaRepository,
                            CarteiraRepository carteiraRepository,
                            TransacaoRepository transacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
        this.carteiraRepository = carteiraRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public BaseResponse criarSaque(String idUsuario, String idConta, CriarSaqueRequest request) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Saque negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Saque negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Saque negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Saque negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Saque negado! Conta deletada.",
                    HttpStatus.CONFLICT,
                    null);
        }
        if (request.valor().compareTo(conta.getSaldo()) > 0) {
            return new BaseResponse(
                    "Saque negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null);
        }
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Saque negado! Valor inválido.",
                    HttpStatus.CONFLICT,
                    null);
        }
        Saque saque = TransacaoMapper.toSaqueEntity(request, conta);
        TransacaoMapper.sacar(conta, request);
        transacaoRepository.save(saque);
        contaRepository.save(conta);

        return new BaseResponse(
                "Saque realizado com sucesso.",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarSaqueResponse(conta, saque));
    }

    public BaseResponse criarDeposito(String idUsuario, String idConta, CriarDepositoRequest request) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Deposito negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Deposito negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Deposito negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Deposito negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Deposito negado! Conta deletada.",
                    HttpStatus.CONFLICT,
                    null);
        }
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Deposito negado! Valor inválido.",
                    HttpStatus.CONFLICT,
                    null);
        }
        Deposito deposito = TransacaoMapper.toDepositoEntity(request, conta);
        TransacaoMapper.depositar(conta, request);
        transacaoRepository.save(deposito);
        contaRepository.save(conta);

        return new BaseResponse(
                "Deposito efetuado com sucesso.",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarDepositoResponse(conta, deposito));
    }

    public BaseResponse criarDepositoNaCarteira(String idUsuario, String idConta, String idCarteira, CriarDepositoNaCarteiraRequest request) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Deposito na carteira negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Deposito na carteira negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Deposito na carteira negado! Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Carteira carteira = carteiraEncontrada.get();
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Deposito na carteira negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
        if (!carteira.getConta().getId().equalsIgnoreCase(conta.getId())) {
            return new BaseResponse(
                    "Deposito na carteira negado! Carteira nao pertence a conta informada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Deposito na carteira negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Deposito na carteira negado! Valor inválido.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
        if (request.valor().compareTo(conta.getSaldo()) > 0) {
            return new BaseResponse(
                    "Deposito na carteira negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        Conta_Carteira contaCarteira = TransacaoMapper.toContaCarteiraEntity(conta, carteira, request);
        TransacaoMapper.depositarNaCarteira(contaCarteira);
        transacaoRepository.save(contaCarteira);
        contaRepository.save(conta);
        return new BaseResponse(

                "Deposito na carteira efetuado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarDepositoNaCarteiraResponse(contaCarteira)
        );
    }

    public BaseResponse criarSaqueNaCarteira(String idUsuario, String idConta, String idCarteira, CriarDepositoNaCarteiraRequest request) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Saque na carteira negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Saque na carteira negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Saque na carteira negado! Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Carteira carteira = carteiraEncontrada.get();
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Saque na carteira negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
        if (!carteira.getConta().getId().equalsIgnoreCase(conta.getId())) {
            return new BaseResponse(
                    "Saque na carteira negado! Carteira nao pertence a conta informada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Saque na carteira negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Saque na carteira negado! Valor inválido.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
        if (request.valor().compareTo(carteira.getSaldo()) > 0) {
            return new BaseResponse(
                    "Saque na carteira negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        Conta_Carteira contaCarteira = TransacaoMapper.toContaCarteiraEntity(conta, carteira, request);
        TransacaoMapper.sacarDaCarteira(contaCarteira);
        transacaoRepository.save(contaCarteira);
        contaRepository.save(conta);
        return new BaseResponse(
                "Saque na carteira efetuado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarSaqueNaCarteiraResponse(contaCarteira)
        );
    }

    public BaseResponse criarPagamentoTransferencia(String idUsuario, String idConta, CriarPagTansfRequest request) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Pagamento negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaOrigemEncontrada = contaRepository.findById(idConta);
        if (contaOrigemEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Pagamento negado! Conta de origem nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Conta contaOrigem = contaOrigemEncontrada.get();
        if (request.valor().compareTo(contaOrigem.getSaldo()) > 0) {
            return new BaseResponse(
                    "Pagamento negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        Optional<Conta> contaDestinoEncontrada = contaRepository.findByNumero(request.numeroContaDestino());
        if (contaDestinoEncontrada.isEmpty()) {
            Pagamento_Transferencia pagamentoTransferenciaExterna = TransacaoMapper.toPagTransfEntityEXterna(contaOrigem, request.numeroContaDestino(), request);
            TransacaoMapper.aplicarTransferenciaExterna(contaOrigem, request);
            transacaoRepository.save(pagamentoTransferenciaExterna);
            contaRepository.save(contaOrigem);
            return new BaseResponse(
                    "Pagamento realizado com sucesso!",
                    HttpStatus.CREATED,
                    TransacaoMapper.toCriarPagamentoTransferenciaResponse(pagamentoTransferenciaExterna)
            );
        }
        Conta contaDestino = contaDestinoEncontrada.get();
        Pagamento_Transferencia pagamentoTransferenciaInterna = TransacaoMapper.toPagTransfEntityInterna(contaOrigem,contaDestino,request);
        TransacaoMapper.aplicarTransferenciaInterna(contaOrigem,contaDestino,request);
        transacaoRepository.save(pagamentoTransferenciaInterna);
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
        return new BaseResponse(
                "Pagamento realizado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarPagamentoTransferenciaResponse(pagamentoTransferenciaInterna)
        );
    }

    public BaseResponse listarTransacoesPorConta(String idConta){
        List<Transacao> transacoes = transacaoRepository.

    }
}
