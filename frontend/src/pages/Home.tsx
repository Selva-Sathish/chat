import { useEffect, useState } from "react"
import request from "../api/axios";

interface User{
    id: number,
    username: string,
    number: string
}

export default function Home(){
    const [users, setUsers] = useState<User[]>([]);
    useEffect(() => {
        request.get("/user")
            .then(response => {setUsers(response.data)})
            .catch(err => console.error("Network Err, ", err));
    }, [])
    return (
        <>
            {
                users &&
                <table>
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Username</th>
                            <th>Mobile Number</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((usr) => (
                            <tr key={usr.id}>
                                <td>{usr.id}</td>
                                <td>{usr.username}</td>
                                <td>{usr.number}</td>
                            </tr>
                        ))}
                    </tbody>

                </table>
            }
        </>
    )
} 