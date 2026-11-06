import "./ex3.css"

function Entrar(){
    console.log("El ratolí ha entrat a la capsa!")
    return(
    <div className={"capsa"} onMouseEnter={Entrar}></div>
    )
}
export default Entrar