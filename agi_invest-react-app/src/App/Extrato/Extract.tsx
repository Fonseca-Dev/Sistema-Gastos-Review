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
    <div style={{ position: "relative", width: "100%", height: "100vh", overflow: "hidden" }}>
      
      {/* Card azul topo */}
      <div style={{
        position: "fixed",
        top: 0,
        left: 0,
        right: 0,
        backgroundColor: "#2563eb",
        color: "white",
        padding: "16px",
        paddingTop: "80px",
        zIndex: 1000
      }}>
        {/* Saldo e variação */}
        <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
          <div style={{ fontSize: "36px", fontWeight: "bold" }}>
            R$ {saldo.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </div>
          <div style={{
            backgroundColor: "#00CD5C",
            borderRadius: "120px",
            padding: "3px 12px",
            color: "white",
            fontSize: "14px",
            display: "flex",
            alignItems: "center",
            justifyContent: "center"
          }}>
            +5.2%
          </div>
        </div>

        {/* Botões Depositar e Saque */}
        <div style={{ display: "flex", gap: "12px", marginTop: "16px" }}>
          <button style={{
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
            gap: "6px"
          }}>
            Depositar
          </button>
          <button style={{
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
            gap: "6px"
          }}>
            Saque
          </button>
        </div>
      </div>

      {/* Card branco principal */}
      <div style={{
        position: "absolute",
        top: "170px",
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: "white",
        borderTopLeftRadius: "16px",
        borderTopRightRadius: "16px",
        zIndex: 1001,
        display: "flex",
        flexDirection: "column",
        overflow: "hidden"
      }}>
        {/* Cabeçalho do histórico */}
        <div style={{ padding: "16px" }}>
          <h3 style={{ margin: 0 }}>Histórico</h3>
        </div>

        {/* Lista de transações scrollável */}
        <div style={{ flex: 1, overflowY: "auto", padding: "0 16px 80px 16px" }}>
          {transacoes.map((transacao, index) => (
            <div key={transacao.id} style={{
              display: "flex",
              alignItems: "center",
              padding: "12px",
              borderRadius: "12px",
              marginBottom: "12px",
              boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
              cursor: "pointer",
              backgroundColor: "white"
            }} onClick={() => handleTransacaoClick(transacao.id)}>
              {/* Ícone */}
              <div style={{
                width: "48px",
                height: "48px",
                borderRadius: "12px",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                marginRight: "12px",
                backgroundColor: "#f5f5f5",
                flexShrink: 0
              }}>
                {renderIcon(transacao.icon)}
              </div>

              {/* Dados da transação */}
              <div style={{ flex: 1, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <div style={{ display: "flex", flexDirection: "column" }}>
                  <div style={{ fontSize: "16px", fontWeight: 600, color: "#1e293b" }}>{transacao.tipo}</div>
                  <div style={{ fontSize: "14px", color: "#64748b" }}>{transacao.horario}</div>
                </div>
                <div style={{ display: "flex", flexDirection: "column", alignItems: "flex-end" }}>
                  <div style={{ fontSize: "16px", fontWeight: 600 }}>
                    {transacao.tipoTransacao === "entrada" ? "+" : "-"} R${transacao.valor.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </div>
                  <div style={{ fontSize: "14px", color: "#adadadff" }}>{transacao.metodo}</div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Menubar fixo */}
      <div style={{ position: "fixed", bottom: 0, left: 0, right: 0, zIndex: 1002 }}>
        <Menubar />
      </div>
    </div>
  );
};

export default Extract;
