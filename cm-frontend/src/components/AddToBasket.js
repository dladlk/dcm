import {Button} from "@material-ui/core";
import React, {useEffect, useRef} from "react";
import CircularProgress from "@material-ui/core/CircularProgress";
import {ProductBasketStatus} from "./BasketData";


export default function AddToBasket(props) {

    const {changeBasket, basketData, product} = props;

    const [state, setState] = React.useState(basketData.getProductBasketStatus(product.id));

    const getButtonTitle = () => {
        switch (state) {
            case ProductBasketStatus.Adding:
                return 'Adding to basket';
            case ProductBasketStatus.Added:
                return 'Remove from basket';
            default:
                return 'Add to basket';
        }
    }

    const isProgress = () => {
        return state === ProductBasketStatus.Adding;
    }

    // TODO: Remove - temporary code to imitate slow adding
    const timerRef = useRef(null);
    const handleClick = () => {
        if (state === ProductBasketStatus.Empty) {
            setState(ProductBasketStatus.Adding);
            timerRef.current = setTimeout(() => {
                changeBasket(product.id, 1);
                setState(ProductBasketStatus.Added)
            }, 300);
        } else if (state === ProductBasketStatus.Added) {
            changeBasket(product.id, 0);
            setState(ProductBasketStatus.Empty);
        }
    }

    useEffect(() => {
        return () => clearTimeout(timerRef.current)
    }, []);

    return (
        <>
            <Button variant="outlined" size={"small"} color="primary" onClick={() => handleClick()}>
                {isProgress() && (
                    <CircularProgress size={'1rem'} style={{marginRight: '5px'}}/>
                )}
                {getButtonTitle()}
            </Button>
        </>
    )
}