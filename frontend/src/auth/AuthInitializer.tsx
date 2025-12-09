import { useEffect } from 'react';
import {useAuth} from '../context/AuthContext';
import request from '../api/axios';

export default function AuthInitilizer(){
    const { setUser, setRole } = useAuth();
    
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
    }, [])

    return null;
}

