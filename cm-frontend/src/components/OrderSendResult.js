import {makeStyles} from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import {Button} from "@material-ui/core";
import React from "react";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import {StyledTableCell, StyledTableRow} from "../pages/ProductListPage";
import {DataRow, DataView} from "./ProductDetail";
import {useHistory} from "react-router";

export default function OrderSendResult(props) {

    const {sentOrderData} = props;

    const useStyles = makeStyles((theme) => ({
        paper: {
            padding: theme.spacing(2),
            marginBottom: theme.spacing(2),
        },
    }));

    const classes = useStyles();

    const {push} = useHistory();

    const showBasketDetails = (basketId) => {
        push('/basket/' + basketId);
    }
    const showProductDetails = (productId) => {
        push('/product/view/' + productId);
    }

    return (
        <>
            <Paper className={classes.paper}>
                <DataView name="Sent" value={sentOrderData.createTime}/>
                <DataView name="Status" value={sentOrderData.status}/>
                <DataView name="Supplier" value={sentOrderData.supplierName}/>
                <DataView name="Order number" value={sentOrderData.orderNumber}/>
                <DataView name="Lines" value={sentOrderData.lineCount}/>
                <DataView name="Index in basket" value={(sentOrderData.orderIndex + 1)}/>
                <DataRow name={'Actions'}>
                    <Button variant={"outlined"} size={"small"} color="primary" style={{marginRight: '1em'}} onClick={() => showBasketDetails(sentOrderData.basketId)}>View basket</Button>
                    <Button variant={"outlined"} size={"small"} color="primary" style={{marginRight: '1em'}}>Download</Button>
                    <Button variant={"outlined"} size={"small"} color="primary">Copy link</Button>
                </DataRow>
            </Paper>
            <Paper className={classes.paper}>

                <TableContainer style={{marginTop: "2em"}}>
                    <Table size="small" aria-label="Items table">
                        <TableHead>
                            <TableRow>
                                <StyledTableCell align="center">Line</StyledTableCell>
                                <StyledTableCell align="left">Item name</StyledTableCell>
                                <StyledTableCell align="left">Seller number</StyledTableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {sentOrderData.document?.orderLine.map((row, index) => (
                                <StyledTableRow key={row.lineItem.idvalue} onClick={() => showProductDetails(row.lineItem.idvalue)}>
                                    <TableCell align={"center"}>{(index + 1)}</TableCell>
                                    <TableCell>{row.lineItem.item.nameValue}</TableCell>
                                    <TableCell>{row.lineItem.item.sellersItemIdentification.idvalue}</TableCell>
                                </StyledTableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Paper>
        </>

    )
}
