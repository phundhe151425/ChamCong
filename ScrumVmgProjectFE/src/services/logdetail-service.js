import httpCommon from "@/http-common";
import axios from "axios";

class LogdtailService{
    getAll(params){
       return  httpCommon.get("/log/logList", {params});
    }

    getByUsers(params){
        return  httpCommon.get("/log/byUser", {params});
    }

    getByDate(params) {
        return httpCommon.get("/log/byDate", {params});
    }
    getByDepartment(params) {
        return httpCommon.get("/log/byDepartment", {params});
    }
    getAllByUserAndTime(params){
        return  httpCommon.get("/log/allByUserAndTime", {params});
    }
    getAllByMonthAndDepartment(params){
        return httpCommon.get("/log/allByMonthAndDepartment",{params})
    }

    getDepartment() {
        return httpCommon.get("department");
    }

    getLogsByDate_Department(params){
        return httpCommon.get("/log/byDate_Department", {params});
    }

    getByDate_UserCode(params){
        return httpCommon.get("/log/byDate_Usercode", {params});
    }

    getAllBySearch(params){
        return httpCommon.get("/log/search", {params});
    }
    updateLog(logEdit){
        return axios.post("http://localhost:8080/api/log/edit",logEdit);
    }
    save(data) {
        return axios.post("http://localhost:8080/api/log/create",data);
    }
    sendImg(data) {
        return axios.post("http://localhost:8080/api/log/saveImageLog",data);
    }
    faceTimeKeepLog(data){
        return axios.post("http://localhost:8080/api/log/timeKeep",data)
    }
}

export default new LogdtailService();
