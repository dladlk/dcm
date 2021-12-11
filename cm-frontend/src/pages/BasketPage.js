import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import CircularProgress from "@material-ui/core/CircularProgress";
import PageHeader from '../components/PageHeader';
import BasketSendResult from "../components/BasketSendResult";
import DataService from "../services/DataService";
import {useParams} from "react-router";
import {useLocation} from "react-router-dom";
import {Alert, AlertTitle} from "@material-ui/lab";

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


export default function BasketPage() {

    const [isLoading, setLoading] = React.useState(false);
    const [errorMessage, setErrorMessage] = React.useState(null);
    const [sentBasketData, setSentBasketData] = React.useState(null);
    const [reloadCount, setReloadCount] = React.useState(0);
    const classes = useStyles();

    const search = useLocation().search;
    const ok = new URLSearchParams(search).get('ok');
    const {id} = useParams();

    React.useEffect(() => {
        loadBasket(id).finally(() => setLoading(false));
    }, [id, reloadCount]);

    async function loadBasket(id) {
        setErrorMessage(null);
        setLoading(true);
        await DataService.fetchSentBasketData(id).then(response => {
            let responseData = response.data;
            console.log(responseData);
            setSentBasketData(responseData);
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
            <PageHeader name="Basket details" refreshAction={refreshAction}/>

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
                    {sentBasketData && (
                        <BasketSendResult showSuccess={reloadCount === 0 && ok === '1'} sentBasketData={sentBasketData}/>
                    )}
                </>
            )}
        </>
    );

}