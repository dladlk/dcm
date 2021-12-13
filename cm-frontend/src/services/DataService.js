import Axios from "axios";
import {API_URL} from "./DataServiceConfig"

Axios.defaults.timeout = 10000;

const apiUrl = API_URL;

const fetchProductDetails = (productId) => {
    return Axios.get(apiUrl + "/products/" + productId);
}
const fetchSentBasketData = (basketId) => {
    return Axios.get(apiUrl + "/basket/" + basketId);
}
const fetchSentOrderData = (orderId) => {
    return Axios.get(apiUrl + "/order/" + orderId);
}

const fetchProducts = (search, page = 0, size = 20) => {
    const params = {
        page: page,
        size: size,
    }
    if (search) {
        params.search = search;
    }
    return Axios.get(apiUrl + "/products", {
        params: params
    });
}

const sendBasket = (basketData, orderData) => {
    return Axios.post(apiUrl + "/basket/send", {
        basketData: basketData,
        orderData: orderData,
    });
}

const fetchProductsByIds = (productIdList = []) => {
    return Axios.post(apiUrl + "/products_by_ids", {
        ids: productIdList
    });
}

const uploadFiles = (formData) => {
    return Axios.post(apiUrl + "/upload", formData);
}

const DataService = {
    fetchProductDetails,
    fetchProducts,
    fetchProductsByIds,
    sendBasket,
    fetchSentBasketData,
    fetchSentOrderData,
    uploadFiles,
}

export default DataService;