import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import CircularProgress from "@material-ui/core/CircularProgress";
import PageHeader from '../components/PageHeader';
import DataService from "../services/DataService";
import {useParams} from "react-router";
import {Alert, AlertTitle} from "@material-ui/lab";
import OrderSendResult from "../components/OrderSendResult";

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


export default function OrderPage() {

    const [isLoading, setLoading] = React.useState(false);
    const [errorMessage, setErrorMessage] = React.useState(null);
    const [sentOrderData, setSentOrderData] = React.useState(null);
    const [reloadCount, setReloadCount] = React.useState(0);
    const classes = useStyles();

    const {id} = useParams();

    React.useEffect(() => {
        loadOrder(id).finally(() => setLoading(false));
    }, [id, reloadCount]);

    async function loadOrder(id) {
        setErrorMessage(null);
        setLoading(true);
        await DataService.fetchSentOrderData(id).then(response => {
            let responseData = response.data;
            console.log(responseData);
            setSentOrderData(responseData);
        }).catch((error) => {
            console.log('Error occurred: ' + error.message);
            setErrorMessage(error.message);
        });
    }

    const refreshAction = () => {
        setReloadCount(reloadCount + 1);
    }

    return (
        <>
            <PageHeader name="Order details" refreshAction={refreshAction}/>

            {(isLoading) ? (
                <Paper className={classes.paper}>
                    <CircularProgress/>
                </Paper>
            ) : (
                <>
                    {errorMessage && (
                        <Alert severity="error" variant={"outlined"} style={{marginBottom: "1em", marginTop: "1em"}}>
                            <AlertTitle>Error</AlertTitle>
                            <div>{errorMessage}</div>
                        </Alert>
                    )}
                    {sentOrderData && (
                        <OrderSendResult sentOrderData={sentOrderData}/>
                    )}
                </>
            )}
        </>
    );

}