import Axios from "axios";

const apiUrl = "http://localhost:8080/api";
//const apiUrl = "/api";

const fetchProductDetails = (productId) => {
    return fetch(apiUrl + "/products/" + productId);
}

const fetchProducts = (search) => {
    const params = {
        page: 0,
        size: 5,
    }
    if (search) {
        params.search = search;
    }
    return Axios.get(apiUrl + "/products", {
        params: params
    });
}

const uploadFiles = (formData) => {
    return Axios.post(apiUrl + "/upload", formData);
}

const DataService = {
    fetchProductDetails,
    fetchProducts,
    uploadFiles,
}

export default DataService;