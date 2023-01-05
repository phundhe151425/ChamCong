<template>
  <div>
    <un-permist v-if="checkAuth==false"></un-permist>
    <div id="snapShoot" class="container" style="background-color: white" >
      <div v-show="isCameraOpen">
        <video  ref="camera" :width="450"
                :height="337.5" autoplay>
        </video>
        <el-button v-if="checkAuth==true" type="button" class="button-camera" @click="handleTakePhoto" round>
          <img v-if="checkAuth==true"
              src="../assets/camera.png"
              style="width: 50px; height: 50px"
          />
        </el-button>
      </div>
      <div v-if="checkAuth==true" class="position-relative text-center mt-5 row" v-show="!isCameraOpen">
        <div v-show="isPhotoTaken" class="col-lg-5 me-3">
          <canvas
              id="photoTaken"
              ref="canvas"
              width="560px"
              height="560px"
          ></canvas>
        </div>
        <div v-if="isPhotoTaken" class="col-lg-5 mt-5 position-relative">
          <div id="log-swal-view"></div>
          <div v-if="faceSuccess" class="row flex-row-reverse">
            <h1
                style="
            text-align: center;
            padding: 20px;
            font-weight: bold;

          "
            >
              THÔNG TIN NHÂN VIÊN
            </h1>
            <div class="col-lg-6">
              <div class="about-text go-to">
                <div class="row about-list">
                  <div
                      style="border-bottom: 1px solid black; width: 500px"
                      class="media">
                    <span class="float-start" style="font-size: 20px"> Họ và tên nhân viên: </span>
                    <span style="float: right; margin-right: 6px; font-size: 20px">
                  {{ currentUser.fullName }}
                </span>
                  </div>
                  <div
                      style="border-bottom: 1px solid black; width: 500px"
                      class="media"
                  >
                    <span class="float-start" style="font-size: 20px"> Giới tính: </span>
                    <span style="float: right; margin-right: 6px; font-size: 20px">
                  {{ currentUser.gender }}
                </span>
                  </div>
                  <div
                      style="border-bottom: 1px solid black; width: 500px"
                      class="media"
                  >
                    <span class="float-start" style="font-size: 20px"> Chức vụ: </span>
                    <span
                        class="text-muted mb-0"
                        v-for="(role, index) in currentUser.roles"
                        :key="index"
                    >
                  <span
                      style="float: right; margin-right: 6px; font-size: 20px"
                      v-if="role.name == 'ROLE_USER'"
                  >Nhân viên</span
                  >
                  <span
                      style="float: right; margin-right: 6px; font-size: 20px"
                      v-if="role.name == 'ROLE_MANAGE'"
                  >Trưởng phòng</span
                  >
                  <span
                      style="float: right; margin-right: 6px; font-size: 20px"
                      v-if="role.name == 'ROLE_ADMIN'"
                  >Phòng nhân sự</span
                  >
                </span>
                  </div>
                  <div
                      style="border-bottom: 1px solid black; width: 500px"
                      class="media"
                  >
                    <span class="float-start" style="font-size: 20px"> Email: </span>
                    <span style="float: right; margin-right: 6px; font-size: 20px">
                  {{ currentUser.username }}
                </span>
                  </div>
                  <div
                      style="border-bottom: 1px solid black; width: 500px"
                      class="media"
                  >
                    <span class="float-start" style="font-size: 20px"> Phòng ban: </span>
                    <span style="float: right; margin-right: 6px; font-size: 20px">
                    {{ currentUser.departments.name }}
                </span>
                  </div>
                  <div
                      style="border-bottom: 1px solid black; width: 500px; margin-bottom: 50px"
                      class="media"
                  >
                    <span class="float-start" style="font-size: 20px"> Mã nhân viên: </span>
                    <span style="float: right; margin-right: 6px; font-size: 20px">
                  {{ currentUser.code }}
                </span>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-lg-6">
              <div class="about-avatar">
                <!--            currentUser.user.cover-->
                <img v-if="currentUser.cover!=null"
                     v-bind:src="
              `http://localhost:8080/` + currentUser.cover
            "
                     width="250px"
                     height="250px"
                />
                <img v-if="currentUser.cover==null"
                     src="../assets/FakeUser.png"
                     width="250px"
                     height="250px"

                />
              </div>
            </div>
          </div>
          <div v-if="!faceSuccess">
            <h1
                style="
            text-align: center;
            color: #E81919;
            padding: 20px;
            font-size: 32px;
          "
            >
              KHÔNG TÌM THẤY NHÂN VIÊN
            </h1>
            <img src='../assets/LoiChamCong.png'>
          </div>
          <div class="position-absolute bottom-0 float-end" >
            <span>Thời gian chụp</span>
            <span v-if="userInfo!=null" style="margin-left: 20px;margin-right: 60px">{{userInfo.time }}</span>
            <span v-if="userInfo==null" style="margin-left: 20px;margin-right: 60px">{{new Date()}}</span>
            <el-button  type="primary" v-if="faceSuccess"  @click="handleConfirm" >Xác nhận</el-button>
            <el-button  type="danger" plain @click="handleOpenCamera" >Chụp lại</el-button>
          </div>
        </div>
        <section v-show="!isPhotoTaken" class="p10 ">
          <a><img class="open-camera" src="../assets/ChupAnh.png" @click="handleOpenCamera"></a>
        </section>
      </div>
    </div>
  </div>


</template>
<script>
import logdetailService from "@/services/logdetail-service";
import FaceIdentified from "@/services/face-identified";
import LogdetailService from "@/services/logdetail-service";
import UserService from "@/services/user.service";
import UnPermist from "@/views/error/UnPermist";

export default {
  name: "App",
  components: {UnPermist},
  data() {
    return {
      isCameraOpen: false,
      isPhotoTaken: false,
      isLoading: false,
      faceSuccess:false,
      userInfo:null,
      currentUser:null,
      checkAuth:false
    };
  },
  created(){

  },
  methods: {
    createCameraElement() {
      this.isLoading = true;
      const constraints = (window.constraints = {
        audio: false,
        video: true,
      });
      navigator.mediaDevices
          .getUserMedia(constraints)
          .then((stream) => {
            this.isLoading = false;
            this.$refs.camera.srcObject = stream;
          })
          .catch((error) => {
            this.isLoading = false;
            alert("May the browser..." + error);
          });
    },
    handleOpenCamera(){
      this.isPhotoTaken=false
      this.isCameraOpen=true
      this.openFullscreen()
    },
   openFullscreen() {
      var elem = document.getElementById("snapShoot");
      if (elem.requestFullscreen) {
        elem.requestFullscreen();
      } else if (elem.webkitRequestFullscreen) { /* Safari */
        elem.webkitRequestFullscreen();
      } else if (elem.msRequestFullscreen) { /* IE11 */
        elem.msRequestFullscreen();
      }
    },
    handleConfirm(){
      const infoKeepLog = {
        "code":this.userInfo.id_staff,
        "time":this.userInfo.time
      }
      LogdetailService.faceTimeKeepLog(infoKeepLog).then(()=>{
        this.$swal.fire({
          title: "Chấm công thành công",
          icon: "success",
          timer: 1000,
          customClass: {
            container: 'log-swal'
          },
          target: document.getElementById("log-swal-view"),
        });
      })
    },
    handleTakePhoto() {
      this.isPhotoTaken = true;
      this.isCameraOpen=false;
      const context = this.$refs.canvas.getContext("2d");
      context.drawImage(this.$refs.camera, 0, 0, 560, 560);
      const canvas = document.getElementById("photoTaken");
      var newUrl = canvas.toDataURL('image/jpeg', 0.92);
      var imgBase64 = newUrl.split("data:image/jpeg;base64,")[1].toString();
      console.log(imgBase64)
      var data ={
        "img_base64": imgBase64
      }
      this.faceTime(data)
    },
    faceTime(data){
      // Axios
      FaceIdentified.faceTimeKeep(data).then( respone =>{
        if(respone.data.local_message === "Chấm công thành công"){
          this.faceSuccess=true;
          console.log(this.faceSuccess)
          this.userInfo=respone.data.data.person[0];
          this.getUser();
          this.sendImage();
        }
        else {
          this.faceSuccess=false;
          console.log(respone.data)
        }
      }).catch(error => {
        console.log(error)
        this.faceSuccess=false;
      })
    },
    sendImage() {
      // Gửi ảnh lưu log
      const canvas = document.getElementById("photoTaken");
      let code = this.userInfo.id_staff;
        canvas.toBlob(function (blob) {
          var formData = new FormData()
          formData.append('file', blob, 'image.jpg');
          formData.append('code',code);
          formData.append('fullName', "Pham Hai Trieu");
          logdetailService.sendImg(formData).then(respone=>{
              console.log(respone.data)
          })
        }, 'image/jpeg')


    },
    checkIP(){
      fetch('https://api.ipify.org?format=json')
          .then(x => x.json())
          .then(({ ip }) => {
            if(ip=='113.190.246.14')
                this.checkAuth= true;
            console.log(this.checkAuth)
          });
    },
    getUser() {
      console.log(this.userInfo)
      const params ={
        code :this.userInfo.id_staff
      }
      UserService.getUserByCode(params).then(res => {
        this.currentUser= res.data
      })

    },
  },
  mounted() {
    this.createCameraElement();
    this.checkIP();
  }
};
</script>

<style>
.log-swal {
  z-index: 2147483600;
}
video {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  min-width: 100%;
  min-height: 100%;
  z-index: -1;
  -webkit-transition: all 1s;
  -moz-transition: all 1s;
  -o-transition: all 1s;
  transition: all 1s;
}
.button-camera{
  background-color: #666;
  border: medium none;
  color: #666;
  display: block;
  font-size: 18px;
  left: 50%;
  margin: 0 auto;
  padding: 8px 16px;
  position: absolute;
  bottom: 30px;
}
.button-camera.active {
  background-color: #0077a2;
}
/*.open-camera:hover{*/
/*  transform: scale(1.15);*/
/*  transition: all .2s ease-in-out*/
/*}*/
*,
*:before,
*:after {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
}

section a {
  text-transform: uppercase;
  text-align: center;
  text-decoration: none;
  font-size: 4.875em;
  font-weight: 900;
  color: #fff;
}
section a:after, section a:before {
  content: '';
}
section.head {
  background-color: #fff;
  position: relative;
  z-index: 10;
}
section.head a {
  width: 652px;
  height: 78px;
  line-height: 78px;
  position: absolute;
  top: 50%;
  left: 50%;
  margin-left: -326px;
  margin-top: -39px;
  font-weight: 200;
  color: #BABABA;
  -webkit-transition: all 2s cubic-bezier(0.21, 1, 0.84, 1.01);
  -moz-transition: all 2s cubic-bezier(0.21, 1, 0.84, 1.01);
  transition: all 2s cubic-bezier(0.21, 1, 0.84, 1.01);
}
section.head a:before, section.head a:after {
  color: #000;
  font-weight: 900;
  font-size: 30px;
  text-transform: none;
  display: block;
  -webkit-transition: inherit;
  -moz-transition: inherit;
  transition: inherit;
}
section.head a:before {
  content: attr(data-bf);
  position: absolute;
  top: -45px;
  left: -5px;
}
section.head a:after {
  content: attr(data-af);
  position: absolute;
  right: 0;
  bottom: -30px;
}
section.head a:hover {
  -webkit-transform: scale(1.05);
  -moz-transform: scale(1.05);
  -ms-transform: scale(1.05);
  -o-transform: scale(1.05);
  transform: scale(1.05);
}
section.head a:hover:before {
  -webkit-transform: scale(1.2) translate(-25px, -15px);
  -moz-transform: scale(1.2) translate(-25px, -15px);
  -ms-transform: scale(1.2) translate(-25px, -15px);
  -o-transform: scale(1.2) translate(-25px, -15px);
  transform: scale(1.2) translate(-25px, -15px);
}
section.head a:hover:after {
  -webkit-transform: scale(1.2) translate(25px, 15px);
  -moz-transform: scale(1.2) translate(25px, 15px);
  -ms-transform: scale(1.2) translate(25px, 15px);
  -o-transform: scale(1.2) translate(25px, 15px);
  transform: scale(1.2) translate(25px, 15px);
}
section.p10 {
  background: #f5f7fa;
}

section.p10 a:before, section.p10 a:after {
  content: '';
  position: absolute;
  width: 40px;
  height: 40px;
  border-color: #FF0000;
  border-style: solid;
  border-width: 0;
  -webkit-transition: all 0.5s cubic-bezier(1, 0.2, 0.26, 0.7);
  -moz-transition: all 0.5s cubic-bezier(1, 0.2, 0.26, 0.7);
  transition: all 0.5s cubic-bezier(1, 0.2, 0.26, 0.7);
  -webkit-transform: translate(0px, 0px) scale(0.8);
  -moz-transform: translate(0px, 0px) scale(0.8);
  -ms-transform: translate(0px, 0px) scale(0.8);
  -o-transform: translate(0px, 0px) scale(0.8);
  transform: translate(0px, 0px) scale(0.8);
  opacity: 0;
}
section.p10 a:before {
  left: 0;
  bottom: 0;
  border-bottom-width: 1px;
  border-left-width: 1px;
}
section.p10 a:after {
  top: 0;
  right: 0;
  border-top-width: 1px;
  border-right-width: 1px;
}
section.p10 a:hover:before, section.p10 a:hover:after {
  -webkit-transition: all 2s cubic-bezier(0.14, 1.13, 0, 0.91);
  -moz-transition: all 2s cubic-bezier(0.14, 1.13, 0, 0.91);
  transition: all 2s cubic-bezier(0.14, 1.13, 0, 0.91);
  opacity: 1;
}
section.p10 a:hover:before {
  -webkit-transform: translate(-30px, 20px) scale(1);
  -moz-transform: translate(-30px, 20px) scale(1);
  -ms-transform: translate(-30px, 20px) scale(1);
  -o-transform: translate(-30px, 20px) scale(1);
  transform: translate(-30px, 20px) scale(1);
  border-bottom-width: 12px;
  border-left-width: 12px;
}
section.p10 a:hover:after {
  -webkit-transform: translate(30px, -20px) scale(1);
  -moz-transform: translate(30px, -20px) scale(1);
  -ms-transform: translate(30px, -20px) scale(1);
  -o-transform: translate(30px, -20px) scale(1);
  transform: translate(30px, -20px) scale(1);
  border-top-width: 12px;
  border-right-width: 12px;
}

@-webkit-keyframes voavoa2 {
  from {
    -webkit-clip-path: polygon(13% 0, 100% 43%, 100% 100%);
    clip-path: polygon(13% 0, 100% 43%, 100% 100%);
  }
  to {
    -webkit-clip-path: polygon(0 45%, 100% 43%, 100% 100%);
    clip-path: polygon(0 45%, 100% 43%, 100% 100%);
  }
}
@-moz-keyframes voavoa2 {
  from {
    -webkit-clip-path: polygon(13% 0, 100% 43%, 100% 100%);
    clip-path: polygon(13% 0, 100% 43%, 100% 100%);
  }
  to {
    -webkit-clip-path: polygon(0 45%, 100% 43%, 100% 100%);
    clip-path: polygon(0 45%, 100% 43%, 100% 100%);
  }
}
@keyframes voavoa2 {
  from {
    -webkit-clip-path: polygon(13% 0, 100% 43%, 100% 100%);
    clip-path: polygon(13% 0, 100% 43%, 100% 100%);
  }
  to {
    -webkit-clip-path: polygon(0 45%, 100% 43%, 100% 100%);
    clip-path: polygon(0 45%, 100% 43%, 100% 100%);
  }
}
</style>

