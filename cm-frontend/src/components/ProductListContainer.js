import React from "react";
import {Route, useHistory} from "react-router-dom";
import ProductListPage from "../pages/ProductListPage";
import Banner from "./Banner";
import UploadPage from "../pages/UploadPage";
import ProductDetailPage from "../pages/ProductDetailPage";
import TopNav from "./TopNav";
import DataService from "../services/DataService";
import useStickyState from '../utils/useStickyState';
import {createBasketData} from "./BasketData";
import {getOrderData} from "./OrderData";
import SendPage from "../pages/SendPage";
import BasketPage from "../pages/BasketPage";

const currentPosition = (list, id) => {
    if (list._cachedPos) {
        if (list._cachedPos[id]) {
            return list._cachedPos[id] - 1; // Cache position with + 1 - so 0 is not considered as absent
        }
    } else {
        list._cachedPos = {};
    }
    if (!list) {
        return 0;
    }
    for (let i = 0; i < list.length; i++) if (list[i].id === id) {
        list._cachedPos[id] = (i + 1);
        return i;
    }
    return 0;
};

export const listNavigator = (list) => {
    return {
        hasNext: (id) => {
            return currentPosition(list, id) < list.length - 1
        },
        hasPrevious: (id) => {
            return currentPosition(list, id) > 0
        },
        getNext: (id) => {
            return '/product/view/' + list[currentPosition(list, id) + 1].id
        },
        getPrevious: (id) => {
            return '/product/view/' + list[currentPosition(list, id) - 1].id
        },
    }
}

export function ProductListContainer() {

    const [showBanner, setShowBanner] = useStickyState(true, 'dcm-banner');
    const [productList, setProductList] = React.useState([]);
    const [productListPage, setProductListPage] = React.useState(0);
    const [productListPageSize, setProductListPageSize] = React.useState(20);
    const [productListTotal, setProductListTotal] = React.useState(0);
    const [productListLoading, setProductListLoading] = React.useState(false);

    const [basketData, setBasketData] = React.useState(createBasketData());
    const changeBasket = (productId, quantity) => {
        setBasketData(basketData.changeBasket(productId, quantity));
    }

    const setBannerClosed = () => {
        setShowBanner(false);
    };
    const setBannerOpened = () => {
        setShowBanner(true);
    };

    const history = useHistory();
    const searchAction = (...params) => {
        history.push('/');
        loadProducts(...params);
    };

    React.useEffect(() => {
        loadProducts();
    }, []);

    async function loadProducts(search = null, page = 0, size = 20) {
        console.log("Load products: " + search + " " + page + " " + size);
        setProductListLoading(true);
        await DataService.fetchProducts(search, page, size).then(response => {
                let responseData = response.data;
                console.log(responseData);
                setProductList(responseData.content);
                setProductListPage(responseData.number);
                setProductListPageSize(responseData.size);
                setProductListTotal(responseData.totalElements);
                setProductListLoading(false);
            }
        ).catch(error => {
            console.log('Error occurred: ' + error.message);
            setProductListLoading(false);
        });
    }

    return (
        <>
            <TopNav aboutAction={setBannerOpened} searchAction={searchAction} showBasketBar={!basketData.isEmpty()}/>
            <Banner opened={showBanner} closeAction={setBannerClosed}/>
            <Route exact path="/">
                <ProductListPage list={productList} isLoading={productListLoading} refreshAction={searchAction} page={productListPage} pageSize={productListPageSize} total={productListTotal}/>
            </Route>
            <Route exact path="/send">
                <SendPage changeBasket={changeBasket} basketData={basketData} orderData={getOrderData()}/>
            </Route>
            <Route exact path="/upload" component={UploadPage}/>
            <Route path="/product/view/:id">
                <ProductDetailPage navigator={listNavigator(productList, 1)} changeBasket={changeBasket} basketData={basketData}/>
            </Route>
            <Route path="/basket/:id">
                <BasketPage />
            </Route>
        </>
    );
}
