import { useEffect, useState, type ChangeEvent, type FormEvent } from "react"
import request from "../api/axios";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
export default function DemoLogin(){
    
    const {user, role, setUser, setRole} = useAuth();
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    
    function handleUsername(e: ChangeEvent<HTMLInputElement>){
        setUsername(e.target.value);
    }

    useEffect(() => {
        if(user && role){
            console.log(user, role , "inside the login page")
            navigate("/");
        }
    }, [user, role])

    function handlePassword(e: ChangeEvent<HTMLInputElement>){
        setPassword(e.target.value);
    }

    function handleForm(e: FormEvent){
        e.preventDefault();
        request.post("/auth/login", {
            username: username,
            password: password
        })
        .then(response => {
            const data = response.data;
            setUser(data.username);
            setRole("user");            
            navigate("/");
        })
        .catch(e => console.error("Network err ", e));
    }
    return (
        <>
            <div style={{display: "flex", justifyContent:"center", alignItems: "center"}} onSubmit={handleForm}>
                <form action="" method="post">
                    <input type="text" name="username" id="username" value={username} onChange={handleUsername}/>
                    <input type="password" name="password" id="password" value={password} onChange={handlePassword}/>
                    <input type="submit" value="submit" />
                </form>
            </div>
        </>
    )
}