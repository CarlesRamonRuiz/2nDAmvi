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
            <ThemeContext.Provider value={"context"}>
            </ThemeContext.Provider>
        </>
    )
}
export default Theme;