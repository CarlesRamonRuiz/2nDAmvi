import {useState} from "react";
import Ex13Desc from "./Ex13Desc.tsx";
import type { IsActive } from "./IsActive.ts";
import "./Ex13.css";

export default function Ex13() {
    const [actiu,setActiu] = useState<IsActive>({isActive:false});
    function getInfo(active: IsActive) {
        setActiu(active);
    }
    console.log(actiu);
    return (
        <>
            { actiu.isActive
            ?
                (<h2 className={"active"}>Falta gent a Juny</h2>)
            :
                (<h2 className={"deactive"}>Falta gent a Juny</h2>)
            }
            <Ex13Desc getInfo={getInfo}/>
        </>
    )
}