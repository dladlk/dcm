import React from "react";
import {Button} from "@material-ui/core";
import {ProductBasketStatus} from "./BasketData";

export const getProductBasketButtonTitle = (basketState) => {
    switch (basketState) {
        case ProductBasketStatus.Added:
            return 'Remove from basket';
        default:
            return 'Add to basket';
    }
}

export const handleProductBasketIconClick = (basketData, product, changeBasket) => {
    const state = basketData.getProductBasketStatus(product.id);
    if (state === ProductBasketStatus.Empty) {
        changeBasket(product.id, 1);
    } else if (state === ProductBasketStatus.Added) {
        changeBasket(product.id, 0);
    }
}

export default function AddToBasket(props) {

    const {changeBasket, basketData, product} = props;

    const handleClick = () => {
        handleProductBasketIconClick(basketData, product, changeBasket);
    }

    return (
        <>
            <Button variant="outlined" size={"small"} color="primary" onClick={() => handleClick()}>
                {getProductBasketButtonTitle(basketData.getProductBasketStatus(product.id))}
            </Button>
        </>
    )
}