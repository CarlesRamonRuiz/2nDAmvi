type props1={
    nom:string;
    edat:number;
}
function User(props:props1){
    return(
    <>
        <p><strong>Nom:</strong> {props.nom} <br/><strong>Edat:</strong> {props.edat} anys</p>
    </>
    )
}
export default User;