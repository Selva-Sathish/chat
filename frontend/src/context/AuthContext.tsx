import React, { createContext, useContext, useEffect, useState } from "react";
import request from "../api/axios";

interface AuthContextType {
  user: string | null;
  role: string | null;
  setUser: (u: string | null) => void;
  setRole: (r: string | null) => void;
}

const AuthContext = createContext<AuthContextType | null>(null);


export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<string | null>(null);
  const [role, setRole] = useState<string | null>(null);
  
  async function fetchUser(){
      try{
          const res = await request.post("/auth/me");
          setUser(res.data.username);
          setRole(res.data.role);
      }
      catch (err){
          console.error(err);
      }
  }

  useEffect(() => {
      fetchUser();
  }, []);

  return (
    <AuthContext.Provider value={{ user, role, setRole, setUser }}>
      {children}
    </AuthContext.Provider>
  );
};

// react hook — use ONLY inside components
export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
};

