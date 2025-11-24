import '../css/Login.css';
import countries from '../data/countries.json';
import Button from '../components/Button';

export default function Login(){
    return (
        <div className="login-card">
            <form action="" method="post">
                <select name="countries" id="countries">
                    {
                        Object.entries(countries).map(([key,value]) => 
                            <option value={key} key={key}>{value}</option>
                        )
                    } 
                </select>
                <input type="tel" name="mobile_no" id="mobile_no" /> <br />
                <Button>submit</Button>
            </form>
        </div>
    )
}