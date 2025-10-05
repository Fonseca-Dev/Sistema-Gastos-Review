import React from "react";
import { Eye, EyeOff, Bell, MessageCircle, Settings } from "lucide-react";
import { useNavigate } from "react-router-dom";
import Menubar from "../Menubar/Menubar";

const Home: React.FC = () => {
  const navigate = useNavigate();
  const [showBalance, setShowBalance] = React.useState(false);
  const [saldo, setSaldo] = React.useState<number | null>(null);
  const [userAvatar, setUserAvatar] = React.useState<string | null>(
    () => localStorage.getItem("userAvatar") || null
  );
  const [userName, setUserName] = React.useState<string>(
    () => localStorage.getItem("userName") || "Usuário"
  );

  // Buscar saldo da última conta
  React.useEffect(() => {
    const usuarioId = localStorage.getItem("userID");
    if (usuarioId) {
      fetch(`https://sistema-gastos-694972193726.southamerica-east1.run.app/usuarios/${usuarioId}/contas`)
        .then((res) => {
          if (!res.ok) throw new Error("Erro ao buscar contas");
          return res.json();
        })
        .then((data) => {
          if (data && data.objeto && data.objeto.length > 0) {
            const conta = data.objeto[data.objeto.length - 1];
            setSaldo(conta.saldo);
          }
        })
        .catch((err) => console.error("Erro ao carregar saldo:", err));
    }
  }, []);

  // Escutar mudanças no localStorage
  React.useEffect(() => {
    const handleStorageChange = (e: StorageEvent) => {
      if (e.key === "userAvatar") setUserAvatar(e.newValue);
      if (e.key === "userName") setUserName(e.newValue || "Usuário");
    };
    window.addEventListener("storage", handleStorageChange);
    return () => window.removeEventListener("storage", handleStorageChange);
  }, []);

  const handleProfileClick = () => {
    navigate("/login");
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      {/* Header azul */}
      <div
        style={{
          width: "100%",
          height: "250px",
          backgroundColor: "#2563eb",
          color: "white",
          padding: "16px",
          boxSizing: "border-box",
        }}
      >
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "32px", marginTop: "20px" }}>
          <Settings size={26} />
          <div style={{ display: "flex", gap: "22px" }}>
            <MessageCircle size={26} />
            <Bell size={26} />
          </div>
        </div>

        {/* Perfil */}
        <div style={{ display: "flex", alignItems: "center", gap: "12px", marginBottom: "16px" }}>
          <button
            onClick={handleProfileClick}
            style={{
              width: "48px",
              height: "48px",
              borderRadius: "50%",
              border: "2px solid white",
              backgroundColor: userAvatar ? "transparent" : "#1e40af",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              fontSize: "20px",
              fontWeight: "bold",
              cursor: "pointer",
              color: "white",
              overflow: "hidden",
              padding: 0,
            }}
          >
            {userAvatar ? (
              <img src={userAvatar} alt="Avatar" style={{ width: "100%", height: "100%", objectFit: "cover" }} />
            ) : (
              userName.charAt(0).toUpperCase()
            )}
          </button>
          <p style={{ fontSize: "18px", fontWeight: "500", margin: 0 }}>Olá, {userName}!</p>
        </div>

        {/* Saldo */}
        <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
          <div style={{ fontSize: "32px", fontWeight: "bold", color: "white" }}>
            {showBalance
              ? "•••••"
              : saldo !== null
              ? saldo.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })
              : "Carregando..."}
          </div>
          <button
            onClick={() => setShowBalance(!showBalance)}
            style={{ background: "none", border: "none", color: "white", cursor: "pointer" }}
          >
            {showBalance ? <EyeOff size={22} /> : <Eye size={22} />}
          </button>
        </div>

        <button
          onClick={() => navigate("/extrato")}
          style={{
            fontSize: "14px",
            textDecoration: "underline",
            background: "none",
            border: "none",
            color: "white",
            cursor: "pointer",
            marginTop: "12px",
          }}
        >
          Ver extrato
        </button>
      </div>

      {/* Conteúdo principal */}
      <div style={{ flex: 1, padding: "16px", backgroundColor: "#f5f5f5" }}>
        {/* Aqui você pode colocar os cards de Pix, Pagar Conta, Empréstimos etc */}
      </div>

      {/* Menubar fixo */}
      <div style={{ position: "fixed", bottom: 0, left: 0, right: 0, zIndex: 1000 }}>
        <Menubar />
      </div>
    </div>
  );
};

export default Home;

