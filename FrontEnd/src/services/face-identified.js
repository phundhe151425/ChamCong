import {faceAPI} from "@/http-common";
// import axios from 'axios';
//
// const API_URL = 'https://192.168.45.45:5001/';

class FaceIdentified {
    faceTimeKeep(data) {
        return faceAPI.post("face_timekeep", data)
        // return axios.post(API_URL + 'face_timekeep', data);
    }

    addFace(data) {
        return faceAPI.post("new_staff", data)
    }

}

export default new FaceIdentified();