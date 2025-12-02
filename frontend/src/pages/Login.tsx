import '../css/Login.css';
import '../css/Input.css';

import countries from '../data/countries.json';
import Button from '../components/Button';
import Input from '../components/Input';

export default function Login(){
    return (
        <div className="login-card">
            <form action="" method="post">
                <div>
                    <select name="countries" id="countries" className='form-select'>
                        {
                            Object.entries(countries).map(([key,value]) => 
                                <option value={key} key={key}>{value}</option>
                            )
                        } 
                    </select>
                    <Input /> <br />
                </div>
                <Button type='submit'>submit</Button>
            </form>
        </div>
    )
}