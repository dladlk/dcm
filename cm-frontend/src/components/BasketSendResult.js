import {makeStyles} from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import {Button, Card, CardActions, CardContent, CardHeader, Grid} from "@material-ui/core";
import React from "react";
import {Alert, AlertTitle} from "@material-ui/lab";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import {StyledTableCell, StyledTableRow} from "../pages/ProductListPage";

export default function BasketSendResult() {

    const useStyles = makeStyles((theme) => ({
        paper: {
            padding: theme.spacing(2),
            marginBottom: theme.spacing(2),
        },
        order: {
            padding: theme.spacing(2),
            textAlign: "center",
        },
        links: {
            padding: theme.spacing(2),
            display: 'flex',
            flexFlow: 'row wrap',
            placeContent: 'space-evenly',
        }
    }));

    const classes = useStyles();

    const orderDataList = [
        {
            id: "b42f4f4f3gfegsfdgadsfasdfasdf",
            orderNumber: "20211205-131812-01",
            supplierName: "Danish Supplier A/S",
            status: "Generated",
        },
        {
            id: "b422434vfsdfdsfaf24ff",
            orderNumber: "20211205-131812-02",
            supplierName: "Norwegian Supplier ApS",
            status: "Downloaded",
        },
    ]

    return (
        <Paper className={classes.paper}>
            <Alert severity="success" variant={"outlined"} style={{marginBottom: "1em"}}>
                <AlertTitle>Success</AlertTitle>
                {orderDataList.length} order{orderDataList.length > 1 ? 's' : ''} are successfully generated
            </Alert>

            <TableContainer>
                <Table className={classes.table} size="small" aria-label="Items table">
                    <TableHead>
                        <TableRow>
                            <StyledTableCell align="left">Order</StyledTableCell>
                            <StyledTableCell align="left">Status</StyledTableCell>
                            <StyledTableCell align="left">Supplier</StyledTableCell>
                            <StyledTableCell align="left">Order number</StyledTableCell>
                            <StyledTableCell align="center">Actions</StyledTableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {orderDataList?.map((row, index) => (
                            <StyledTableRow key={row.id}>
                                <TableCell>{(index + 1)}</TableCell>
                                <TableCell>{row.status}</TableCell>
                                <TableCell>{row.supplierName}</TableCell>
                                <TableCell>{row.orderNumber}</TableCell>
                                <TableCell align={"center"}>
                                    <Button size={"small"} color="primary">Download</Button>
                                    <Button size={"small"} color="primary">Copy link</Button>
                                </TableCell>
                            </StyledTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Grid container spacing={2} style={{display: 'none'}}>
                {orderDataList.map((o, index) => (
                    <Grid item sm={12} md={4}>

                        <Card sx={{maxWidth: 345}} style={{marginBottom: '1em'}}>
                            <CardHeader title={"Order " + (index + 1)}/>
                            <CardContent>
                                <div>{o.supplierName}</div>
                                <div>
                                    <span>Number: </span>
                                    {o.orderNumber}
                                </div>
                                <div>Status: {o.status}</div>
                            </CardContent>
                            <CardActions>
                                <Button variant="outlined" size={"small"} color="primary">Open</Button>
                                <Button variant="outlined" size={"small"} color="primary">Download</Button>
                                <Button variant="outlined" size={"small"} color="primary">Copy link</Button>
                            </CardActions>
                        </Card>

                    </Grid>
                ))}
            </Grid>
        </Paper>
    )
}
