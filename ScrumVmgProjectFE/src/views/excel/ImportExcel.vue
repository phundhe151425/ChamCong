<template>
  <span>
    <el-button v-b-modal="'my-modal'" type="danger" round file icon="el-icon-upload2"
    >Nhập file
    </el-button>
    <b-modal id="my-modal" centered size="xl"  >
      <template #modal-header="{ close }">
        <h5>{{header}}</h5>
        <b-icon-x-circle-fill style="color: #d8363a" size="lg" @click="close()">
        </b-icon-x-circle-fill>
      </template>
      <form id="formFile" @submit.prevent="getData">
        <input type="file" name="file" />
        <el-button native-type="submit" type="primary" round >Xem trước dữ liệu</el-button>
        <el-button v-if="format==2" round @click="downloadExample()">Tải xuống mẫu</el-button>
        <el-button v-if="format==1" round @click="downloadExample()">Tải xuống mẫu</el-button>
      </form>

      <el-table
          v-if="format==1"
            :hidden="check"
            :data="tableData"
          height="500"

          style="width: 100%;font-size: 16px">
      <el-table-column
          prop="fullName"
          label="Họ và tên"
          width="180">
      </el-table-column>
      <el-table-column
          prop="username"
          label="Email"
          width="250">
      </el-table-column>
      <el-table-column
          prop="code"
          label="Mã NV"
          width="100"
      >
      </el-table-column>
        <el-table-column
            prop="department"
            label="Phòng ban"
            width="250">
      </el-table-column>
        <el-table-column
            prop="role"
            label="Chức vụ">
      </el-table-column>
      <el-table-column
          prop="gender"
          label="Giới tính">
      </el-table-column>

    </el-table><el-table
          v-if="format==2"
            :hidden="check"
            :data="tableData"
          height="500"
            style="width: 100%;font-size: 16px">
      <el-table-column
          prop="user.fullName"
          label="Họ và tên"
          width="180">
      </el-table-column>
      <el-table-column
          prop="user.code"
          label="Mã NV"
          width="100">
      </el-table-column>
        <el-table-column
            prop="timeIn"
            label="Giờ vào"
            width="200">
      </el-table-column>
      <el-table-column
            prop="timeOut"
            label="Giờ ra"
            width="200">
      </el-table-column>
      <el-table-column
          prop="signs.name"
          label="Chấm công">
      </el-table-column>
      <el-table-column
          prop="totalWork"
          label="Tổng giờ làm"
          width="200">
      </el-table-column>
    </el-table>
      <template #modal-footer="{ok}">
        <b-button variant="success" @click="save(),ok()">Lưu</b-button>
        <b-button variant="danger" @click="ok()">Hủy</b-button>

      </template>
    </b-modal>
  </span>

</template>
<script>
import ExcelService from "@/services/excel-service";
import UserService from "@/services/user.service";
import LogdetailService from "@/services/logdetail-service";
export default {
  name: "ImportExcel",
  props: {
    header: String,
    format:String
  },
  data() {
    return {
      check:true,
      tableData: []
    };
  },
  methods:{
    getData(){
      let form = document.querySelector("#formFile");
      if(this.format==1){
        ExcelService.importUser(form).then(respone => {
          console.log(respone.data)
          this.tableData=respone.data
          this.check=false;
        }).catch(error =>{
          console.log(error)
          this.$swal.fire({
            title: "Thêm file thất bại!",
            icon: "error",
            text:error.response.data.error.message,
            timer: 2000,
            timerProgressBar: true,
          });
        })
      }
      else {
        ExcelService.importLog(form).then(respone => {
          console.log(respone.data)
          this.tableData=respone.data
          this.check=false;
        }).catch(error =>{
          console.log(error)
          this.$swal.fire({
            title: "Thêm file thất bại!",
            icon: "error",
            text:error.response.data.error.message,
            timer: 2000,
            timerProgressBar: true,
          });
        })
      }
    },
    downloadExample(){
      this.$emit('downloadExample');
    },
    save(){
      if(this.format==1){
        UserService.save(this.tableData).then(response => {
          this.$swal.fire({
            title: "Thêm người dùng thành công",
            icon: "success",
            type:"success",
            text:response.data.message,
            timer: 2000,
            timerProgressBar: true,
          });
          this.$emit('getData');
        }).catch(error => {
          this.$swal.fire({
            title: "Thêm file thất bại!",
            icon: "error",
            text:error.response.data.error.message,
            timer: 2000,
            timerProgressBar: true,
          });
        })
      }
      else
        LogdetailService.save(this.tableData).then(response => {
          this.$swal.fire({
            title: "Thêm chấm công thành công",
            icon: "success",
            type:"success",
            text:response.data.message,
            timer: 2000,
            timerProgressBar: true,
          });
          this.$emit('getData');
        }).catch(error => {
          this.$swal.fire({
            title: "Thêm file thất bại!",
            icon: "error",
            text:error.response.data.error.message,
            timer: 2000,
            timerProgressBar: true,
          });
        })

    }

  },

}
</script>

<style scoped>

</style>