import React from "react";
import { useNavigate } from "react-router-dom";
import Menubar from "../Menubar/Menubar";

const Extract: React.FC = () => {
  const navigate = useNavigate();
  const transacoes = [
    { id: 1, tipo: "Depósito", valor: 500, metodo: "Pix", tipoTransacao: "entrada", horario: "10:30" },
    { id: 2, tipo: "Pagamento", valor: 150, metodo: "Cartão", tipoTransacao: "saida", horario: "12:45" },
  ];
  const saldo = 1234.56;

  return (
    <div style={{ display: "flex", flexDirection: "column", height: "100vh" }}>
      {/* Header azul */}
      <div style={{ backgroundColor: "#2563eb", color: "white", padding: "16px" }}>
        <h2>Saldo atual</h2>
        <p style={{ fontSize: "32px", fontWeight: "bold" }}>
          {saldo.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
        </p>
      </div>

      {/* Conteúdo rolável */}
      <div style={{ flex: 1, overflowY: "auto", padding: "16px", backgroundColor: "#f0f2f5" }}>
        <h3>Histórico de transações</h3>
        <div style={{ display: "flex", flexDirection: "column", gap: "12px", marginTop: "12px" }}>
          {transacoes.map((t) => (
            <div
              key={t.id}
              style={{ backgroundColor: "white", padding: "12px", borderRadius: "12px", cursor: "pointer", boxShadow: "0 2px 8px rgba(0,0,0,0.1)" }}
              onClick={() => navigate(`/transacao/${t.id}`)}
            >
              <div style={{ display: "flex", justifyContent: "space-between" }}>
                <div>
                  <strong>{t.tipo}</strong>
                  <p style={{ margin: 0 }}>{t.horario}</p>
                </div>
                <div>
                  {t.tipoTransacao === "entrada" ? "+" : "-"} {t.valor.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                  <p style={{ margin: 0, fontSize: "12px", color: "#666" }}>{t.metodo}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Menubar fixo */}
      <div style={{ position: "fixed", bottom: 0, left: 0, right: 0 }}>
        <Menubar />
      </div>
    </div>
  );
};

export default Extract;
