import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasket';

const useStyles = makeStyles((theme) => ({
    basket: {
        padding: theme.spacing(0, 2),
    },
    basketIcon: {
        color: theme.palette.common.white
    },
}));

export default function BasketBar() {
    const classes = useStyles();

    return (

        <div className={classes.basket}>
            <div className={classes.basketIcon}>
                <ShoppingBasketIcon/>
            </div>
        </div>

    )
}