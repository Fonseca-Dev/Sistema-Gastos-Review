import React from "react";
import { Eye, EyeOff, RefreshCcw, Bell, MessageCircle, Settings } from "lucide-react";
import { useNavigate } from "react-router-dom";
import Menubar from "../Menubar/Menubar";

const Home: React.FC = () => {
  const navigate = useNavigate();
  const [showBalance, setShowBalance] = React.useState(false);
  const [saldo, setSaldo] = React.useState<number | null>(1234.56); // Simulação

  const userName = localStorage.getItem("userName") || "Usuário";

  return (
    <div style={{ display: "flex", flexDirection: "column", height: "100vh" }}>
      {/* Header azul */}
      <div
        style={{
          backgroundColor: "#2563eb",
          color: "white",
          padding: "16px",
          display: "flex",
          flexDirection: "column",
        }}
      >
        {/* Top icons */}
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "24px" }}>
          <Settings size={26} />
          <div style={{ display: "flex", gap: "16px" }}>
            <MessageCircle size={26} />
            <Bell size={26} />
          </div>
        </div>

        {/* Perfil */}
        <div style={{ display: "flex", alignItems: "center", gap: "12px", marginBottom: "24px" }}>
          <div
            style={{
              width: "48px",
              height: "48px",
              borderRadius: "50%",
              backgroundColor: "#1e40af",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              fontWeight: "bold",
              fontSize: "20px",
            }}
          >
            {userName.charAt(0).toUpperCase()}
          </div>
          <p style={{ margin: 0, fontSize: "18px" }}>Olá, {userName}!</p>
        </div>

        {/* Saldo */}
        <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
          <div style={{ fontSize: "32px", fontWeight: "bold" }}>
            {showBalance ? "•••••" : saldo?.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
          </div>
          <button onClick={() => setShowBalance(!showBalance)} style={{ background: "none", border: "none", color: "white" }}>
            {showBalance ? <EyeOff size={22} /> : <Eye size={22} />}
          </button>
        </div>

        {/* Botão Ver Extrato */}
        <button
          onClick={() => navigate("/extrato")}
          style={{
            marginTop: "16px",
            backgroundColor: "white",
            color: "#2563eb",
            borderRadius: "12px",
            padding: "12px 24px",
            border: "none",
            fontWeight: 600,
            cursor: "pointer",
          }}
        >
          Ver extrato
        </button>
      </div>

      {/* Espaço para conteúdo rolável */}
      <div style={{ flex: 1, backgroundColor: "#f0f2f5", padding: "16px" }}>
        <p>Conteúdo principal da Home...</p>
      </div>

      {/* Menubar fixo */}
      <div style={{ position: "fixed", bottom: 0, left: 0, right: 0 }}>
        <Menubar />
      </div>
    </div>
  );
};

export default Home;
