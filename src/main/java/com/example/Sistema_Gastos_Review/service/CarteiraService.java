package com.example.Sistema_Gastos_Review.service;

import com.example.Sistema_Gastos_Review.dto.request.AlterarCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.entity.Carteira;
import com.example.Sistema_Gastos_Review.entity.Conta;
import com.example.Sistema_Gastos_Review.entity.Usuario;
import com.example.Sistema_Gastos_Review.mapper.CarteiraMapper;
import com.example.Sistema_Gastos_Review.mapper.ContaMapper;
import com.example.Sistema_Gastos_Review.repository.CarteiraRepository;
import com.example.Sistema_Gastos_Review.repository.ContaRepository;
import com.example.Sistema_Gastos_Review.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarteiraService {

    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final CarteiraRepository carteiraRepository;


    public CarteiraService(UsuarioRepository usuarioRepository, ContaRepository contaRepository, CarteiraRepository carteiraRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
        this.carteiraRepository = carteiraRepository;
    }

    public BaseResponse criarCarteira(String idUsuario, String idConta, CriarCarteiraRequest request) {
        if (Objects.isNull(request)) {
            return new BaseResponse(
                    "Request esta nulo.",
                    HttpStatus.BAD_REQUEST,
                    null);
        }
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if(usuarioEncontrado.isEmpty()){
            return new BaseResponse(
                    "Criacao de carteira negada! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Criacao de carteira negada! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        if (!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())) {
            return new BaseResponse(
                    "Criacao de carteira negada! Conta não pertence ao usuário informado.",
                    HttpStatus.FORBIDDEN,
                    null);
        }
        if (conta.getEstado().equalsIgnoreCase("DELETADA")) {
            return new BaseResponse("Criacao de carteira negada! Conta deletada.",
                    HttpStatus.CONFLICT,
                    ContaMapper.toDeletarContaResponse(conta));
        }
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findByContaIdAndNome(idConta, request.nome().toUpperCase());
        if (carteiraEncontrada.isPresent()) {
            return new BaseResponse(
                    "Criacao de carteira negada! Carteira ja criada com esse nome.",
                    HttpStatus.CONFLICT,
                    CarteiraMapper.toCriarCarteiraResponse(carteiraEncontrada.get()));
        }
        if (request.saldo() == null || request.saldo().compareTo(BigDecimal.ZERO) < 0) {
            return new BaseResponse(
                    "Criacao de carteira negada! Saldo inicial da carteira invalido.",
                    HttpStatus.BAD_REQUEST,
                    null);
        }
        if (request.meta() == null || request.meta().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Criacao de carteira negada! Meta inicial da carteira deve ser maior que zero.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
        if (request.meta().compareTo(request.saldo()) <= 0) {
            return new BaseResponse(
                    "Criacao de carteira negada! Meta da carteira deve ser maior que o saldo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        if (request.saldo().compareTo(contaEncontrada.get().getSaldo()) > 0) {
            return new BaseResponse(
                    "Criacao de carteira negada! Saldo inicial da carteira maior que o saldo disponivel na conta.",
                    HttpStatus.CONFLICT,
                    null);
        }

        Carteira nova = CarteiraMapper.toEntity(request);
        nova.setConta(contaEncontrada.get());
        contaEncontrada.get().setSaldo(contaEncontrada.get().getSaldo().subtract(request.saldo()));
        contaRepository.save(contaEncontrada.get());
        carteiraRepository.save(nova);
        return new BaseResponse("Carteira criada com sucesso.",
                HttpStatus.CREATED,
                CarteiraMapper.toCriarCarteiraResponse(nova));
    }

    public BaseResponse alterarCarteira(String idUsuario, String idConta, String idCarteira, AlterarCarteiraRequest request) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()){
            return new BaseResponse(
                    "Alteracao de carteira negada! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if(contaEncontrada.isEmpty()){
            return new BaseResponse(
                    "Alteracao de carteira negada! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Alteracao de carteira negada! Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Carteira carteira = carteiraEncontrada.get();
        if(!carteira.getConta().getId().equalsIgnoreCase(conta.getId())){
            return new BaseResponse(
                    "Alteracao de carteira negada! Carteira nao pertence a conta informada.",
                    HttpStatus.FORBIDDEN,
                    null);
        }
        if(!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())){
            return new BaseResponse(
                    "Alteracao de carteira negada! Conta nao pertence ao usuario informado.",
                    HttpStatus.FORBIDDEN,
                    null);
        }
        if (request.meta() == null || request.meta().compareTo(BigDecimal.ZERO) <= 0) {
            return new BaseResponse(
                    "Alteracao de carteira negada! Meta da carteira deve ser maior que zero.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
        if (request.meta().compareTo(carteira.getSaldo()) <= 0) {
            return new BaseResponse(
                    "Alteracao de carteira negada! Meta da carteira deve ser maior que o saldo.",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
        CarteiraMapper.atualizarCarteira(carteira, request);
        carteiraRepository.save(carteira);
        return new BaseResponse(
                "Carteira alterada com sucesso.",
                HttpStatus.OK, CarteiraMapper.toAlterarCarteiraResponse(carteira));
    }

    public BaseResponse deletarCarteira(String idUsuario, String idConta, String idCarteira) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if (usuarioEncontrado.isEmpty()){
            return new BaseResponse(
                    "Delecao de carteira negada! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if(contaEncontrada.isEmpty()){
            return new BaseResponse(
                    "Delecao de carteira negada! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        Optional<Carteira> carteiraEncontrada = carteiraRepository.findById(idCarteira);
        if (carteiraEncontrada.isEmpty()) {
            return new BaseResponse(
                    "Delecao de carteira negada! Carteira nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Carteira carteira = carteiraEncontrada.get();
        if(!carteira.getConta().getId().equalsIgnoreCase(conta.getId())){
            return new BaseResponse(
                    "Delecao de carteira negada! Carteira nao pertence a conta informada.",
                    HttpStatus.FORBIDDEN,
                    null);
        }
        if(!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())){
            return new BaseResponse(
                    "Delecao de carteira negada! Conta nao pertence ao usuario informado.",
                    HttpStatus.FORBIDDEN,
                    null);
        }
        CarteiraMapper.deletarCarteira(carteira);
        carteiraRepository.save(carteira);
        return new BaseResponse(
                "Carteira deletada com sucesso.",
                HttpStatus.OK,
                CarteiraMapper.toDeletarCarteiraResponse(carteira));
    }
}
