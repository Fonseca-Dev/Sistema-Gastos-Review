package com.example.Sistema_Gastos_Review.service;

import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoNaCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoContaRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarTransferenciaRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarSaqueContaRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.dto.response.CategoriaResponse;
import com.example.Sistema_Gastos_Review.dto.response.TransacaoContaResponse;
import com.example.Sistema_Gastos_Review.entity.*;
import com.example.Sistema_Gastos_Review.mapper.TransacaoMapper;
import com.example.Sistema_Gastos_Review.repository.*;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final PagamentoTransferenciaRepository transferenciaRepository;

    public TransacaoService(
            UsuarioRepository usuarioRepository,
            ContaRepository contaRepository,
            CarteiraRepository carteiraRepository,
            TransacaoRepository transacaoRepository,
            DepositoRepository depositoRepository,
            SaqueRepository saqueRepository,
            ContaCarteiraRepository contaCarteiraRepository,
            PagamentoTransferenciaRepository transferenciaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
        this.carteiraRepository = carteiraRepository;
        this.transacaoRepository = transacaoRepository;
        this.depositoRepository = depositoRepository;
        this.saqueRepository = saqueRepository;
        this.contaCarteiraRepository = contaCarteiraRepository;
        this.transferenciaRepository = transferenciaRepository;
    }

    public BaseResponse criarSaque(String idUsuario, String idConta, CriarSaqueContaRequest request) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Saque negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Saque negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Saque negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação se a conta informada pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Saque negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta não esta deletada
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Saque negado! Conta deletada.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do Saque não é maior que o saldo da conta
        if (request.valor().compareTo(conta.getSaldo()) > 0) {
            return new BaseResponse(
                    "Saque negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do saque é invalido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Saque negado! Valor inválido.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Saque saque = TransacaoMapper.toSaqueEntity(request, conta);

        //Mapper que tem o metodo de Saque da Conta
        TransacaoMapper.sacar(conta, request);

        //Aqui eu salvo a transacao
        transacaoRepository.save(saque);

        //Aqui eu salvo a conta com seu saldo atualizado
        contaRepository.save(conta);

        return new BaseResponse(
                "Saque realizado com sucesso.",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarSaqueResponse(conta, saque));
    }

    public BaseResponse criarDeposito(String idUsuario, String idConta, CriarDepositoContaRequest request) {
        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Deposito negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Deposito negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Deposito negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação se a conta informada pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Deposito negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta não esta deletada
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse(
                    "Deposito negado! Conta deletada.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se o valor do saque é invalido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Deposito negado! Valor inválido.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Deposito deposito = TransacaoMapper.toDepositoEntity(request, conta);

        //Mapper que tem o metodo de Deposito da Conta
        TransacaoMapper.depositar(conta, request);

        //Aqui eu salvo a transacao
        transacaoRepository.save(deposito);

        //Aqui eu salvo a conta com seu saldo atualizado
        contaRepository.save(conta);

        return new BaseResponse(
                "Deposito efetuado com sucesso.",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarDepositoResponse(conta, deposito));
    }

    public BaseResponse criarDepositoNaCarteira(String idUsuario, String idConta, String idCarteira, CriarDepositoNaCarteiraRequest request) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Deposito na carteira negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Deposito na carteira negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para ver se a carteira informada é válida
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Deposito na carteira negado! Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Carteira carteira = carteiraEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Deposito na carteira negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação para saber se a carteira pertence a conta informada
        if (!carteira.getConta().getId().equalsIgnoreCase(conta.getId())) {
            return new BaseResponse(
                    "Deposito na carteira negado! Carteira nao pertence a conta informada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Deposito na carteira negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se o valor de deposito é valido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Deposito na carteira negado! Valor inválido.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação para saber se o valor do deposito na carteira é maior que o saldo da conta
        if (request.valor().compareTo(conta.getSaldo()) > 0) {
            return new BaseResponse(
                    "Deposito na carteira negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Conta_Carteira contaCarteira = TransacaoMapper.toContaCarteiraDepositoEntity(conta, carteira, request);

        //Mapper que tem o metodo de Deposito da Carteira
        TransacaoMapper.depositarNaCarteira(contaCarteira);

        //Aqui eu salvo a transacao
        transacaoRepository.save(contaCarteira);

        //Aqui eu salvo a conta com o saldo atualizado
        contaRepository.save(conta);
        return new BaseResponse(

                "Deposito na carteira efetuado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarDepositoNaCarteiraResponse(contaCarteira)
        );
    }

    public BaseResponse criarSaqueNaCarteira(String idUsuario, String idConta, String idCarteira, CriarDepositoNaCarteiraRequest request) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Saque na carteira negado! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Saque na carteira negado! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para ver se a carteira informada é válida
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Saque na carteira negado! Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Carteira carteira = carteiraEncontrada.get();

        //Validação se o request esta nulo
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Saque na carteira negado! Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação para saber se a carteira pertence a conta informada
        if (!carteira.getConta().getId().equalsIgnoreCase(conta.getId())) {
            return new BaseResponse(
                    "Saque na carteira negado! Carteira nao pertence a conta informada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Saque na carteira negado! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se o valor de saque é valido
        if (request.valor().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Saque na carteira negado! Valor inválido.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        //Validação para saber se o valor de saque é maior que o saldo
        if (request.valor().compareTo(carteira.getSaldo()) > 0) {
            return new BaseResponse(
                    "Saque na carteira negado! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Conta_Carteira contaCarteira = TransacaoMapper.toContaCarteiraSaqueEntity(conta, carteira, request);

        //Mapper que tem o metodo de Saque da Carteira
        TransacaoMapper.sacarDaCarteira(contaCarteira);

        //Aqui eu salvo a transacao
        transacaoRepository.save(contaCarteira);

        //Aqui eu salvo a conta com o saldo atualizado
        contaRepository.save(conta);
        return new BaseResponse(
                "Saque na carteira efetuado com sucesso!",
                HttpStatus.CREATED,
                TransacaoMapper.toCriarSaqueNaCarteiraResponse(contaCarteira)
        );
    }

    // A anotação @Transactional garante que todas as operações dentro do metodo
    // aconteçam de forma atômica — ou seja, se der erro no meio, tudo é revertido.
    @Transactional
    public BaseResponse criarTransferencia(String idUsuario, String idConta, CriarTransferenciaRequest request) {
        final int MAX_RETRIES = 3; // Define o número máximo de tentativas em caso de conflito (OptimisticLock)
        int attempts = 0;

        // Loop que tentará repetir a operação até o limite de tentativas
        while (attempts < MAX_RETRIES) {
            try {
                // Chama o metodo que executa de fato a lógica da transferência
                return executarTransferencia(idUsuario, idConta, request);
            } catch (OptimisticLockException e) {
                // Caso duas transações tentem alterar a mesma conta ao mesmo tempo,
                // essa exceção será lançada.
                attempts++;
                if (attempts == MAX_RETRIES) {
                    // Após atingir o número máximo de tentativas, retorna erro de conflito
                    return new BaseResponse(
                            "Falha na transferencia devido a concorrência. Tente novamente.",
                            HttpStatus.CONFLICT,
                            null
                    );
                }
                // Pequena pausa antes de tentar novamente (boa prática para aliviar concorrência)
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        }

        // Caso ocorra algum erro inesperado fora do loop
        return new BaseResponse("Falha desconhecida na transferencia.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }



    private BaseResponse executarTransferencia(String idUsuario, String idConta, CriarTransferenciaRequest request) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if(usuarioEncontrado.isEmpty()){
            return new BaseResponse(
                    "Transferencia negada! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if(contaEncontrada.isEmpty()){
            return new BaseResponse(
                    "Transferencia negada! Conta de origem nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Conta contaOrigem = contaEncontrada.get();

        //Validação para saber se o valor de transferencia é maior que o saldo
        if (request.valor().compareTo(contaOrigem.getSaldo()) > 0) {
            return new BaseResponse(
                    "Transferencia negada! Saldo insuficiente.",
                    HttpStatus.CONFLICT,
                    null);
        }

        //Validação para saber se a tranferência é para um banco externo
        if(!request.bancoDestino().equalsIgnoreCase("BCO AGIBANK S.A.")){

            //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
            Transferencia transferenciaExterna = TransacaoMapper.toTransferenciaEntityExterna(contaOrigem, request.numeroContaDestino(), request);

            //Mapper que tem o metodo de Transferencia Externa
            TransacaoMapper.aplicarTransferenciaExterna(contaOrigem, request);

            //Aqui eu salvo a transacao
            transacaoRepository.save(transferenciaExterna);

            //Aqui eu salvo a conta com o saldo atualizado
            contaRepository.save(contaOrigem);
            return new BaseResponse("Transferencia realizada com sucesso!", HttpStatus.CREATED,
                    TransacaoMapper.toCriarTransferenciaResponse(transferenciaExterna));
        }

        Optional<Conta> contaDestinoEncontrada = contaRepository.findByNumero(request.numeroContaDestino());

        //Validação para saber se a conta destino é válida
        if (contaDestinoEncontrada.isEmpty()) {
            return new BaseResponse("Transferencia negada! Conta destino não encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        // Transferência interna
        Conta contaDestino = contaDestinoEncontrada.get();

        //Validação para saber se a conta de Origem está deletada
        if (contaOrigem.getEstado().equalsIgnoreCase("DELETADA")){
            return new BaseResponse(
                    "Transferencia negada! Conta origem deletada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        //Validação para saber se a conta de Destino está deletada
        if (contaDestino.getEstado().equalsIgnoreCase("DELETADA")){
            return new BaseResponse(
                    "Transferencia negada! Conta destino deletada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Mapper que transforma o requet em um entidade que será salva no Banco de Dados
        Transferencia pagamentoInterno = TransacaoMapper.toTransferenciaEntityInterna(contaOrigem, contaDestino, request);

        //Mapper que tem o metodo de Transferencia Interna
        TransacaoMapper.aplicarTransferenciaInterna(contaOrigem, contaDestino, request);

        //Aqui eu salvo a transacao
        transacaoRepository.save(pagamentoInterno);

        //Aqui eu salvo a conta de origem com o saldo atualizado
        contaRepository.save(contaOrigem);

        //Aqui eu salvo a conta de destino com o saldo atualizado
        contaRepository.save(contaDestino);

        return new BaseResponse("Tranferencia realizada com sucesso!", HttpStatus.CREATED,
                TransacaoMapper.toCriarTransferenciaResponse(pagamentoInterno));
    }


    public BaseResponse listarTransacoesPorConta(String idUsuario, String idConta) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta mao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Aqui eu busco todas as transacoes pelo idConta
        List<Deposito> depositos = depositoRepository.findByContaId(conta.getId());
        List<Saque> saques = saqueRepository.findByContaId(conta.getId());
        List<Conta_Carteira> contaCarteiraList = contaCarteiraRepository.findByContaId(conta.getId());
        List<Transferencia> transferencias = transferenciaRepository.findByContaOrigem_Id(conta.getId());
        List<TransacaoContaResponse> transacoesResponse = TransacaoMapper.listarTransacoesContaResponse(depositos, saques, contaCarteiraList, transferencias);
        return new BaseResponse("Transacoes", HttpStatus.OK, transacoesResponse);
    }

    public BaseResponse listarTransacoesPorCarteira(String idUsuario, String idConta, String idCarteira) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para ver se a carteira informada é válida
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Carteira carteira = carteiraEncontrada.get();

        //Validação para saber se a carteira pertence a conta informada
        if (!carteira.getConta().getId().equalsIgnoreCase(conta.getId())) {
            return new BaseResponse(
                    "Carteira nao pertence a conta informada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Busco todas as transacoes da carteira pelo seu ID
        List<Conta_Carteira> contaCarteiraList = contaCarteiraRepository.findByCarteiraId(carteira.getId());

        //Validação pra ver se a carteira não possui transação
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

    public BaseResponse categoriasMaisUsadasPorContaMesAno(String idUsuario, String idConta, int ano, int mes) {

        //Validação para ver se o usuário informado é válido
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();

        //Validação para ver se a conta informada é válida
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Conta nao encontrada."
                    , HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        //Validação para saber se a conta pertence ao usuario informado
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Conta mao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        //Busco as transacoes da conta pelo seu ID
        List<Transferencia> transacoes = transferenciaRepository.findByContaOrigem_Id(idConta);
        if (transacoes.isEmpty()) {
            return new BaseResponse(
                    "Nenhuma transacao encontrada.",
                    HttpStatus.NOT_FOUND, null
            );
        }

        // Aqui usamos Stream API para filtrar, agrupar e somar os valores das transações.
        List<CategoriaResponse> categoriaResponses = transacoes.stream()
                //Filtra apenas as transações do ano e mês especificados
                .filter(pt -> pt.getData().getYear() == ano && pt.getData().getMonthValue() == mes)
                //Agrupa por categoria e soma os valores
                .collect(Collectors.groupingBy(
                        // Agrupa pela categoria
                        Transferencia::getCategoria,
                        // Pega os valores das transações
                        Collectors.mapping(Transferencia::getValor,
                                // Soma os valores por categoria
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                //Converte o resultado do agrupamento (Map) em uma lista de CategoriaResponse
                .entrySet().stream()
                .map(e -> new CategoriaResponse(e.getKey(), e.getValue()))
                //Ordena as categorias em ordem decrescente de valor (mais gasto → menos gasto)
                .sorted((c1, c2) -> c2.valor().compareTo(c1.valor())) // ordem decrescente
                .toList();

        // Validação final: verifica se há categorias após o filtro
        if (categoriaResponses.isEmpty()) {
            return new BaseResponse(
                    "Nenhuma transacao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }

        //Retorna a lista de categorias mais usadas (ordenadas por valor gasto)
        return new BaseResponse(
                "Categorias encontradas.",
                HttpStatus.OK,
                categoriaResponses
        );
    }
}
