import Axios from "axios";

const apiUrl = "http://localhost:8080/api";
//const apiUrl = "/api";

const fetchProductDetails = (productId) => {
    return fetch(apiUrl + "/products/" + productId);
}

const fetchProducts = (search) => {
    return Axios.get(apiUrl + "/products" + (search ? "?search="+search : ""));
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