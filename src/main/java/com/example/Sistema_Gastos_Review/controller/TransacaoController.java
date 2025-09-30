package com.example.Sistema_Gastos_Review.controller;

import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoNaCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarDepositoRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarPagTansfRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarSaqueRequest;
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
            @RequestBody CriarSaqueRequest request){
        BaseResponse response = transacaoService.criarSaque(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/transacoes/depositos")
    public ResponseEntity<BaseResponse> criarDeposito(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarDepositoRequest request){
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
    public ResponseEntity<BaseResponse> criarPagamentoTransferencia(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarPagTansfRequest request
            ){
        BaseResponse response = transacaoService.criarPagamentoTransferencia(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }
}
