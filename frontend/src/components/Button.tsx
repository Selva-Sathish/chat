import type { ReactNode } from "react";
import '../css/Button.css';
type props = {
    children : ReactNode,
    classNames?: string,
    disabled?: boolean,
    type?: "submit" | "button" | "reset"
}

export default function Button({children, disabled=false, type="submit"} : props ){
    return (
        <button className="btn btn-primary" disabled={disabled} type={type}>{children}</button>
    )
}