import { makeStyles } from "@material-ui/core";
import { Fragment } from "react";

const useStyles = makeStyles(theme => ({
    row: {
        display: "flex",
        alignItems: "flex-start",
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

    const {product} = props;

    return (
        <>
            <DataView name="ID" value={product.id}></DataView>
            <DataView name="Created" value={product.createTime}></DataView>
            <DataView name="Updated" value={product.updateTime}></DataView>
            <DataView name="Version" value={product.version}></DataView>
            <DataView name="Orderable Indicator" value={product.document.orderableIndicator}></DataView>
            <Quantity name="Content Quantity" value={product.document.contentUnitQuantity}></Quantity>
            <Quantity name="Minimum Quantity" value={product.document.minimumOrderQuantity}></Quantity>
            <Quantity name="Maximum Quantity" value={product.document.maximumOrderQuantity}></Quantity>
            <DataView name="Name" value={product.document.item.name}></DataView>
            <DataView name="Description" value={product.document.item.descriptionList}></DataView>
            <DataListView name="Keywords" value={product.document.item.keywordList}></DataListView>
        </>
    )
}