import Axios from "axios";

const apiUrl = "http://localhost:8080/api";
//const apiUrl = "/api";

const fetchProductDetails = (productId) => {
    return fetch(apiUrl + "/product/" + productId);
}

const fetchProducts = (search) => {
    return fetch(apiUrl + "/products" + (search ? "?search="+search : ""));
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