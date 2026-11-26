import {useState} from "react";
import {ThemeContext} from "./ex14Context.ts";
function Theme(){
    const [showMore, setShowMore] = useState(false);
    function handleMoreClick() {
        setShowMore(!showMore);
        setTheme(theme);
    }
    return(
        <>
            <ThemeContext.Provider value={"light"}>
            <h2>Variable d'entorn {theme}</h2>
            <button onClick={handleMoreClick}>Canviar a {showMore ? 'light' : 'dark'}
            </button>
            </ThemeContext.Provider>
        </>
    )
}
export default Theme;