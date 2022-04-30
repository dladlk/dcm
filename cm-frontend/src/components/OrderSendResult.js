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
import {ViewToggle} from "../pages/ProductDetailPage";
import SmallSnackbar from "./SmallSnackbar";
import {copyCurrentUrlToClipboard} from "../services/ClipboardService";

export default function OrderSendResult(props) {

    const {sentOrderData} = props;

    const [viewMode, setViewMode] = React.useState("table");
    const [showSnackBar, setShowSnackBar] = React.useState(false);

    const handleViewChange = (event) => {
        setViewMode(event.target.value);
    }
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
                <DataView name="Sent" value={sentOrderData.createTime.$date}/>
                <DataView name="Status" value={sentOrderData.status}/>
                <DataView name="Supplier" value={sentOrderData.supplierName}/>
                <DataView name="Order number" value={sentOrderData.orderNumber}/>
                <DataView name="Lines" value={sentOrderData.lineCount}/>
                <DataView name="Index in basket" value={(sentOrderData.orderIndex + 1)}/>
                <DataRow name={'Actions'}>
                    <Button variant={"outlined"} size={"small"} color="primary" style={{marginRight: '1em'}} onClick={() => showBasketDetails(sentOrderData.basketId)}>View basket</Button>
                    <Button variant={"outlined"} size={"small"} color="primary" style={{marginRight: '1em'}}>Download</Button>
                    <Button variant={"outlined"} size={"small"} color="primary" onClick={() => copyCurrentUrlToClipboard(() => setShowSnackBar(true))}>Copy link</Button>
                </DataRow>
            </Paper>

            <ViewToggle viewMode={viewMode} handleViewChange={handleViewChange}/>

            <Paper className={classes.paper}>

                {viewMode === "json" ? (
                    <pre style={{whiteSpace: 'pre-wrap', wordBreak: 'break-all'}}>{JSON.stringify(sentOrderData, null, 2)}</pre>
                ) : (

                    <TableContainer>
                        <Table size="small" aria-label="Items table">
                            <TableHead>
                                <TableRow>
                                    <StyledTableCell align="center">Line</StyledTableCell>
                                    <StyledTableCell align="left">Quantity</StyledTableCell>
                                    <StyledTableCell align="left">Name</StyledTableCell>
                                    <StyledTableCell align="left">Seller number</StyledTableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {sentOrderData.document?.orderLine.map((row, index) => (
                                    <StyledTableRow key={row.lineItem._id.value} onClick={() => showProductDetails(row.lineItem._id.value)}>
                                        <TableCell align={"center"}>{(index + 1)}</TableCell>
                                        <TableCell>{row.lineItem.quantity.value} {row.lineItem.quantity.unitCode}</TableCell>
                                        <TableCell>{row.lineItem.item.name.value}</TableCell>
                                        <TableCell>{row.lineItem.item.sellersItemIdentification._id.value}</TableCell>
                                    </StyledTableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}

            </Paper>

            <SmallSnackbar opened={showSnackBar} hide={() => setShowSnackBar(false)}/>
        </>

    )
}
