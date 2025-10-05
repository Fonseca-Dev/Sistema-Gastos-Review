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
    <>
      {/* Header azul */}
      <div
        style={{
          position: "fixed",
          top: 0,
          left: 0,
          width: "100%",
          height: "220px",
          backgroundColor: "#2563eb",
          color: "white",
          padding: "16px",
          boxSizing: "border-box",
          zIndex: 1000,
        }}
      >
        {/* Saldo */}
        <div style={{ marginTop: "50px", paddingLeft: "16px" }}>
          <div style={{ fontSize: "14px" }}>Saldo atual</div>
          <div style={{ fontSize: "36px", fontWeight: "bold", marginTop: "4px" }}>
            {saldo.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
          </div>
        </div>
      </div>

      {/* Card branco com histórico */}
      <div
        style={{
          position: "fixed",
          top: "180px", // abaixo do azul
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "white",
          borderTopLeftRadius: "16px",
          borderTopRightRadius: "16px",
          padding: "16px",
          overflowY: "auto",
          zIndex: 1001,
        }}
      >
        <div style={{ fontSize: "18px", fontWeight: "bold", marginBottom: "16px" }}>
          Histórico de Transações
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
          {transacoes.map((transacao) => (
            <div
              key={transacao.id}
              style={{
                display: "flex",
                alignItems: "center",
                padding: "12px",
                borderRadius: "12px",
                boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
                cursor: "pointer",
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
      <div
        style={{
          position: "fixed",
          bottom: 0,
          left: 0,
          right: 0,
          zIndex: 1002,
        }}
      >
        <Menubar />
      </div>
    </>
  );
};

export default Extract;
