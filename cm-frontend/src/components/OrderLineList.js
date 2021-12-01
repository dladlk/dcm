import {makeStyles, withStyles} from "@material-ui/core/styles";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import ItemDetailsService from "../services/ItemDetailsService";
import React from "react";
import {Fab, Paper} from "@material-ui/core";
import RemoveIcon from "@material-ui/icons/Remove";
import AddIcon from "@material-ui/icons/Add";

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

export default function OrderLineList(props) {

    const {basketData, showRowDetails, changeBasket, productList} = props;

    const useStyles = makeStyles((theme) => ({
        paper: {
            padding: theme.spacing(2),
            paddingBottom: theme.spacing(5),
            marginBottom: theme.spacing(3),
        },
        table: {
            minWidth: 600,
        },
    }));

    const classes = useStyles();

    const productItem = (productId) => {
        if (productId in productList) {
            return productList[productId].document.item;
        }
        return null;
    }

    return <Paper className={classes.paper}>
        <TableContainer>
            <Table className={classes.table} size="small" aria-label="Basket contents">
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
                    {!basketData.isEmpty() ? basketData.getOrderLineList().map((orderLine, index) => (
                        <StyledTableRow key={orderLine.productId} onClick={() => showRowDetails(orderLine.productId)}>
                            <TableCell>{(index + 1)}</TableCell>
                            <TableCell align={"center"}><QuantityControl quantity={orderLine.quantity} productId={orderLine.productId} changeBasket={changeBasket}/></TableCell>
                            <TableCell>{ItemDetailsService.itemName(productItem(orderLine.productId))}</TableCell>
                            <TableCell>{ItemDetailsService.itemStandardNumber(productItem(orderLine.productId))}</TableCell>
                            <TableCell>{ItemDetailsService.itemSellerNumber(productItem(orderLine.productId))}</TableCell>
                        </StyledTableRow>
                    )) : (
                        <StyledTableRow>
                            <TableCell colSpan={5} align="center">Basket is empty</TableCell>
                        </StyledTableRow>
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    </Paper>
        ;
}
