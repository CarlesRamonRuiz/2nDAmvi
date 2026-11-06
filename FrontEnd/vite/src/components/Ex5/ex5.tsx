import User from "./user.tsx";
import "./ex5.css"
function Perfil(){
    const becari1nom:string = "Eloi";
    const becari1edat:number = 24;

    const becari2nom:string = "Biel";
    const becari2edat:number = 67;

    return(
        <>
            <div className={"capsa"}>
                <h1>Perfil d'Usuari</h1>
            <User nom={becari1nom}edat={becari1edat}></User>
            </div>
            <div className={"capsa"}>
                <h1>Perfil d'Usuari</h1>
                <User nom={becari2nom}edat={becari2edat}></User>
            </div>
        </>
    )

}
export default Perfil