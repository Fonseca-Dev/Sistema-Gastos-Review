import React from "react";
import { Home, FileText, CreditCard, User, Wallet } from "lucide-react";
import { useNavigate, useLocation } from "react-router-dom";

const Menubar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleHomeClick = () => {
    navigate("/home");
  };

  const handleExtratoClick = () => {
    navigate("/extrato");
  };

  const handleContaClick = () => {
    // Implementar navegação para conta quando a página existir
    console.log("Conta clicada");
  };

  const handleCartoesClick = () => {
    // Implementar navegação para cartões quando a página existir
    console.log("Cartões clicados");
  };

  const handlePerfilClick = () => {
    // Implementar navegação para perfil quando a página existir
    console.log("Perfil clicado");
  };

  const getButtonStyle = (path: string) => ({
    display: 'flex',
    flexDirection: 'column' as const,
    alignItems: 'center',
    color: location.pathname === path ? '#2563eb' : '#6b7280',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: '4px'
  });

  return (
    <div style={{
      position: 'fixed',
      bottom: 0,
      left: 0,
      right: 0,
      borderTopRightRadius: '16px',
      borderTopLeftRadius: '16px',
      backgroundColor: 'white',
      boxShadow: '0 -2px 10px rgba(0, 0, 0, 0.1)',
      borderTop: '1px solid #e5e7eb',
      display: 'flex',
      justifyContent: 'space-around',
      alignItems: 'center',
      padding: '28px 0',
      zIndex: 9999
    }}>
      <button onClick={handleHomeClick} style={getButtonStyle('/home')}>
        <Home size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Início</span>
      </button>

      <button onClick={handleExtratoClick} style={getButtonStyle('/extrato')}>
        <FileText size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Extrato</span>
      </button>

      <button onClick={handleContaClick} style={getButtonStyle('/conta')}>
        <Wallet size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Conta</span>
      </button>

      <button onClick={handleCartoesClick} style={getButtonStyle('/cartoes')}>
        <CreditCard size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Cartões</span>
      </button>

      <button onClick={handlePerfilClick} style={getButtonStyle('/perfil')}>
        <User size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Perfil</span>
      </button>
    </div>
  );
};

export default Menubar;