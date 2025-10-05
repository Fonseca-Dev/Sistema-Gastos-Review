package com.example.Sistema_Gastos_Review.controller;

import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoNaCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoContaRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarTransferenciaRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarSaqueContaRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.service.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{idUsuario}/contas/{idConta}")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/transacoes/saques")
    public ResponseEntity<BaseResponse> criarSaque(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarSaqueContaRequest request){
        BaseResponse response = transacaoService.criarSaque(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/transacoes/depositos")
    public ResponseEntity<BaseResponse> criarDeposito(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarDepositoContaRequest request){
        BaseResponse response = transacaoService.criarDeposito(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/carteiras/{idCarteira}/depositos")
    public ResponseEntity<BaseResponse> criarDepositoNaCarteira(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idCarteira,
            @RequestBody CriarDepositoNaCarteiraRequest request
            ){
        BaseResponse response = transacaoService.criarDepositoNaCarteira(idUsuario, idConta, idCarteira, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/carteiras/{idCarteira}/saques")
    public ResponseEntity<BaseResponse> criarSaqueNaCarteira(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idCarteira,
            @RequestBody CriarDepositoNaCarteiraRequest request
    ){
        BaseResponse response = transacaoService.criarSaqueNaCarteira(idUsuario, idConta, idCarteira, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/transacoes/pagtransfer")
    public ResponseEntity<BaseResponse> criarTransferencia(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarTransferenciaRequest request
            ){
        BaseResponse response = transacaoService.criarTransferencia(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/transacoes/depositos")
    public ResponseEntity<BaseResponse> listarDepositosPorConta(
            @PathVariable String idUsuario,
            @PathVariable String idConta){
        BaseResponse response = transacaoService.listarTransacoesPorConta(idUsuario, idConta);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/carteiras/{idCarteira}/transacoes")
    public ResponseEntity<BaseResponse> listarTransacoesPorCarteira(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idCarteira){
        BaseResponse response = transacaoService.listarTransacoesPorCarteira(idUsuario, idConta, idCarteira);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/transacoes/categorias-mais-usadas")
    public ResponseEntity<BaseResponse> categoriasMaisUsadasPorConta(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestParam int ano,
            @RequestParam int mes){
        BaseResponse response = transacaoService.categoriasMaisUsadasPorContaMesAno(idUsuario, idConta, ano, mes);
        return ResponseEntity.status(response.status()).body(response);
    }
}
