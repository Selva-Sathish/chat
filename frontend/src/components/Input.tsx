import type { ReactNode } from "react";

type InputProps = React.HTMLAttributes<HTMLInputElement>;

export default function Input({type = "text", ...rest}: InputProps){
    
    return (
        <input type="text" />
    )
}