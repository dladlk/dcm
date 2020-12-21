import { makeStyles } from "@material-ui/core";
import { Fragment } from "react";
import ItemDetailsService from '../services/ItemDetailsService';

const useStyles = makeStyles(theme => ({
    row: {
        display: "flex",
        alignItems: "flex-start",
        marginBottom: theme.spacing(1),
    },
    name: {
        fontWeight: 'bold',
        flex: '1',
        minWidth: '80px', 
    },
    value: {
        flex: '6',
    }
  }));

function DataView (props) {
    
    const {name, value} = props;
    const classes = useStyles();

    return ( 
        <> {value ? (
        <div className={classes.row}>
            <div className={classes.name}>{name}</div>
            <div className={classes.value}>{value}</div>
        </div>
        ) : ( <></> ) } 
        </>
    )
}

function DataListView (props) {
    
    const {name, value} = props;
    const classes = useStyles();

    return ( 
        <> {value ? (
        <div className={classes.row}>
            <div className={classes.name}>{name}</div>
            <div className={classes.value}>
                {value.map((v, i) => (
                    <Fragment key={i}>
                    <div>{v}</div>
                    </Fragment>
                ))}
            </div>
        </div>
        ) : ( <></> ) } 
        </>
    )
}

function Quantity (props) {
    const {name, value} = props;
    function quantityValue(quantity) {
        return (
            <>{quantity.quantity} {quantity.unitCode}</>
        )
    }
    return (
        <> {value ? (
            <DataView name = {name} value={quantityValue(value)}></DataView>
        ) : ( <></> ) } 
        </>
    )
}

export default function ProductView(props) {

    const showTech = false;

    const {product} = props;

    return (
        <>
            { showTech ? (
                <>
                <DataView name="ID" value={product.id}></DataView>
                <DataView name="Created" value={product.createTime}></DataView>
                <DataView name="Updated" value={product.updateTime}></DataView>
                <DataView name="Version" value={product.version}></DataView>
                <DataView name="Orderable Indicator" value={product.document.orderableIndicator}></DataView>
                <Quantity name="Content Quantity" value={product.document.contentUnitQuantity}></Quantity>
                <Quantity name="Minimum Quantity" value={product.document.minimumOrderQuantity}></Quantity>
                <Quantity name="Maximum Quantity" value={product.document.maximumOrderQuantity}></Quantity>
                </>
            ) : ( <></> ) }

            <DataView name="Name" value={product.document.item.name}></DataView>
            <DataView name="Standard number" value={ItemDetailsService.itemStandardNumber(product.document.item)}></DataView>
            <DataView name="Seller number" value={ItemDetailsService.itemSellerNumber(product.document.item)}></DataView>
            <DataView name="Description" value={product.document.item.descriptionList}></DataView>
            <DataListView name="Keywords" value={product.document.item.keywordList}></DataListView>
            <DataView name="UNSPSC" value={ItemDetailsService.itemUNSPSC(product.document.item)}></DataView>
            <DataView name="Origin Country" value={ItemDetailsService.itemOriginCountry(product.document.item)}></DataView>
            <DataView name="URL" value={ItemDetailsService.itemPictureURL(product.document.item)}></DataView>
        </>
    )
}