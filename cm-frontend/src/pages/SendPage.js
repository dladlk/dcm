import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import CircularProgress from "@material-ui/core/CircularProgress";
import {useHistory} from "react-router";
import PageHeader from '../components/PageHeader';
import {Fab} from "@material-ui/core";
import SendIcon from "@material-ui/icons/Send";
import DataService from "../services/DataService";
import OrderLineList from "../components/OrderLineList";
import OrderHeader from "../components/OrderHeader";
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


export default function SendPage(props) {

    const {basketData, changeBasket, orderData} = props;

    const [isLoading, setLoading] = React.useState(false);
    const [isSending, setSending] = React.useState(false);
    const [productList, setProductList] = React.useState({});
    const [reloadCount, setReloadCount] = React.useState(0);

    const [errorMessage, setErrorMessage] = React.useState(null);
    const [errorProductIdSet, setErrorProductIdSet] = React.useState(null);

    const classes = useStyles();

    const {push} = useHistory();

    const refreshAction = () => {
        setReloadCount(reloadCount + 1);
    }
    const sendAction = () => {
        sendBasket().finally(() => setSending(false));
    }

    let imitateError = false;

    async function sendBasket() {
        if (!basketData.isEmpty() && !orderData.isEmpty()) {
            setErrorMessage(null);
            setSending(true);
            await DataService.sendBasket(basketData, orderData).then(response => {
                    let responseData = response.data;
                    console.log(responseData);

                    if (imitateError) {
                        responseData.success = false;
                        responseData.errorMessage = "Some error message";
                        responseData.errorProductIdList = basketData.getOrderLineList().slice(0, 1).map((ol) => ol.productId);
                    }

                    if (responseData.success) {
                        changeBasket(null, 0);
                        const basketId = responseData.basketId;
                        push("/basket/"+basketId+"?ok=1");
                    } else {
                        setErrorMessage(responseData.errorMessage);
                        setErrorProductIdSet(new Set(responseData.errorProductIdList));
                    }
                }
            ).catch(error => {
                console.log('Error occurred: ' + error.message);
                setErrorMessage(error.message);
            });
        }
    }

    const callLoadProducts = () => {
        loadProducts().finally(() => setLoading(false))
    };

    async function loadProducts() {
        if (!basketData.isEmpty()) {
            setErrorMessage(null);
            setLoading(true);
            const productIdList = basketData.getOrderLineList().map((orderLine) => orderLine.productId);
            await DataService.fetchProductsByIds(productIdList).then(response => {
                    let responseData = response.data;
                    console.log(responseData);
                    const productMapById = {}
                    for (const index in responseData) {
                        const p = responseData[index];
                        productMapById[p.id] = p;
                    }
                    setProductList(productMapById);
                }
            ).catch(error => {
                console.log('Error occurred: ' + error.message);
                setErrorMessage(error.message);
            });
        }
    }

    React.useEffect(callLoadProducts, [reloadCount]);

    const showRowDetails = (productId) => {
        push('/product/view/' + productId);
    }

    return (
        <>
            <PageHeader name="Send basket" refreshAction={(isSending) ? null : refreshAction}>
                <Fab color="primary" aria-label="Send order" size="small" title={"Send order"} disabled={isSending}>
                    {isSending ? (
                        <CircularProgress/>
                    ) : (
                        <SendIcon onClick={() => sendAction()}/>
                    )}
                </Fab>
            </PageHeader>

            {(isLoading) ? (
                <Paper className={classes.paper}>
                    <CircularProgress/>
                </Paper>
            ) : (
                <>
                    <>
                        {errorMessage && (
                            <Alert severity="error" variant={"outlined"} style={{marginBottom: "1em", marginTop: "1em"}}>
                                <AlertTitle>Error</AlertTitle>
                                <div>{errorMessage}</div>
                            </Alert>
                        )}
                        <OrderHeader orderData={orderData} lockControls={isSending}/>
                        <OrderLineList basketData={basketData} showRowDetails={showRowDetails} changeBasket={changeBasket} productList={productList} lockControls={isSending} errorProductIdSet={errorProductIdSet}/>
                    </>
                </>
            )}
        </>
    );
}
