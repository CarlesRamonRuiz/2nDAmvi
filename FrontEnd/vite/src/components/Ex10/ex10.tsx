import {LlistaProductes} from "./producte.tsx";
import type {JSX} from "react";

function Llista(){
    const llistaProductes:string[] = ['Cereals','Pasta'];

    const mapaProductes = llistaProductes.map((nom,preu)=>{
        return <div key={nom}>{preu + 1} - {nom}</div>
    })
    // Funció que transforma la llista de noms de l'objecte ts a un conjunt d'elements amb format jsx.Element.
    function carregarLlistaProductes():JSX.Element[]{
        // Creem una array de JSX.Element. Cal importar com s'indica a dalt
        const llistaProductes:JSX.Element[] = [];
        // Malauradament i de manera indesitjada, he hagut de posar const perquè si no eslint em dona error.
        for (const nom of LlistaProductes){
            // Coses important, es necessita un key per diferenciar-se de la resta d'elements li.
            // Com que és codi tsx, es posa les variables entre claus
            const li1:JSX.Element = (
                <li key={nom.id}>
                    {nom.id} . Nom:{nom.nom} Preu: {nom.preu}
                </li>
            );
            // Afegim els elements a l'array
            llistaProductes.push(li1);
        }
        return llistaProductes;
    }
    const llista:JSX.Element[] = carregarLlistaProductes();
    return(
        <>

            <h2>Llista de Productes</h2>
            {llista.length>0 &&
            <ol>
                {llista}
            </ol>}
            {llista.length<=0 && <h2>No hi ha elements.</h2>}

        </>
    )
}
export default Llista;