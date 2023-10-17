import axios from "axios";

export const BASE_URL = 'http://localhost:8100/api';
export const FE_URL = 'https://localhost:8000/';

export const jsonAPI = axios.create({
    baseURL: BASE_URL,
    headers: {
        "Content-type": 'application/json'
    }
})
export const faceAPI = axios.create({
    baseURL: 'https://172.16.106.56:5001/'
})
export default axios.create({
    baseURL: BASE_URL,
    headers: {
        enctype: "multipart/form-data",
        "Content-type": 'application/octet-stream',

        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        processData: false,
    }
})

