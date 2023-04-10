import authHeader from './auth-header';

import httpCommon from "@/http-common";

import { BASE_URL } from "@/http-common";

import axios from 'axios';

class UserService {
    getUser_Department(params) {
        return httpCommon.get("/user-management/users", {params})
    }

    getPublicContent() {
        return httpCommon.get("/user-management" + 'all');
    }

    getUserBoard() {
        return httpCommon.get("/user-management" + 'user', {headers: authHeader()});
    }

    getAllUser() {
        return httpCommon.get("/user-management/getAllUser", {headers: authHeader()});
    }

    getAllUserByDepartmentId(id) {
        return httpCommon.get(`/user-management/getAllUserByDepartmentId/${id}`)
    }

    getAllUserByPositionId() {
        return httpCommon.get("/user-management/getAllUserByPositionId", {headers: authHeader()})
    }


    getModeratorBoard() {
        return httpCommon.get("/user-management" + 'mod', {headers: authHeader()});
    }


    getAdminBoard() {
        return httpCommon.get("/user-management" + 'admin', {headers: authHeader()});
    }

    editUser(id, data) {
        let formData = new FormData(data);
        console.log(formData)
        return axios.put(BASE_URL + `/user-management/user/${id}`, formData)
    }

    getUserById(id) {
        return httpCommon.get(`/user-management/users/${id}`);
    }

    save(data) {
        return axios.post(BASE_URL + "/user-management/create", data);
    }

    forgotPassword(params) {
        return axios.post(BASE_URL + "/mail/reset_password", {}, {params})
    }

    getUserByCode(params) {
        return httpCommon.get(`/user-management/user/id_staff`, {params});
    }
}

export default new UserService();