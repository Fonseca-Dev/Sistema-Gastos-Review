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
    <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      
      {/* Header azul */}
      <div style={{
        backgroundColor: "#2563eb",
        padding: "24px 16px",
        color: "white",
        borderBottomLeftRadius: "16px",
        borderBottomRightRadius: "16px"
      }}>
        <div style={{ fontSize: "14px", opacity: 0.8 }}>Saldo atual</div>
        <div style={{ fontSize: "36px", fontWeight: "bold", marginTop: "4px" }}>
          R$ {saldo.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
        </div>
        <div style={{
          backgroundColor: '#00CD5C',
          borderRadius: '120px',
          padding: '4px 12px',
          color: 'white',
          fontSize: '14px',
          fontWeight: '500',
          display: 'inline-block',
          marginTop: '8px'
        }}>
          +5.2%
        </div>
      </div>

      {/* Botões Depositar e Saque */}
      <div style={{
        display: 'flex',
        gap: '12px',
        padding: '16px',
        justifyContent: 'center'
      }}>
        <button style={{
          flex: 1,
          backgroundColor: 'white',
          borderRadius: '120px',
          color: '#0065F5',
          fontSize: '17px',
          fontWeight: '700',
          cursor: 'pointer',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          gap: '6px',
          border: 'none',
          height: '42px'
        }}>
          {/* Ícone + */}
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M12 5v14"/>
            <path d="M19 12l-7-7-7 7"/>
          </svg>
          Depositar
        </button>

        <button style={{
          flex: 1,
          backgroundColor: 'white',
          borderRadius: '120px',
          color: '#0065F5',
          fontSize: '17px',
          fontWeight: '700',
          cursor: 'pointer',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          gap: '6px',
          border: 'none',
          height: '42px'
        }}>
          {/* Ícone - */}
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M12 5v14"/>
            <path d="M19 12l-7 7-7-7"/>
          </svg>
          Saque
        </button>
      </div>

      {/* Histórico */}
      <div style={{ padding: '0 16px', marginBottom: '16px' }}>
        <h3 style={{ fontSize: '18px', fontWeight: 'bold' }}>Histórico</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', marginTop: '8px' }}>
          {transacoes.map((transacao) => (
            <div key={transacao.id} onClick={() => handleTransacaoClick(transacao.id)} style={{
              display: 'flex',
              alignItems: 'center',
              padding: '12px',
              backgroundColor: 'white',
              borderRadius: '12px',
              cursor: 'pointer',
              boxShadow: '0 2px 8px rgba(0,0,0,0.08)'
            }}>
              <div style={{
                width: '48px',
                height: '48px',
                borderRadius: '12px',
                backgroundColor: '#f0f0f0',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                marginRight: '12px'
              }}>
                {renderIcon(transacao.icon)}
              </div>
              <div style={{ flex: 1, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div style={{ display: 'flex', flexDirection: 'column' }}>
                  <span style={{ fontWeight: 600, fontSize: '16px', color: '#1e293b' }}>{transacao.tipo}</span>
                  <span style={{ fontSize: '14px', color: '#64748b' }}>{transacao.horario}</span>
                </div>
                <div style={{ textAlign: 'right', display: 'flex', flexDirection: 'column', alignItems: 'flex-end' }}>
                  <span style={{ fontWeight: 600, fontSize: '16px', color: transacao.tipoTransacao === 'entrada' ? '#000' : '#ff0000' }}>
                    {transacao.tipoTransacao === 'entrada' ? '+' : '-'}R$ {transacao.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </span>
                  <span style={{ fontSize: '14px', color: '#adadad' }}>{transacao.metodo}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div style={{ flexGrow: 1 }}></div> {/* Espaço flexível para empurrar o Menubar */}

      <Menubar />
    </div>
  );
};

export default Extract;
