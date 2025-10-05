import React from "react";
import { useNavigate } from "react-router-dom";
import Menubar from "../Menubar/Menubar";
import { useSaldo } from "../../contexts/SaldoContext";
import { useTransacao } from "../../contexts/TransacaoContext";
import { useTipoTransacao } from "../../contexts/TipoTransacaoContext";

const Extract: React.FC = () => {
  const navigate = useNavigate();
  const { saldo, setSaldo } = useSaldo();
  const { transacoes } = useTransacao();
  const { renderIcon } = useTipoTransacao();

  const handleTransacaoClick = (transacaoId: number) => {
    navigate(`/transacao/${transacaoId}`);
  };

  return (
    <>

    <Menubar />

    {/*txt "Saldo atual"*/}
    <div style={{
      position: 'fixed',
      top: '85px',
      left: '44px',
      zIndex: 1002,

      backgroundColor: 'rgba(255, 255, 255, 0.45)',
      right: '220px',
      borderRadius: '120px',
      padding: '0px 12px',


      color: 'white',
      fontSize: '14px',
      fontWeight: 'thin'
    }}>
      Saldo atual
    </div>

    {/* Container para Saldo e Variação */}
    <div style={{
      position: 'fixed',
      top: '110px',
      left: '44px',
      zIndex: 1002,
      display: 'flex',
      alignItems: 'center',
      gap: '12px'
    }}>
      {/* txt "Saldo" */}
      <div style={{
        color: 'white',
        fontSize: '36px',
        fontWeight: 'bold'
      }}>
        {`R$ ${saldo.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`}
      </div>

      {/*txt "Variação"*/}
      <div style={{
        backgroundColor: '#00CD5C',
        borderRadius: '120px',
        padding: '3px 12px',
        color: 'white',
        fontSize: '14px',
        fontWeight: 'thin',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        textAlign: 'center'
      }}>
        +5.2%
      </div>
    </div>

    {/* Ícone de seta para a direita */}
    <div style={{
      position: 'fixed',
      top: '121px',
      right: '44px',
      zIndex: 1002,
      color: 'white',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center'
    }}>
      <svg 
        width="32" 
        height="32" 
        viewBox="0 0 24 24" 
        fill="none" 
        stroke="currentColor" 
        strokeWidth="2" 
        strokeLinecap="round" 
        strokeLinejoin="round"
      >
        <path d="M9 18l6-6-6-6"/>
      </svg>
    </div>

    {/* Container dos Botões */}
    <div style={{
      position: 'fixed',
      top: '170px',
      left: '37px',
      zIndex: 1002,
      display: 'flex',
      gap: '12px'
    }}>
      {/* Botão Depositar */}
      <button style={{
        backgroundColor: 'rgba(255, 255, 255, 1)',
        borderRadius: '120px',
        color: '#0065F5',
        fontSize: '17px',
        fontWeight: '700',
        cursor: 'pointer',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        textAlign: 'center',
        gap: '6px',
        transition: 'all 0.2s ease',
        border: 'none',
        height: '42px',
        minWidth: '150px'
      }}>
        <svg 
          width="20" 
          height="20" 
          viewBox="0 0 24 24" 
          fill="none" 
          stroke="currentColor" 
          strokeWidth="2" 
          strokeLinecap="round" 
          strokeLinejoin="round"
        >
          <path d="M12 5v14"/>
          <path d="M19 12l-7-7-7 7"/>
        </svg>
        Depositar
      </button>

      {/* Botão Saque */}
      <button style={{
        backgroundColor: 'rgba(255, 255, 255, 1)',
        borderRadius: '120px',
        color: '#0065F5',
        fontSize: '17px',
        fontWeight: '700',
        cursor: 'pointer',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        textAlign: 'center',
        gap: '6px',
        transition: 'all 0.2s ease',
        border: 'none',
        height: '42px',
        minWidth: '150px'
      }}>
        <svg 
          width="20" 
          height="20" 
          viewBox="0 0 24 24" 
          fill="none" 
          stroke="currentColor" 
          strokeWidth="2" 
          strokeLinecap="round" 
          strokeLinejoin="round"
        >
          <path d="M12 5v14"/>
          <path d="M19 12l-7 7-7-7"/>
        </svg>
        Saque
      </button>
    </div>



    {/* txt "Histórico" */}
    <div style={{
      position: 'fixed',
      left: '16px',
      top: '280px',
      zIndex: 1002,
      color: 'white',
      fontSize: '18px',
      fontWeight: 'bold'
    }}>
      Histórico
    </div>

    {/* Card branco fixo no bottom */}
        <div style={{
          position: 'fixed',
          left: '0px',
          right: '0px',
          top: '310px', 
          bottom: '0px', 
          height: '500px',
          borderTopRightRadius: '16px',
          borderTopLeftRadius: '16px',
          width: '100%',
          backgroundColor: 'white',
          zIndex: 1009, // Sobrepõe o card azul (que tem z-index 1000)
          display: 'flex',
          flexDirection: 'column',
          overflow: 'hidden' // Garante que o conteúdo não vaze das bordas arredondadas
        }}>

          {/* Conteúdo do card branco */}
          <div style={{
            flex: 1,
            overflowY: 'auto',
            padding: '20px 16px',
            paddingBottom: '80px', // Espaço para o menu inferior
            backgroundColor: 'white'
          }}>
            {/* Lista de transações */}
            <div style={{
              display: 'flex',
              flexDirection: 'column',
              gap: '12px'
            }}>
              {transacoes.map((transacao, index) => (
                <div key={transacao.id} style={{
                  display: 'flex',
                  alignItems: 'center',
                  padding: '8px',
                  backgroundColor: 'white',
                  borderRadius: '12px',
                  border: 'none',
                  cursor: 'pointer',
                  boxShadow: `0 ${2 + index}px ${8 + index * 2}px -2px rgba(0, 0, 0, 0.1), 0 ${1 + index}px ${4 + index}px -1px rgba(0, 0, 0, 0.06)`
                }}
                onClick={() => handleTransacaoClick(transacao.id)}
                >
                  {/* Ícone */}
                  <div style={{
                    width: '48px',
                    height: '48px',
                    backgroundColor: 'white',
                    borderRadius: '12px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    marginRight: '12px',
                    flexShrink: 0
                  }}>
                    {renderIcon(transacao.icon)}
                  </div>

                  {/* Informações da transação */}
                  <div style={{
                    flex: 1,
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center'
                  }}>
                    {/* Lado esquerdo - Tipo e horário */}
                    <div style={{
                      display: 'flex',
                      flexDirection: 'column'
                    }}>
                      <div style={{
                        fontSize: '16px',
                        fontWeight: '600',
                        color: '#1e293b',
                        marginBottom: '2px'
                      }}>
                        {transacao.tipo}
                      </div>
                      <div style={{
                        fontSize: '14px',
                        color: '#64748b'
                      }}>
                        {transacao.horario}
                      </div>
                    </div>

                    {/* Lado direito - Valor e método */}
                    <div style={{
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'flex-end'
                    }}>
                      <div style={{
                        fontSize: '16px',
                        fontWeight: '600',
                        color: transacao.tipoTransacao === 'entrada' ? '#000000ff' : '#000000ff',
                        marginBottom: '2px'
                      }}>
                        {transacao.tipoTransacao === 'entrada' ? '+' : '-'}R$ {transacao.valor.toLocaleString('pt-BR', { 
                          minimumFractionDigits: 2, 
                          maximumFractionDigits: 2 
                        })}
                      </div>
                      <div style={{
                        fontSize: '15px',
                        color: '#adadadff'
                      }}>
                        {transacao.metodo}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
          
        </div>    {/* Header/card azul */}
    <div style={{
          position: 'fixed',
          top: '0px',
          bottom: '0px',
          width: '393px',
          backgroundColor: '#2563eb',
          color: 'white',
          padding: '16px',
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
          zIndex: 1000
        }}></div>

    </>
  );
};

export default Extract;
