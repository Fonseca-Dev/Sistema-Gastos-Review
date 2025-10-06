handleGoToSignupimport React, { useState } from "react";
//import { ArrowLeft } from "lucide-react";
import { useNavigate } from "react-router-dom";
import backgroundImage from "../../assets/images/background.png";

const Login: React.FC = () => {
  const navigate = useNavigate();
  const [avatarImage] = useState<string | null>(() => localStorage.getItem('userAvatar') || null);
  const [userName] = useState<string>(() => localStorage.getItem('userName') || 'Usuário');

  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    alert(`Login realizado com sucesso!\nEmail: ${email}`);
    navigate("/home");
  };

  /*const handleQuickLogin = () => {
    console.log("Entrando rapidamente");
    alert("Login rápido realizado!");
    navigate("/home");
  };

  const handleBackClick = () => navigate("/");*/

  const handleGoToSignup = () => navigate("/signup");

  return (
    <div style={{
      position: 'absolute',
      top: 0,
      left: 0,
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
      scrollbarWidth: 'none',
      msOverflowStyle: 'none',
    } as React.CSSProperties}>
      
      {/* Logo */}
      <div style={{ position: 'absolute', top: '40px', left: '16px', zIndex: 2 }}>
        <h1 style={{ fontFamily: "system-ui, -apple-system, sans-serif", fontWeight: 600, fontSize: "24px", color: "white", margin: 0 }}>
          <span style={{ fontWeight: 600 }}>agi</span>
          <span style={{ fontWeight: 260 }}>Control</span>
        </h1>
      </div>


      {/* Avatar */}
      <div style={{
        position: 'absolute', left: '45px', top: '35%', transform: 'translateY(-50%)',
        marginTop: '-40px', zIndex: 2
      }}>
        {avatarImage ? (
          <img src={avatarImage} alt="avatar" style={{
            width: '80px', height: '80px', borderRadius: '50%', border: '2px solid white',
            objectFit: 'cover', boxShadow: '0 4px 12px rgba(0,0,0,0.3)'
          }}/>
        ) : (
          <div style={{
            width: '80px', height: '80px', borderRadius: '50%', border: '2px solid white',
            backgroundColor: '#f0f0f0', display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: '40px', color: '#999', boxShadow: '0 4px 12px rgba(0,0,0,0.3)'
          }}>👤</div>
        )}
      </div>

      {/* Saudação */}
      <div style={{
        position: 'absolute', left: '45px', top: '35%', transform: 'translateY(-50%)',
        marginTop: '30px', width: 'calc(100% - 32px)', zIndex: 2
      }}>
        <h2 style={{ fontSize: '24px', fontWeight: 'bold', margin: 0, color: 'white' }}>
          Que bom te ver de novo,<br />{userName}!
        </h2>
      </div>

      {/* Subtítulo */}
      <div style={{
        position: 'absolute', left: '45px', top: '35%', transform: 'translateY(-50%)',
        marginTop: '90px', width: 'calc(100% - 32px)', zIndex: 2
      }}>
        <h2 style={{ fontSize: '24px', fontWeight: 'bold', margin: 0, color: '#CAFC92' }}>Bora agilizar?</h2>
      </div>

      {/* Formulário */}
      <form onSubmit={handleLogin} style={{
        position: 'absolute', left: '45px', bottom: '30px', transform: 'translateY(-50%)',
        marginTop: '160px', display: 'flex', flexDirection: 'column', gap: '16px',
        width: 'calc(100% - 90px)', maxWidth: '350px', zIndex: 2
      }}>
        {/* Campo Email */}
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          required
          style={{
            width: '100%', padding: '16px', borderRadius: '12px', border: '2px solid white',
            backgroundColor: 'transparent', color: 'white', fontSize: '16px'
          }}
        />

        {/* Campo Senha */}
        <input
          type="password"
          placeholder="Senha"
          value={senha}
          onChange={e => setSenha(e.target.value)}
          required
          style={{
            width: '100%', padding: '16px', borderRadius: '12px', border: '2px solid white',
            backgroundColor: 'transparent', color: 'white', fontSize: '16px'
          }}
        />

        {/* Botão Entrar */}
        <button
          type="submit"
          style={{
            width: '100%', padding: '16px', backgroundColor: 'white', color: '#2563eb',
            border: '2px solid #2563eb', borderRadius: '12px', fontSize: '16px', fontWeight: '600',
            cursor: 'pointer'
          }}
        >
          Entrar
        </button>

        {/* Link para cadastro */}
        <p style={{ textAlign: 'center', color: 'white', margin: 0 }}>
          Não tem cadastro?{" "}
          <span
            onClick={handleGoToSignup}
            style={{ color: '#CAFC92', cursor: 'pointer', fontWeight: 'bold' }}
          >
            Cadastre-se
          </span>
        </p>
      </form>
    </div>
  );
};

export default Login;






