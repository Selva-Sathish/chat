import type { ReactNode } from "react";

type props = {
    children : ReactNode,
    classNames: string,
    disabled: boolean,
    type: "submit" | "button" | "reset"
}

export default function Button({children, disabled=false, type="submit"} : props ){
    const customStyle  = {
        padding: "10px 20px",
        border: "none",
        backgroundColor: "#007bff",
        borderColor: "#007bff",
        cursor: "pointer",
        color: "#fff"
    };

    return (
        <button style={customStyle} disabled={disabled} type={type}>{children}</button>
    )
}