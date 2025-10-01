package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Pagamento_Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoTransferenciaRepository extends JpaRepository<Pagamento_Transferencia, String> {
    List<Pagamento_Transferencia> findByContaOrigem_Id(String contaOrigemId);
}
