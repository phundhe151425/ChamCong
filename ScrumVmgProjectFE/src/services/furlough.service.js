import axios from "axios";
const API_URL = 'http://localhost:8080/api/furlough/';

 class FurloughService{
     getAllByYear(param){
         return axios.get(API_URL + "furloughByYear", {params:param})
     }
     getAllByYearAndUser(param){
         return axios.get(API_URL + "furloughByYearAndUser", {params:param})
     }
     edit(data){
         return axios.post(API_URL + "edit", data)
     }
 }
 export default new FurloughService();