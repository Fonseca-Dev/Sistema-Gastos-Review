import React, { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';
import { buscarUltimaConta } from '../services/ContaService';

interface SaldoContextType {
  saldo: number;
  setSaldo: (value: number) => void;
  atualizarSaldo: () => Promise<void>; // opcional, para atualizar após operações
}

const SaldoContext = createContext<SaldoContextType | undefined>(undefined);

export const useSaldo = () => {
  const context = useContext(SaldoContext);
  if (!context) {
    throw new Error('useSaldo deve ser usado dentro de um SaldoProvider');
  }
  return context;
};

interface SaldoProviderProps {
  children: ReactNode;
}

export const SaldoProvider: React.FC<SaldoProviderProps> = ({ children }) => {
  const [saldo, setSaldo] = useState<number>(0); // Valor inicial do saldo
  const userID = localStorage.getItem('userID'); // ou pega do contexto de usuário

  const atualizarSaldo = async () => {
    if (!userID) return;
    try {
      const ultimaConta = await buscarUltimaConta(userID);
      setSaldo(ultimaConta?.saldo ?? 0);
    } catch (error) {
      console.error('Erro ao atualizar saldo:', error);
      setSaldo(0);
    }
  };

  useEffect(() => {
    atualizarSaldo();
  }, [userID]);

  return (
    <SaldoContext.Provider value={{ saldo, setSaldo, atualizarSaldo }}>
      {children}
    </SaldoContext.Provider>
  );
};
