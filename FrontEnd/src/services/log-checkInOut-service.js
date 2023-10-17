import httpCommon from "@/http-common";
// import axios from "axios";
// import { BASE_URL } from "@/http-common";

class LogCheckInOutService {

    getData(params) {
        return httpCommon.get("/logCheckInOut/data", {params});
    }

}

export default new LogCheckInOutService();