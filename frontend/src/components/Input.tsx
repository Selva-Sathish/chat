import "../css/Button.css";
import "../css/Input.css";

type InputProps = React.InputHTMLAttributes<HTMLInputElement>;

export default function Input( {className, type = "text",  ...rest} : InputProps){
    
    return (
        <input type={type} className={(type == "button") ? "btn" : "form-input" } {...rest} />
    )
}