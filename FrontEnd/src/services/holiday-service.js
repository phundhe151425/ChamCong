import httpCommon from "@/http-common";
import axios from "axios";
import { BASE_URL } from "@/http-common";
const API_URL = BASE_URL + '/holiday/';

class HolidayService {
    getAll(params) {
        return httpCommon.get("/holiday", {params});
    }

    getHoliday(id) {
        return httpCommon.get(`/holiday/${id}`,)
    }

    deleteHoliday(id) {
        return httpCommon.delete("/holiday?id=" + id);
    }

    addHoliday(holiday) {
        let holidayForm = new FormData(holiday)

        return axios.post(API_URL + 'add', holidayForm);
    }

    updateHoliday(id, holiday) {
        let holidayForm = new FormData(holiday)

        return axios.put(API_URL + `${id}`, holidayForm);
    }
}

export default new HolidayService()