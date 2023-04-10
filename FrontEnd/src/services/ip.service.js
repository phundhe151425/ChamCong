import httpCommon from "@/http-common";
import { BASE_URL } from "@/http-common";

class IpService {
    getClientIp() {
        return httpCommon.get(BASE_URL+"/client-ip");
    }
}

export default new IpService()