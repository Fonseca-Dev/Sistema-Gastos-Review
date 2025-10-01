package com.example.Sistema_Gastos_Review.service;

import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoNaCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarPagTansfRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarSaqueRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.dto.response.CategoriaResponse;
import com.example.Sistema_Gastos_Review.dto.response.TransacaoContaResponse;
import com.example.Sistema_Gastos_Review.entity.*;
import com.example.Sistema_Gastos_Review.mapper.TransacaoMapper;
import com.example.Sistema_Gastos_Review.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransacaoService {
    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final CarteiraRepository carteiraRepository;
    private final TransacaoRepository transacaoRepository;
    private final DepositoRepository depositoRepository;
    private final SaqueRepository saqueRepository;
    private final ContaCarteiraRepository contaCarteiraRepository;
    private final PagamentoTransferenciaRepository pagamentoTransferenciaRepository;

    public TransacaoService(
            UsuarioRepository usuarioRepository,
            ContaRepository contaRepository,
            CarteiraRepository carteiraRepository,
            TransacaoRepository transacaoRepository,
            DepositoRepository depositoRepository,
            SaqueRepository saqueRepository,
            ContaCarteiraRepository contaCarteiraRepository,
            PagamentoTransferenciaRepository pagamentoTransferenciaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
        this.carteiraRepository = carteiraRepository;
        this.transacaoRepository = transacaoRepository;
        this.depositoRepository = depositoRepository;
        this.saqueRepository = saqueRepository;
        this.contaCarteiraRepository = contaCarteiraRepository;
        this.pagamentoTransferenciaRepository = pagamentoTransferenciaRepository;
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
        Conta_Carteira contaCarteira = TransacaoMapper.toContaCarteiraDepositoEntity(conta, carteira, request);
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
        Conta_Carteira contaCarteira = TransacaoMapper.toContaCarteiraSaqueEntity(conta, carteira, request);
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
        Pagamento_Transferencia pagamentoTransferenciaInterna = TransacaoMapper.toPagTransfEntityInterna(contaOrigem, contaDestino, request);
        TransacaoMapper.aplicarTransferenciaInterna(contaOrigem, contaDestino, request);
        transacaoRepository.save(pagamentoTransferenciaInterna);
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);
        return new BaseResponse(
                "Pagamento realizado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarPagamentoTransferenciaResponse(pagamentoTransferenciaInterna)
        );
    }

    public BaseResponse listarTransacoesPorConta(String idUsuario, String idConta) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta mao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        List<Deposito> depositos = depositoRepository.findByContaId(conta.getId());
        List<Saque> saques = saqueRepository.findByContaId(conta.getId());
        List<Conta_Carteira> contaCarteiraList = contaCarteiraRepository.findByContaId(conta.getId());
        List<Pagamento_Transferencia> pagamentoTransferenciaList = pagamentoTransferenciaRepository.findByContaOrigem_Id(conta.getId());
        List<TransacaoContaResponse> transacoesResponse = TransacaoMapper.listarTransacoesContaResponse(depositos, saques, contaCarteiraList, pagamentoTransferenciaList);
        return new BaseResponse("Transacoes", HttpStatus.OK, transacoesResponse);
    }

    public BaseResponse listarTransacoesPorCarteira(String idUsuario, String idConta, String idCarteira) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Carteira carteira = carteiraEncontrada.get();
        if (!carteira.getConta().getId().equalsIgnoreCase(conta.getId())) {
            return new BaseResponse(
                    "Carteira nao pertence a conta informada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        List<Conta_Carteira> contaCarteiraList = contaCarteiraRepository.findByCarteiraId(carteira.getId());
        if (contaCarteiraList.isEmpty()) {
            return new BaseResponse(
                    "Nenhuma transacao encontrada na carteira informada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        return new BaseResponse(
                "Transacoes encontradas.",
                HttpStatus.OK,
                TransacaoMapper.listarTransacoesCarteiraResponse(contaCarteiraList)
        );
    }

    public BaseResponse categoriasMaisUsadasPorConta(String idUsuario, String idConta, int ano, int mes) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta mao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        List<Pagamento_Transferencia> transacoes = pagamentoTransferenciaRepository.findByContaOrigem_Id(idConta);
        if (transacoes.isEmpty()) {
            return new BaseResponse(
                    "Nenhuma transacao encontrada.",
                    HttpStatus.NOT_FOUND, null
            );
        }

        List<CategoriaResponse> categoriaResponses = transacoes.stream()
                .filter(pt -> pt.getData().getYear() == ano && pt.getData().getMonthValue() == mes)
                .collect(Collectors.groupingBy(
                        Pagamento_Transferencia::getCategoria,
                        Collectors.mapping(Pagamento_Transferencia::getValor,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet().stream()
                .map(e -> new CategoriaResponse(e.getKey(), e.getValue()))
                .sorted((c1, c2) -> c2.valor().compareTo(c1.valor())) // ordem decrescente
                .toList();

        if (categoriaResponses.isEmpty()) {
            return new BaseResponse(
                    "Nenhuma transacao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        return new BaseResponse(
                "Categorias encontradas.",
                HttpStatus.OK,
                categoriaResponses
        );
    }
}
