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

export default function BasketSendResult(props) {

    const {showSuccess = false} = props;

    const useStyles = makeStyles((theme) => ({
        paper: {
            padding: theme.spacing(2),
            marginBottom: theme.spacing(2),
        },
    }));

    const classes = useStyles();

    const basketStatus = {
        sentDate: '01.12.2021 12:54:43',
        id: 'vs094fj34f309jv340',
    }

    const orderDataList = [
        {
            id: "b42f4f4f3gfegsfdgadsfasdfasdf",
            orderNumber: "20211205-131812-01",
            orderLines: 1,
            supplierName: "Danish Supplier A/S",
            status: "Generated",
        },
        {
            id: "b422434vfsdfdsfaf24ff",
            orderNumber: "20211205-131812-02",
            orderLines: 1,
            supplierName: "Norwegian Supplier ApS",
            status: "Downloaded",
        },
    ]

    return (
        <>
            {showSuccess && (
                <Alert severity="success" variant={"outlined"} style={{marginBottom: "1em", marginTop: "1em"}}>
                    <AlertTitle>Success</AlertTitle>
                    <div>{orderDataList.length} order{orderDataList.length > 1 ? 's' : ''} in the basket are successfully generated and scheduled for sending.</div>
                    <div style={{marginTop: "10px"}}>You can either:
                        <ul>
                            <li>copy and save link to the whole basket with all orders to track their status together;</li>
                            <li>download all orders in XML format;</li>
                            <li>copy and save links to each order separately to track their status;</li>
                            <li>download each order in XML format separately.</li>
                        </ul>
                    </div>
                </Alert>
            )}

            <Paper className={classes.paper}>

                <DataView name="Sent" value={basketStatus.sentDate}/>
                <DataView name="Orders" value={orderDataList.length}/>
                <DataView name="Lines" value={orderDataList.reduce((prev, cur) => prev + cur.orderLines, 0)}/>
                <DataRow name={'Actions'}>
                    <Button variant={"outlined"} size={"small"} color="primary" style={{marginRight: '1em'}}>Download all</Button>
                    <Button variant={"outlined"} size={"small"} color="primary">Copy basket link</Button>
                </DataRow>
            </Paper>
            <Paper className={classes.paper}>

                <TableContainer style={{marginTop: "2em"}}>
                    <Table className={classes.table} size="small" aria-label="Items table">
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
                            {orderDataList?.map((row, index) => (
                                <StyledTableRow key={row.id}>
                                    <TableCell align={"center"}>{(index + 1)}</TableCell>
                                    <TableCell>{row.status}</TableCell>
                                    <TableCell>{row.supplierName}</TableCell>
                                    <TableCell>{row.orderNumber}</TableCell>
                                    <TableCell>{row.orderLines}</TableCell>
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
