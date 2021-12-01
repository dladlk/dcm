import React from "react";
import {makeStyles, withStyles} from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import CircularProgress from "@material-ui/core/CircularProgress";
import {useHistory} from "react-router";
import PageHeader from '../components/PageHeader';
import {Fab} from "@material-ui/core";
import SendIcon from "@material-ui/icons/Send";
import RemoveIcon from "@material-ui/icons/Remove";
import AddIcon from "@material-ui/icons/Add";
import DataService from "../services/DataService";
import ItemDetailsService from "../services/ItemDetailsService";
import * as PropTypes from "prop-types";

const useStyles = makeStyles(theme => ({
    table: {
        minWidth: 600,
    },
    header: {
        marginBottom: '1em'
    },
    paper: {
        padding: theme.spacing(2),
        paddingBottom: theme.spacing(5),
        marginBottom: theme.spacing(3),
    }
}));

const StyledTableCell = withStyles(() => ({
    head: {
        fontWeight: 'bold',
    },
}))(TableCell);

const StyledTableRow = withStyles((theme) => ({
    root: {
        '&:nth-of-type(odd)': {
            backgroundColor: theme.palette.action.hover,
        },
        '&:hover': {
            backgroundColor: 'rgba(0,0,0,.08)',
        },
        cursor: 'pointer'
    },
}))(TableRow);

function QuantityControl(props) {
    const {productId, quantity, changeBasket} = props;

    const changeQuantity = (e, quantityChange) => {
        e.stopPropagation();
        if (quantity + quantityChange === 0) {
            changeBasket(productId, 0);
        } else {
            changeBasket(productId, quantityChange);
        }
        return false;
    }

    return (
        <div onClick={(e) => {
            e.stopPropagation();
            return false;
        }}>
            <Fab color="default" aria-label="Decrease quantity" size="small" title={"Decrease quantity"}>
                <RemoveIcon onClick={(e) => changeQuantity(e, -1)}/>
            </Fab>
            <span style={{padding: '10px', display: 'inline-block', width: '50px', textAlign: 'center'}}>{quantity}</span>
            <Fab color="default" aria-label="Increase quantity" size="small" title={"Increase quantity"}>
                <AddIcon onClick={(e) => changeQuantity(e, 1)}/>
            </Fab>
        </div>
    )
}

function OrderLineList(props) {
    return <TableContainer>
        <Table className={props.classes.table} size="small" aria-label="Basket contents">
            <TableHead>
                <TableRow>
                    <StyledTableCell align="left">#</StyledTableCell>
                    <StyledTableCell align="center">Quantity</StyledTableCell>
                    <StyledTableCell align="left">Name</StyledTableCell>
                    <StyledTableCell align="left">Standard number</StyledTableCell>
                    <StyledTableCell align="left">Seller number</StyledTableCell>
                </TableRow>
            </TableHead>
            <TableBody>
                {!props.basketData.isEmpty() ? props.basketData.getOrderLineList().map(props.callbackFn) : (
                    <StyledTableRow>
                        <TableCell colSpan={5} align="center">Basket is empty</TableCell>
                    </StyledTableRow>
                )}
            </TableBody>
        </Table>
    </TableContainer>;
}

OrderLineList.propTypes = {
    classes: PropTypes.any,
    basketData: PropTypes.any,
    callbackFn: PropTypes.func
};
export default function BasketPage(props) {

    const {basketData, changeBasket} = props;

    const [isLoading, setLoading] = React.useState(false);
    const [productList, setProductList] = React.useState({});
    const [reloadCount, setReloadCount] = React.useState(0);

    const classes = useStyles();

    const {push} = useHistory();

    const refreshAction = () => {
        setReloadCount(reloadCount + 1);
    }
    const sendAction = () => {
    }

    const productItem = (productId) => {
        if (productId in productList) {
            return productList[productId].document.item;
        }
        return null;
    }

    const callLoadProducts = () => { loadProducts().finally(() => setLoading(false))};

    async function loadProducts() {
        if (!basketData.isEmpty()) {
            setLoading(true);
            const productIdList = basketData.getOrderLineList().map((orderLine) => orderLine.productId);

            await DataService.fetchProductsByIds(productIdList).then(response => {
                    let responseData = response.data;
                    console.log(responseData);
                    const productMapById = {}
                    for (const index in responseData) {
                        const p = responseData[index];
                        productMapById[p.id] = p;
                    }
                    console.log(productMapById);
                    setProductList(productMapById);
                }
            ).catch(error => {
                console.log('Error occurred: ' + error.message);
            });
        }
    }

    React.useEffect(callLoadProducts, [reloadCount]);

    const showRowDetails = (productId) => {
        push('/product/view/' + productId);
    }

    return (
        <>
            <PageHeader name="Basket" refreshAction={refreshAction}>
                <Fab color="primary" aria-label="Send order" size="small" title={"Send order"}>
                    <SendIcon onClick={() => sendAction()}/>
                </Fab>
            </PageHeader>
            <Paper className={classes.paper}>
                {(isLoading || false) ? (
                    <CircularProgress/>
                ) : (
                    <>
                        <OrderLineList key={'empty'} classes={classes} basketData={basketData} callbackFn={(orderLine, index) => (
                            <StyledTableRow key={orderLine.productId} onClick={() => showRowDetails(orderLine.productId)}>
                                <TableCell>{(index + 1)}</TableCell>
                                <TableCell align={"center"}><QuantityControl quantity={orderLine.quantity} productId={orderLine.productId} changeBasket={changeBasket}/></TableCell>
                                <TableCell>{ItemDetailsService.itemName(productItem(orderLine.productId))}</TableCell>
                                <TableCell>{ItemDetailsService.itemStandardNumber(productItem(orderLine.productId))}</TableCell>
                                <TableCell>{ItemDetailsService.itemSellerNumber(productItem(orderLine.productId))}</TableCell>
                            </StyledTableRow>
                        )}/>
                    </>
                )}
            </Paper>
        </>
    );
}
