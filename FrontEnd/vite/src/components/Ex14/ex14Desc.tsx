import {ThemeContext} from "./ex14Context.ts";
import {useContext, useState} from "react";

function ex14Desc(){
    let theme = useContext(ThemeContext);
    const [tema, setTema] = useState(theme)

    function canviarTema(){
        if(tema==="light")
            setTema("dark")
        else
            setTema("light")
    }
    return(
      <>
        <button onClick={canviarTema} className={tema}>Canviar a {!tema}</button>
      </>
    );

}
export default ex14Desc;