import React from "react";
import { useNavigate } from "react-router-dom";
import Menubar from "../Menubar/Menubar";
import { useSaldo } from "../../contexts/SaldoContext";
import { useTransacao } from "../../contexts/TransacaoContext";
import { useTipoTransacao } from "../../contexts/TipoTransacaoContext";

const Extract: React.FC = () => {
  const navigate = useNavigate();
  const { saldo } = useSaldo();
  const { transacoes } = useTransacao();
  const { renderIcon } = useTipoTransacao();

  const handleTransacaoClick = (transacaoId: number) => {
    navigate(`/transacao/${transacaoId}`);
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", height: "100vh" }}>
      {/* Card azul de topo */}
      <div
        style={{
          backgroundColor: "#2563eb",
          color: "white",
          padding: "16px",
          paddingTop: "80px",
          borderBottomLeftRadius: "16px",
          borderBottomRightRadius: "16px",
        }}
      >
        {/* Saldo */}
        <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
          <div style={{ fontSize: "36px", fontWeight: "bold" }}>
            R${" "}
            {saldo.toLocaleString("pt-BR", {
              minimumFractionDigits: 2,
              maximumFractionDigits: 2,
            })}
          </div>
          {/* Variação */}
          <div
            style={{
              backgroundColor: "#00CD5C",
              borderRadius: "120px",
              padding: "3px 12px",
              color: "white",
              fontSize: "14px",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            +5.2%
          </div>
        </div>

        {/* Botões Depositar/Saque */}
        <div style={{ display: "flex", gap: "12px", marginTop: "16px" }}>
          <button
            style={{
              flex: 1,
              backgroundColor: "white",
              color: "#0065F5",
              borderRadius: "120px",
              fontWeight: "700",
              height: "42px",
              border: "none",
              cursor: "pointer",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              gap: "6px",
            }}
          >
            Depositar
          </button>
          <button
            style={{
              flex: 1,
              backgroundColor: "white",
              color: "#0065F5",
              borderRadius: "120px",
              fontWeight: "700",
              height: "42px",
              border: "none",
              cursor: "pointer",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              gap: "6px",
            }}
          >
            Saque
          </button>
        </div>
      </div>

      {/* Histórico de transações */}
      <div
        style={{
          flex: 1,
          backgroundColor: "#f0f2f5",
          padding: "16px",
          marginTop: "16px",
          overflowY: "auto",
        }}
      >
        <h3>Histórico de Transações</h3>
        <div style={{ display: "flex", flexDirection: "column", gap: "12px", marginTop: "12px" }}>
          {transacoes.map((transacao) => (
            <div
              key={transacao.id}
              style={{
                display: "flex",
                alignItems: "center",
                padding: "12px",
                borderRadius: "12px",
                cursor: "pointer",
                boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
                backgroundColor: "white",
              }}
              onClick={() => handleTransacaoClick(transacao.id)}
            >
              {/* Ícone */}
              <div
                style={{
                  width: "48px",
                  height: "48px",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  marginRight: "12px",
                  borderRadius: "12px",
                  backgroundColor: "#f5f5f5",
                  flexShrink: 0,
                }}
              >
                {renderIcon(transacao.icon)}
              </div>

              {/* Dados da transação */}
              <div
                style={{
                  flex: 1,
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <div style={{ display: "flex", flexDirection: "column" }}>
                  <div style={{ fontSize: "16px", fontWeight: 600, color: "#1e293b" }}>
                    {transacao.tipo}
                  </div>
                  <div style={{ fontSize: "14px", color: "#64748b" }}>{transacao.horario}</div>
                </div>

                <div style={{ display: "flex", flexDirection: "column", alignItems: "flex-end" }}>
                  <div style={{ fontSize: "16px", fontWeight: 600 }}>
                    {transacao.tipoTransacao === "entrada" ? "+" : "-"}{" "}
                    {transacao.valor.toLocaleString("pt-BR", {
                      minimumFractionDigits: 2,
                      maximumFractionDigits: 2,
                    })}
                  </div>
                  <div style={{ fontSize: "14px", color: "#adadadff" }}>{transacao.metodo}</div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Menubar fixo */}
      <div style={{ position: "sticky", bottom: 0, left: 0, right: 0 }}>
        <Menubar />
      </div>
    </div>
  );
};

export default Extract;
