import {Button} from "@material-ui/core";
import React, {useEffect, useRef} from "react";

export default function AddToBasket() {

    const [state, setState] = React.useState('empty');

    const timerRef = useRef(null);

    function handleAdd() {
        setState('adding');
        timerRef.current = setTimeout(() => {
            setState('added')
        }, 500);
    }

    useEffect(() => {
        return () => clearTimeout(timerRef.current)
    }, []);

    return (
        <>
            <Button variant="outlined" size={"small"} color="primary" onClick={() => handleAdd()}>Add to basket</Button>
        </>
    )
}