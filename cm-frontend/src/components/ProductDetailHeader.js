import {Card, CardContent, Fab, makeStyles, Typography} from "@material-ui/core";
import ArrowIcon from '@material-ui/icons/KeyboardBackspaceOutlined';
import RefreshIcon from '@material-ui/icons/Refresh';
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasketOutlined';
import {useHistory} from "react-router";
import React from "react";
import {getProductBasketButtonTitle, handleProductBasketIconClick} from "./AddToBasket";
import {ProductBasketStatus} from "./BasketData";

const useStyles = makeStyles(theme => ({

    cardContent: {
        '&:last-child': {
            paddingBottom: theme.spacing(2),
        },
    },
    row: {
        display: "flex",
    },
    header: {
        flex: '1',
        [theme.breakpoints.down('sm')]: {
            display: 'none',
        },
    },
    buttons: {
        flex: '1',
        display: "flex",
        placeContent: 'stretch flex-end',
        alignItems: 'stretch',

        '& button': {
            marginLeft: theme.spacing(4),
        }
    }
}));

// noinspection JSUnusedLocalSymbols
const _emptyNavigator = {
    hasNext: (id) => {
        return false
    },
    hasPrevious: (id) => {
        return false
    },
    getNext: (id) => {
        return null
    },
    getPrevious: (id) => {
        return null
    },
}

export default function ProductDetailHeader(prop) {

    const {name, navigator = _emptyNavigator, id, refreshAction, basketData, product, changeBasket} = prop;

    const classes = useStyles();

    const history = useHistory();

    const handleBack = () => {
        history.push('/');
    }

    const navigateTo = (path) => {
        history.push(path);
    }

    const handleIconClick = () => {
        handleProductBasketIconClick(basketData, product, changeBasket);
    }

    const getShoppingBasketColor = (product) => {
        return product && basketData.getProductBasketStatus(product.id) === ProductBasketStatus.Empty ? "" : "primary";
    }

    return (
        <Card style={{marginTop: '16px'}}>
            <CardContent className={classes.cardContent}>
                <div className={classes.row}>
                    <div className={classes.header}>
                        <Typography variant="h4">{name}</Typography>
                    </div>
                    <div className={classes.buttons}>
                        <Fab color={getShoppingBasketColor(product)} aria-label={getProductBasketButtonTitle(product)} title={getProductBasketButtonTitle(product)} size="small" onClick={() => handleIconClick()}>
                            <ShoppingBasketIcon/>
                        </Fab>
                        <Fab color="primary" aria-label="Previous" title={"Previous product"} size="small" disabled={!navigator.hasPrevious(id)} onClick={() => navigateTo(navigator.getPrevious(id))}>
                            <ArrowIcon style={{transform: 'rotate(90deg)'}}/>
                        </Fab>
                        <Fab color="primary" aria-label="Next" title={"Next product"} size="small" disabled={!navigator.hasNext(id)} onClick={() => navigateTo(navigator.getNext(id))}>
                            <ArrowIcon style={{transform: 'rotate(270deg)'}}/>
                        </Fab>
                        <Fab color="primary" aria-label="Refresh" title={"Refresh product"} size="small" onClick={() => refreshAction(id)}>
                            <RefreshIcon/>
                        </Fab>
                        <Fab color="primary" aria-label="Back" title={"Return back to list"} size="small" onClick={handleBack}>
                            <ArrowIcon/>
                        </Fab>
                    </div>
                </div>
            </CardContent>
        </Card>
    )
} 