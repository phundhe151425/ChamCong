import axios from 'axios';
import authHeader from './auth-header';
import { BASE_URL } from "@/http-common";
const API_URL = BASE_URL + '/department/';

class DepartmentDataService {
    getAllDepartment() {
        return axios.get(API_URL, {headers: authHeader()});
    }
}

export default new DepartmentDataService();