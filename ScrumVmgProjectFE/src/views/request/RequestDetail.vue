<template>
  <div class="container ">
    <router-link to="/managerequest" class="btn-back"><i class="el-icon-back"></i>&nbsp;&nbsp;Danh sách đề xuất</router-link>
    <br><br>
    <h5 class="title-request">{{ request.title }}</h5>
    <br><br>
    <div style="width: 100%">
      <span>Trạng thái:</span>&nbsp;&nbsp;
      <button v-if="request.approveStatus.id==1"  class="btn-1" >
        {{ request.approveStatus.name }}
      </button>
      <button v-if="request.approveStatus.id==2" class="btn-2">
        {{ request.approveStatus.name }}
      </button>
      <button v-if="request.approveStatus.id==3" class="btn-3">
        {{ request.approveStatus.name }}
      </button>
      <button v-if="request.approveStatus.id==4" class="btn-4">
        {{ request.approveStatus.name }}
      </button>
      <div v-for="(item, index) in request.approvers" :key="index">
        <button class="btn-accept"  v-if="request.approveStatus.id==1 && item.id==currentUser.user.id" @click="changeStatus(request.id, 2,request.approveStatus.id, currentUser.user.id)">Chấp thuận</button>
        <button class="btn-refuse" v-if="request.approveStatus.id==1 && item.id==currentUser.user.id" @click="changeStatus(request.id, 3,request.approveStatus.id,currentUser.user.id)">Từ chối</button>
        <button class="btn-undo" v-if="(request.approveStatus.id==2 || request.approveStatus.id==3) && item.id==currentUser.user.id" @click="changeStatus(request.id, 1,request.approveStatus.id,currentUser.user.id)">Hoàn tác</button>
        <span class="btn-refuse" v-if="request.approveStatus.id==5">Đã hủy</span>
        <span class="btn-refuse" v-if="request.approveStatus.id==6">Hoàn thành</span>
      </div>

    </div>
    <br><br>
    <span class="title-request sub-title">Thông tin đề xuất</span>
    <hr style="margin: 0">
    <div class="d-flex">
      <div class="col-6 left">
        <br>
        <table>
          <tr>
            <td class="header-table">Nhân viên:</td>
            <td>{{ request.creator.fullName }}</td>
          </tr>
          <tr>
            <td class="header-table">Mã nhân viên:</td>
            <td>{{ request.creator.code }}</td>
          </tr>
          <tr>
            <td class="header-table">Phòng ban:</td>
            <td>{{ request.creator.departments.name }}</td>
          </tr>
        </table>
      </div>
      <div class="col-6 right">
        <br>
        <table>
          <tr>
            <td class="header-table">Loại đề xuất:</td>
            <td>Đề xuất {{ request.catergoryRequest.name }}: {{ request.categoryReason.name }}</td>
          </tr>
          <tr v-if="request.categoryReason.id!=6">
            <td class="header-table">Thời gian tạo:</td>
            <td><span v-if="request.timeStart!=null">{{request.timeStart}},</span> <span v-if="request.dateFrom!=null">Ngày: {{request.dateFrom}}</span></td>
          </tr>
          <tr v-if="request.categoryReason.id!=6">
            <td class="header-table">Thời gian thực hiện:</td>
            <td><span v-if="request.timeEnd!=null">{{request.timeEnd}},</span> <span v-if="request.dateTo!=null">Ngày: {{request.dateTo}}</span></td>
          </tr>
          <tr v-if="request.categoryReason.id!=6">
            <td class="header-table">Số ngày nghỉ:</td>
            <td>{{request.totalDays}} ngày</td>
          </tr>
          <tr v-if="request.categoryReason.id==6">
            <td class="header-table">Chấm công ngày:</td>
            <td>{{request.dateForget}}</td>
          </tr>
          <tr>
            <td class="header-table">Nội dung đề xuất:</td>
            <td>{{ request.content }}</td>
          </tr>
        </table>
      </div>
    </div>

    <div class="d-flex">
      <div class="col-6 left">
        <br>
        <span class="title-request sub-title">Người phê duyệt</span>
        <hr style="margin: 0; width: 90%">
        <br>
        <div v-for="(item, index) in request.approvers" :item="item"
             :index="index"
             :key="item.id">
          <b-avatar style="margin-left: 12px" variant="info" v-if="item.cover != null"
                    v-bind:src="`http://localhost:8080/` + item.cover"></b-avatar>
          <b-avatar style="margin-left: 12px" variant="info" v-if="item.cover == null"
                    src="../assets/user.jpg"></b-avatar>
          <span style="font-weight: 600">&nbsp; {{ item.fullName }}</span>
          <br><br>
        </div>

      </div>
      <div class="col-6 right">
        <br>
        <span class="title-request sub-title">Người theo dõi</span>
        <hr style="margin: 0; width: 90%">
        <br>
        <div v-for="(item, index) in request.followers" :item="item"
             :index="index"
             :key="item.id">
          <b-avatar style="margin-left: 12px" variant="info" v-if="item.cover != null"
                    v-bind:src="`http://localhost:8080/` + item.cover"></b-avatar>
          <b-avatar style="margin-left: 12px" variant="info" v-if="item.cover == null"
                    src="../assets/user.jpg"></b-avatar>
          <span style="font-weight: 600">&nbsp; {{ item.fullName }}</span>
          <br><br>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import RequestService from "@/services/request-service";

export default {
  name: "RequestDetail",
  data() {
    return {
      request: [],
      requestId: '',
    }

  },
  created() {
    this.getParams()
    this.getRequest()
  },
  computed: {

    currentUser() {
      // return JSON.parse(localStorage.getItem('user'));
      return this.$store.state.auth.user;
    },
  },
  methods: {
    getParams() {
      this.requestId = this.$route.params.id
      console.log(this.requestId)
    },
    getRequest() {
      this.requestId = this.$route.params.id
      RequestService.getRequest(this.requestId).then(response => {
        this.request = response.data
        console.log(this.request)
      })
    },
    changeStatus(requestId, newStatusId, oldStatusId,approvedId) {
      if (newStatusId == 1) {
        this.$swal
            .fire({
              title: "Xác nhận hoàn tác",
              showDenyButton: true,
              confirmButtonColor: "#75C4C0",
              confirmButtonText: "Hoàn Tác",
              denyButtonColor: "#ED9696",
              denyButtonText: "Đóng",
              customClass: {
                actions: "my-actions",
                cancelButton: "order-1 right-gap",
                confirmButton: "order-2",
                denyButton: "order-3",
              },
            })
            .then((result) => {
              if (result.isConfirmed) {
                RequestService.changeStatus(requestId, newStatusId, oldStatusId,approvedId).then((response) => {
                  this.$swal.fire({
                    title: response.data.message,
                    icon: "success",
                    timer: 2000,
                    timerProgressBar: true,
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    width: "24em",
                  });
                  this.getRequest();
                });
              } else if (result.isDenied) {
                this.$swal.fire({
                  title: "Thay đổi thất bại",
                  icon: "error",
                  timer: 2000,
                  timerProgressBar: true,
                  toast: true,
                  position: "top-end",
                  showConfirmButton: false,
                  width: "24em",
                });
              }
            });
      }
      if (newStatusId == 2) {
        this.$swal
            .fire({
              title: "Xác nhận chấp thuận",
              showDenyButton: true,
              confirmButtonColor: "#75C4C0",
              confirmButtonText: "Chấp thuận",
              denyButtonColor: "#ED9696",
              denyButtonText: "Đóng",
              customClass: {
                actions: "my-actions",
                cancelButton: "order-1 right-gap",
                confirmButton: "order-2",
                denyButton: "order-3",
              },
            })
            .then((result) => {
              if (result.isConfirmed) {
                RequestService.changeStatus(requestId, newStatusId, oldStatusId,approvedId).then((response) => {
                  this.$swal.fire({
                    title: response.data.message,
                    icon: "success",
                    timer: 2000,
                    timerProgressBar: true,
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    width: "24em",
                  });
                  this.getRequest();
                });
              } else if (result.isDenied) {
                this.$swal.fire({
                  title: "Thay đổi thất bại",
                  icon: "error",
                  timer: 2000,
                  timerProgressBar: true,
                  toast: true,
                  position: "top-end",
                  showConfirmButton: false,
                  width: "24em",
                });
              }
            });
      }
      if (newStatusId == 3) {
        this.$swal
            .fire({
              title: "Xác nhận từ chối",
              showDenyButton: true,
              confirmButtonColor: "#75C4C0",
              confirmButtonText: "Tù chối",
              denyButtonColor: "#ED9696",
              denyButtonText: "Đóng",
              customClass: {
                actions: "my-actions",
                cancelButton: "order-1 right-gap",
                confirmButton: "order-2",
                denyButton: "order-3",
              },
            })
            .then((result) => {
              if (result.isConfirmed) {
                RequestService.changeStatus(requestId, newStatusId, oldStatusId,approvedId).then((response) => {
                  this.$swal.fire({
                    title: response.data.message,
                    icon: "success",
                    timer: 2000,
                    timerProgressBar: true,
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    width: "24em",
                  });
                  this.getRequest();
                });
              } else if (result.isDenied) {
                this.$swal.fire({
                  title: "Thay đổi thất bại",
                  icon: "error",
                  timer: 2000,
                  timerProgressBar: true,
                  toast: true,
                  position: "top-end",
                  showConfirmButton: false,
                  width: "24em",
                });
              }
            });
      }
      if (newStatusId == 4) {
        this.$swal
            .fire({
              title: "Xác nhận hủy yêu cầu",
              showDenyButton: true,
              confirmButtonColor: "#75C4C0",
              confirmButtonText: "Xác nhận",
              denyButtonColor: "#ED9696",
              denyButtonText: "Đóng",
              customClass: {
                actions: "my-actions",
                cancelButton: "order-1 right-gap",
                confirmButton: "order-2",
                denyButton: "order-3",
              },
            })
            .then((result) => {
              if (result.isConfirmed) {
                RequestService.changeStatus(requestId, newStatusId, oldStatusId,approvedId).then((response) => {
                  this.$swal.fire({
                    title: response.data.message,
                    icon: "success",
                    timer: 2000,
                    timerProgressBar: true,
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    width: "24em",
                  });
                  this.getRequest();
                });
              } else if (result.isDenied) {
                this.$swal.fire({
                  title: "Thay đổi thất bại",
                  icon: "error",
                  timer: 2000,
                  timerProgressBar: true,
                  toast: true,
                  position: "top-end",
                  showConfirmButton: false,
                  width: "24em",
                });
              }
            });
      }
    },
  }
}
</script>

<style scoped>
.container {

}

.btn-back {
  color: #4787F3;
  text-decoration: none;
}

.title-request {
  text-transform: uppercase;
}

.sub-title {
  color: #828282;
}

.btn-1 {
  cursor: default;
  color: #DF8620;
  background-color: #FAECDB;
  border: none;
  border-radius: 20px;
  padding: 3px 20px;
}

.btn-2 {
  cursor: default;
  color: #2BBB6E;
  background-color: #DEF7EA;
  border: none;
  border-radius: 20px;
  padding: 3px 20px;
}

.btn-3 {
  cursor: default;
  color: #BF2C31;
  background-color: #F7DEDF;
  border: none;
  border-radius: 20px;
  padding: 3px 20px;
}

.btn-4 {
  cursor: default;
  color: #6C6F93;
  background-color: #E4E5F1;
  border: none;
  border-radius: 20px;
  padding: 3px 20px;
}

.btn-refuse {
  float: right;
  color: #FFFFFF;
  background-color: #E24146;
  border: none;
  border-radius: 8px;
  padding: 6px 30px;
  margin-right: 10px;
}

.btn-accept {
  float: right;
  color: #FFFFFF;
  background-color: #75C4C0;
  border: none;
  border-radius: 8px;
  padding: 6px 30px;
  /*margin-right: 5px;*/
}

.btn-refuse:hover {
  float: right;
  color: #FFFFFF;
  background-color: #de1d23;
  border: none;
  border-radius: 8px;
  padding: 6px 30px;
  margin-right: 10px;
}

.btn-accept:hover {
  float: right;
  color: #FFFFFF;
  background-color: #06bdb3;
  border: none;
  border-radius: 8px;
  padding: 6px 30px;
  /*margin-right: 5px;*/
}

.btn-undo{
  float: right;
  color: #FFFFFF;
  background-color: #e28743;
  border: none;
  border-radius: 8px;
  padding: 6px 30px;
  /*margin-right: 5px;*/
}

.btn-undo:hover{
  float: right;
  color: #FFFFFF;
  background-color: #eab676;
  border: none;
  border-radius: 8px;
  padding: 6px 30px;
  /*margin-right: 5px;*/
}

.header-table {
  font-weight: 700;
  width: 180px;
}

td {
  padding: 20px 0;
}

</style>