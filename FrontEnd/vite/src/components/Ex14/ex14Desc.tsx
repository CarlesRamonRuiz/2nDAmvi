import {ThemeContext} from "./ex14Context.ts";
import {useContext, useState} from "react";

function ex14Desc(){
    let theme = useContext(ThemeContext);
    const [tema, setTema] = useState(theme)
    return(
      <>
        
      </>
    );

}