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

export default function BasketPage(props) {

    const {basketData, changeBasket} = props;

    const [isLoading, setLoading] = React.useState(false);

    const classes = useStyles();

    const {push} = useHistory();

    const refreshAction = () => {}
    const sendAction = () => {}

    const showRowDetails = (productId) => {
        push('/product/view/' + productId);
    }

    return (
        <>
            <PageHeader name="Basket" refreshAction={refreshAction}>
                <Fab color="primary" aria-label="Send order" size="small" title={"Send order"}>
                    <SendIcon onClick = {() => sendAction()}/>
                </Fab>
            </PageHeader>
            <Paper className={classes.paper}>
                {(isLoading || false) ? (
                    <CircularProgress/>
                ) : (
                    <>
                        <TableContainer>
                            <Table className={classes.table} size="small" aria-label="Basket contents">
                                <TableHead>
                                    <TableRow>
                                        <StyledTableCell align="left">#</StyledTableCell>
                                        <StyledTableCell align="left">Product id</StyledTableCell>
                                        <StyledTableCell align="left">Quantity</StyledTableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {!basketData.isEmpty() ? basketData.getOrderLineList().map((orderLine, index) => (
                                        <StyledTableRow key={orderLine.productId} onClick={() => showRowDetails(orderLine.productId)}>
                                            <TableCell>{(index + 1)}</TableCell>
                                            <TableCell>{orderLine.productId}</TableCell>
                                            <TableCell>{orderLine.quantity}</TableCell>
                                        </StyledTableRow>
                                    )) : (
                                        <StyledTableRow key={'empty'}>
                                            <TableCell colSpan={3} align="center">Basket is empty</TableCell>
                                        </StyledTableRow>
                                    )}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </>
                )}
            </Paper>
        </>
    );
}
