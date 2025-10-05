import React, { useState } from "react";
import { ArrowLeft } from "lucide-react";
import { useNavigate } from "react-router-dom";
import backgroundImage from "../../assets/images/background.png";

const Login: React.FC = () => {
  const navigate = useNavigate();
  const [avatarImage] = useState<string | null>(() => {
    return localStorage.getItem('userAvatar') || null;
  });
  const [userName] = useState<string>(() => {
    return localStorage.getItem('userName') || 'Usuário';
  });

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    alert("Login realizado com sucesso!");
    navigate("/home");
  };

  const handleQuickLogin = () => {
    console.log("Entrando rapidamente");
    alert("Login rápido realizado!");
    navigate("/home");
  };

  const handleBackClick = () => {
    navigate("/");
  };

  return (
    <div style={{
      position: 'absolute',
      top: '0px',
      left: '0px',
      width: '393px',
      height: '852px',
      display: 'flex',
      flexDirection: 'column',
      overflowY: 'auto',
      boxSizing: 'border-box',
      backgroundImage: `url(${backgroundImage})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center',
      backgroundRepeat: 'no-repeat',
      
      /* Ocultar barra de scroll mantendo funcionalidade */
      scrollbarWidth: 'none',
      msOverflowStyle: 'none',
    } as React.CSSProperties}>
      
        {/* Logo */}
        <div style={{
          position: 'absolute',
          top: '40px',
          left: '16px',
          zIndex: 2
        }}>
          <h1
            style={{
              fontFamily: "system-ui, -apple-system, sans-serif",
              fontWeight: 600,
              fontSize: "24px",
              lineHeight: "32px",
              color: "white",
              margin: 0
            }}
          >
            <span style={{ fontWeight: 600, color: "white" }}>agi</span>
            <span style={{ fontWeight: 260, color: "white" }}>Control</span>
          </h1>
        </div>

        {/* Botão voltar */}
        <button 
          onClick={handleBackClick}
          style={{
            background: 'none',
            border: 'none',
            color: 'white',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            position: 'absolute',
            top: '70px',
            right: '99px',
            fontSize: '16px',
            zIndex: 2
          }}
        >
          <ArrowLeft size={24} />
          Voltar
        </button>

        {/* Avatar */}
        <div style={{
          position: 'absolute',
          left: '45px',
          top: '50%',
          transform: 'translateY(-50%)',
          marginTop: '-40px',
          zIndex: 2
        }}>
          {avatarImage ? (
            <img
              src={avatarImage}
              alt="avatar"
              style={{
                width: '80px',
                height: '80px',
                borderRadius: '50%',
                border: '2px solid white',
                objectFit: 'cover',
                objectPosition: 'center',
                boxShadow: '0 4px 12px rgba(0, 0, 0, 0.3)'
              }}
            />
          ) : (
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              border: '2px solid white',
              backgroundColor: '#f0f0f0',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontSize: '40px',
              color: '#999999',
              boxShadow: '0 4px 12px rgba(0, 0, 0, 0.3)'
            }}>
              👤
            </div>
          )}
        </div>

        {/* Saudação */}
        <div style={{
          position: 'absolute',
          left: '45px',
          top: '50%',
          transform: 'translateY(-50%)',
          marginTop: '30px',
          width: 'calc(100% - 32px)',
          zIndex: 2
        }}>
          <h2 style={{
            fontSize: '24px',
            fontWeight: 'bold',
            margin: '0 0 -30px 0',
            fontFamily: 'system-ui, -apple-system, sans-serif',
            lineHeight: '1.3',
            textAlign: 'left',
            color: 'white'
          }}>
            Que bom te ver de novo,<br />
            {userName}!
          </h2>
        </div>
        
        <div style={{
          position: 'absolute',
          left: '45px',
          top: '50%',
          transform: 'translateY(-50%)',
          marginTop: '90px',
          width: 'calc(100% - 32px)',
          zIndex: 2
        }}>
          <h2 style={{
            fontSize: '24px',
            fontWeight: 'bold',
            margin: '0',
            fontFamily: 'system-ui, -apple-system, sans-serif',
            color: '#CAFC92',
            textAlign: 'left'
          }}>
            Bora agilizar?
          </h2>
        </div>

        {/* Formulário */}
        <form onSubmit={handleLogin} style={{ 
          position: 'absolute',
          left: '45px',
          bottom: '30px',
          transform: 'translateY(-50%)',
          marginTop: '160px',
          display: 'flex', 
          flexDirection: 'column', 
          gap: '20px',
          width: 'calc(100% - 90px)',
          maxWidth: '350px',
          zIndex: 2
        }}>
          
          {/* Botão Entrar Rápido */}
          <button
            type="button"
            onClick={handleQuickLogin}
            style={{
              width: '100%',
              padding: '16px',
              backgroundColor: 'white',
              color: '#2563eb',
              border: '2px solid #2563eb',
              borderRadius: '12px',
              fontSize: '16px',
              fontWeight: '600',
              cursor: 'pointer',
              transition: 'all 0.2s',
              boxShadow: '0 2px 8px rgba(37, 99, 235, 0.1)'
            }}
            onMouseOver={(e) => {
              (e.target as HTMLButtonElement).style.backgroundColor = '#ffffffff';
            }}
            onMouseOut={(e) => {
              (e.target as HTMLButtonElement).style.backgroundColor = 'white';
            }}
          >
            Entrar
          </button>

          {/* Botão Entrar com Senha */}
          <button
            type="submit"
            style={{
              width: '100%',
              padding: '16px',
              backgroundColor: 'transparent',
              color: 'white',
              border: '2px solid white',
              borderRadius: '12px',
              fontSize: '16px',
              fontWeight: '600',
              cursor: 'pointer',
              transition: 'all 0.2s'
            }}
            onMouseOver={(e) => {
              (e.target as HTMLButtonElement).style.backgroundColor = 'rgba(255, 255, 255, 0.1)';
            }}
            onMouseOut={(e) => {
              (e.target as HTMLButtonElement).style.backgroundColor = 'transparent';
            }}
          >
            Entrar com senha
          </button>
        </form>
      </div>
  );
};

export default Login;
