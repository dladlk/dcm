import Axios from "axios";

//const apiUrl = "http://localhost:8080/api";
const apiUrl = "/dcm/api";

const fetchProductDetails = (productId) => {
    return fetch(apiUrl + "/products/" + productId);
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
    uploadFiles,
}

export default DataService;