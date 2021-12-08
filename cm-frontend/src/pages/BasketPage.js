import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import CircularProgress from "@material-ui/core/CircularProgress";
import PageHeader from '../components/PageHeader';
import BasketSendResult from "../components/BasketSendResult";

const useStyles = makeStyles(theme => ({
    table: {
        minWidth: 600,
    },
    header: {
        marginBottom: '1em'
    },
    paper: {
        padding: theme.spacing(2),
    }
}));


export default function BasketPage(props) {

    const [isLoading, setLoading] = React.useState(false);
    const classes = useStyles();

    return (
        <>
            <PageHeader name="Basket details"/>

            {(isLoading) ? (
                <Paper className={classes.paper}>
                    <CircularProgress/>
                </Paper>
            ) : (
                <>
                    <BasketSendResult showSuccess={false}/>
                </>
            )}
        </>
    );

}