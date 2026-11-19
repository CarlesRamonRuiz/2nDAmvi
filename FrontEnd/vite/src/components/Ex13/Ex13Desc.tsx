import type { IsActive } from "./IsActive.ts";

type IsActiveProps = {
    getInfo: (isActive: IsActive) => void;
};

export default function Ex13Desc(props: IsActiveProps) {
    //let estat: boolean = active.isActive;

    function getCheckbox(e:React.ChangeEvent<HTMLInputElement>) {
        const cosa:IsActive = {isActive: e.target.checked };

        props.getInfo(cosa);
       // estat = cosa.isActive;
    }

    return (
        <input onChange={getCheckbox} type="checkbox" />
    )
}