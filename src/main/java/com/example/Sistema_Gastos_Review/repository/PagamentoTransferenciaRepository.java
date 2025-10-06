package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoTransferenciaRepository extends JpaRepository<Transferencia, String> {
    List<Transferencia> findByContaOrigem_Id(String contaOrigemId);
}
