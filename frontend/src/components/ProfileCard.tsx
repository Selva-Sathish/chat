import '../css/ProfileCard.css';

export default function Profiles(){
    const now = new Date();

    let hours = now.getHours();
    let minutes = now.getMinutes().toString().padStart(2, "0");
    let ampm = hours >= 12 ? "PM" : "AM";

    hours = hours % 12;
    hours = hours || 12;

    let finalTime = `${hours}:${minutes} ${ampm}`;

    return (
        <>
            <div className="profiles">
                <div style={{display:"flex"}}>
                    <div className='profile-icon'>
                        <img src="" alt="" />
                    </div>
                    <div className='profile-name'>
                        <div className='name'>Sathish</div>
                        <div className='profile-cont'>content</div>
                    </div>
                </div>
                <div className='profile-time'>
                    {finalTime}
                </div>            
            </div>
        </>     
    )
}