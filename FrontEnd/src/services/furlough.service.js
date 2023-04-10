import axios from "axios";
import { BASE_URL } from "@/http-common";

const API_URL = BASE_URL + '/furlough/';

class FurloughService {
    getAllByYear(param) {
        return axios.get(API_URL + "furloughByYear", {params: param})
    }

    getAllByYearAndUser(param) {
        return axios.get(API_URL + "furloughByYearAndUser", {params: param})
    }

    edit(data) {
        return axios.post(API_URL + "edit", data)
    }
}

export default new FurloughService();