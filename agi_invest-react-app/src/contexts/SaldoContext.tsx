import React, { createContext, useContext, useState, useEffect } from "react";
import { useLocation } from "react-router-dom";

type SaldoContextType = {
  saldo: number | null;
  atualizarSaldo: () => Promise<void>;
};

const SaldoContext = createContext<SaldoContextType>({
  saldo: null,
  atualizarSaldo: async () => {},
});

export const SaldoProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [saldo, setSaldo] = useState<number | null>(null);
  const location = useLocation();

  const atualizarSaldo = async () => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;

    try {
      const response = await fetch(`https://seu-backend.com/carteira/${userId}`);
      if (!response.ok) throw new Error("Erro ao buscar saldo");
      const data = await response.json();
      setSaldo(data.saldo ?? 0);
    } catch (err) {
      console.error("Erro ao atualizar saldo:", err);
    }
  };

  // Atualiza o saldo sempre que trocar de rota
  useEffect(() => {
    atualizarSaldo();
  }, [location.pathname]);

  return (
    <SaldoContext.Provider value={{ saldo, atualizarSaldo }}>
      {children}
    </SaldoContext.Provider>
  );
};

export const useSaldo = () => useContext(SaldoContext);
