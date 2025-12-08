import { createContext, useContext, useState, type ReactNode } from "react";
import request from "../api/axios";

interface AuthContextType {
  user: string | null;
  setUser: (user: string) => void;
  role: string | null;
  setRole: (role: string ) => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<string | null>(null);
  const [role, setRole] = useState<string | null>(null);

  const value: AuthContextType = {
    user,
    setUser,
    role,
    setRole,
  };

  async function refreshToken() {
      request
            .post("/auth/refresh")
            .then(response => response.data)
            
  }
  return (
        <AuthContext.Provider value={{ user, setUser, role, setRole }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
  const ctx = useContext(AuthContext);

  if (ctx === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }

  return ctx;
}
