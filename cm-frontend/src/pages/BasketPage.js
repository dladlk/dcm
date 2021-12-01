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

    const {basketData, changeBasket} = props;

    const [isLoading, setLoading] = React.useState(false);
    const [productList, setProductList] = React.useState({});
    const [reloadCount, setReloadCount] = React.useState(0);

    const classes = useStyles();

    const {push} = useHistory();

    const refreshAction = () => {
        setReloadCount(reloadCount + 1);
    }
    const sendAction = () => {
    }

    const callLoadProducts = () => {
        loadProducts().finally(() => setLoading(false))
    };

    async function loadProducts() {
        if (!basketData.isEmpty()) {
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
                    console.log(productMapById);
                    setProductList(productMapById);
                }
            ).catch(error => {
                console.log('Error occurred: ' + error.message);
            });
        }
    }

    React.useEffect(callLoadProducts, [reloadCount]);

    const showRowDetails = (productId) => {
        push('/product/view/' + productId);
    }

    return (
        <>
            <PageHeader name="Basket" refreshAction={refreshAction}>
                <Fab color="primary" aria-label="Send order" size="small" title={"Send order"}>
                    <SendIcon onClick={() => sendAction()}/>
                </Fab>
            </PageHeader>

            {(isLoading || false) ? (
                <Paper className={classes.paper}>
                    <CircularProgress/>
                </Paper>
            ) : (
                <>
                    <OrderHeader />
                    <OrderLineList basketData={basketData} showRowDetails={showRowDetails} changeBasket={changeBasket} productList={productList}/>
                </>
            )}
        </>
    );
}
