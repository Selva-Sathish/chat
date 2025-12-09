import type { JSX } from "react";
import { useAuth }  from "../context/AuthContext";
import { Navigate } from "react-router-dom";

export default function PublicRoute({children}: {children : JSX.Element} ){
    
    const {user, role} = useAuth();
    
    return (user && role) ? <Navigate to={"/"} replace /> : children;
}