import {useState} from "react";

function RandomNumber(){
    const [check, setCheck] = useState(false);
    let randomNumber: number = Math.floor(Math.random() * 10);
    function handleMoreClick() {
        randomNumber = Math.floor(Math.random() * 10)+1;
        console.log(randomNumber)
        setCheck(!check);
    }
    return(
        <>
            <button onClick={handleMoreClick}>Generar Random Number</button>
            {randomNumber<5 && <p>El nombre {randomNumber} és petit</p>}
            {randomNumber>=5 && <p>El nombre {randomNumber} és gran</p>}
        </>
    )
}
export default RandomNumber;