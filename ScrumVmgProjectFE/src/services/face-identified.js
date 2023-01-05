import {faceAPI} from "@/http-common";

class FaceIdentified{
    faceTimeKeep(data){
        return faceAPI.post("/face_timekeep",data)
    }
    addFace(data){
        return faceAPI.post("/new_staff",data)
    }

}
export default new FaceIdentified();