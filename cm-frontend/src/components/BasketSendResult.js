import {makeStyles} from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import {Button} from "@material-ui/core";
import React from "react";
import {Alert, AlertTitle} from "@material-ui/lab";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import {StyledTableCell, StyledTableRow} from "../pages/ProductListPage";
import {DataRow, DataView} from "./ProductDetail";
import {useHistory} from "react-router";

export default function BasketSendResult(props) {

    const {showSuccess = false, sentBasketData} = props;

    const {orderList, basket} = sentBasketData;

    const [stateShowSuccess, setStateShowSuccess] = React.useState(showSuccess);

    const useStyles = makeStyles((theme) => ({
        paper: {
            padding: theme.spacing(2),
            marginBottom: theme.spacing(2),
        },
    }));

    const {push} = useHistory();

    const showRowDetails = (rowId) => {
        push('/order/' + rowId);
    }

    const classes = useStyles();

    return (
        <>
            {stateShowSuccess && (
                <Alert severity="success" variant={"outlined"} style={{marginBottom: "1em", marginTop: "1em"}}>
                    <AlertTitle>Success</AlertTitle>
                    <div>{orderList.length} order{orderList.length > 1 ? 's' : ''} in the basket {orderList.length > 1 ? 'are' : 'is'} successfully generated and scheduled for sending.</div>
                    <div style={{marginTop: "10px"}}>You can either:
                        <ul>
                            <li>copy and save link to the whole basket with all orders to track their status together;</li>
                            <li>download all orders in XML format;</li>
                            <li>copy and save links to each order separately to track their status;</li>
                            <li>download each order in XML format separately.</li>
                        </ul>
                    </div>
                    <Button variant={"outlined"} color={"primary"} size={"small"} onClick={() => setStateShowSuccess(false)}>Close</Button>
                </Alert>
            )}

            <Paper className={classes.paper}>

                <DataView name="Sent" value={basket.createTime}/>
                <DataView name="Orders" value={basket.orderCount}/>
                <DataView name="Lines" value={basket.lineCount}/>
                <DataRow name={'Actions'}>
                    <Button variant={"outlined"} size={"small"} color="primary" style={{marginRight: '1em'}}>Download all</Button>
                    <Button variant={"outlined"} size={"small"} color="primary">Copy basket link</Button>
                </DataRow>
            </Paper>
            <Paper className={classes.paper}>

                <TableContainer style={{marginTop: "2em"}}>
                    <Table size="small" aria-label="Items table">
                        <TableHead>
                            <TableRow>
                                <StyledTableCell align="center">Order</StyledTableCell>
                                <StyledTableCell align="left">Status</StyledTableCell>
                                <StyledTableCell align="left">Supplier</StyledTableCell>
                                <StyledTableCell align="left">Order number</StyledTableCell>
                                <StyledTableCell align="left">Lines</StyledTableCell>
                                <StyledTableCell align="center">Actions</StyledTableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {orderList?.map((row, index) => (
                                <StyledTableRow key={row.id} onClick={() => showRowDetails(row.id)}>
                                    <TableCell align={"center"}>{(index + 1)}</TableCell>
                                    <TableCell>{row.status}</TableCell>
                                    <TableCell>{row.supplierName}</TableCell>
                                    <TableCell>{row.orderNumber}</TableCell>
                                    <TableCell>{row.lineCount}</TableCell>
                                    <TableCell align={"center"}>
                                        <Button size={"small"} color="primary">Download</Button>
                                        <Button size={"small"} color="primary">Copy link</Button>
                                    </TableCell>
                                </StyledTableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Paper>
        </>

    )
}
