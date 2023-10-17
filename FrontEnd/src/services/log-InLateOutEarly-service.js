import httpCommon from "@/http-common";
// import axios from "axios";
// import { BASE_URL } from "@/http-common";

class LogInLateOutEarly {

    getData(params) {
        return httpCommon.get("/logInLateOutEarly/data", {params});
    }

}

export default new LogInLateOutEarly();