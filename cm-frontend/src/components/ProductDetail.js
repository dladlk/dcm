// noinspection JSUnresolvedVariable

import { makeStyles } from "@material-ui/core";
import { Fragment } from "react";
import ItemDetailsService from '../services/ItemDetailsService';
import CatalogBadge from "./CatalogBadge";
import ProductPictureList from './ProductPictureList';
import AddToBasket from "./AddToBasket";

const useStyles = makeStyles(theme => ({
    row: {
        display: "flex",
        alignItems: "flex-start",
        marginBottom: theme.spacing(1),
    },
    name: {
        fontWeight: 'bold',
        flex: '1',
        paddingRight: '1ch',
        [theme.breakpoints.up('md')]: {
        minWidth: '80px',
        },
    },
    value: {
        flex: '6',
    },
}));

function DataRow(props) {
    const { name, children } = props;

    const classes = useStyles();

    return (
        <div className={classes.row}>
            <div className={classes.name}>{name}</div>
            <div className={classes.value}>{children}</div>
        </div>
    )
}

function DataView(props) {

    const _isValueDefined = (value) => value ? true : false;
    // noinspection JSUnusedLocalSymbols
    const _renderValue = (v, i) => { return v };

    const { name, value, isValueDefined = _isValueDefined, renderValue = _renderValue } = props;

    return (
        <> {isValueDefined(value) ? (
            <DataRow name={name}>{renderValue(value)}</DataRow>
        ) : (<></>)}
        </>
    )
}

const isListFilled = (list) => list && list.length > 0 ? true : false;

function DataListView(props) {
    const _isValueDefined = isListFilled;
    const _renderListValue = (v, i) => {
        return (
            <Fragment key={i}>
                <div>{v}</div>
            </Fragment>
        )
    }
    const { name, value, isValueDefined = _isValueDefined, renderListValue = _renderListValue, extractValue} = props;
    return (
        <DataView name={name} value={value} isValueDefined={isValueDefined} renderValue={(value) => Array.isArray(value) ? value.map((e) => { return renderListValue(e, extractValue)}) : renderListValue(value)} />
    )
}

function Quantity(props) {
    const renderQuantity = (quantity) => { return (<>{quantity.quantity} {quantity.unitCode}</>) };
    return (
        <DataView name={props.name} value={props.value} renderValue={renderQuantity} />
    )
}


const renderPicture = (specList) => {
    return (
        <ProductPictureList specList = {specList}/>
    )
}

const renderCatalogs = (source) => {
    return (
        <>
         <span>{source.length}{source.length > 1 ? ' catalogs: ':' catalog'}</span>
         {source.map((s,i) => {
             return (
                 <CatalogBadge key={s} code={s} index={i}/>
             )
        })}
        </>
    )
}

const renderSourcedValue = (v, extractValue = (e)=> {return e.value}) => {
    return (
        <div key={v._source ? v._source.code + '-' + extractValue(v) : v}>
        {v._source ? (<><CatalogBadge code={v._source.code} index={v._source.index}/>{' '}<span>{extractValue(v)}</span></>) : <>{v}</>}
        </div>
    )
}

export default function ProductView(props) {

    const showTech = false;

    const showOrdering = true;

    const { product } = props;

    return (
        <>
            { showOrdering && (
                <>
                    <DataRow name={'Order'}>
                        <AddToBasket product={product} {...props} />
                    </DataRow>
                </>
            )}
            { showTech && (
                <>
                    <DataView name="ID" value={product.id}/>
                    <DataView name="Created" value={product.createTime}/>
                    <DataView name="Updated" value={product.updateTime}/>
                    <DataView name="Version" value={product.version}/>
                    <DataView name="Orderable Indicator" value={product.document.orderableIndicator}/>
                    <Quantity name="Content Quantity" value={product.document.contentUnitQuantity}/>
                    <Quantity name="Minimum Quantity" value={product.document.minimumOrderQuantity}/>
                    <Quantity name="Maximum Quantity" value={product.document.maximumOrderQuantity}/>
                </>
            )}

            <DataView name="Catalogs" value={product._source} renderValue={renderCatalogs}/>
            <DataView name="Standard number" value={ItemDetailsService.itemStandardNumber(product.document.item)}/>
            <DataListView name="Name" value={product.document.item.name} renderListValue={renderSourcedValue}/>
            <DataListView name="Seller number" value={product.document.item.sellersItemIdentification} renderListValue={renderSourcedValue} extractValue={(e)=>{return e.id}}/>
            <DataListView name="Manufacturer" value={product.document.item.manufacturerParty} renderListValue={renderSourcedValue} extractValue={(e)=>{return e?.partyName?.name}}/>
            <DataListView name="Description" value={product.document.item.descriptionList} renderListValue={renderSourcedValue}/>
            <DataListView name="Keywords" value={product.document.item.keywordList} renderListValue={renderSourcedValue}/>
            <DataView name="UNSPSC" value={ItemDetailsService.itemUNSPSC(product.document.item)}/>
            <DataView name="Origin Country" value={ItemDetailsService.itemOriginCountry(product.document.item)}/>
            <DataListView name="Specifications" value={ItemDetailsService.itemSpecifications(product.document.item)} renderListValue={ItemDetailsService.renderItemSpecification} />
            <DataListView name="Certificates" value={ItemDetailsService.itemCertificates(product.document.item)} renderListValue={ItemDetailsService.renderItemCertificate}/>
            <DataListView name="Additional properties" value={ItemDetailsService.itemAdditionalProperties(product.document.item)} renderListValue={ItemDetailsService.renderItemAdditionalProperty}/>
            <DataView name="Pictures" value={ItemDetailsService.itemPictureURL(product.document.item)} renderValue={renderPicture} isValueDefined={isListFilled} />
        </>
    )
}