<template>
  <div style="padding-bottom: 38px;">
    <div className="container" style="text-align: center; width: 90%;margin: auto">
      <el-row :gutter="20">
        <el-col :lg="9" :xl="6" >
          <div class="grid-content text-start">
            <span>Ngày</span> &ensp;
            <el-date-picker style=""
                            v-model="dateRange"
                            type="daterange"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            range-separator=""
                            start-placeholder="Chọn thời gian"
                            :editable="false"
                            @change="getAllByDate"
            >
            </el-date-picker>
          </div>
        </el-col>
      </el-row>

<!--      <form ac>-->
<!--        <div className="block" class="text-start">-->
<!--          <span className="demonstration">Ngày</span> &ensp;-->
<!--          <el-date-picker style="width: 20%;font-size: 16px;"-->
<!--                          v-model="dateRange"-->
<!--                          type="daterange"-->
<!--                          format="yyyy-MM-dd"-->
<!--                          value-format="yyyy-MM-dd"-->
<!--                          range-separator=""-->
<!--                          start-placeholder="Chọn thời gian"-->
<!--                          @change="getAllByDate">-->
<!--          </el-date-picker>-->
<!--        </div>-->
<!--      </form>-->


      <br>
      <div class="text-center ">
        <el-table
            :data="logs"
            height="800px"
            :default-sort="{prop: 'date_log', order: 'descending'}"
            :header-cell-style="{ background: '#D9D9D9', color: 'black', align: 'center'}"
            style="width: 100%; display: inline-block; border-radius: 10px;box-shadow: rgb(149 157 165 / 20%) 0px 8px 24px;"
            :row-class-name="tableRowClassName">

          <el-table-column type="index" label="STT" width="100px" align="center"></el-table-column>
          <el-table-column prop="user.code" label="Mã nhân viên" width="150px" align="center"></el-table-column>
          <el-table-column prop="user.fullName" label="Họ và tên" align="center"></el-table-column>
          <el-table-column prop="user.departments.name" label="Bộ phận" align="center" ></el-table-column>
          <el-table-column prop="user.username" label="Email" align="center" width="300px"></el-table-column>
          <el-table-column prop="dateLog" sortable label="Ngày" width="150px" align="center"></el-table-column>
          <el-table-column prop="timeIn" label="Giờ vào" width="150px" align="center"></el-table-column>
          <el-table-column prop="timeOut" label="Giờ ra" width="150px" align="center"></el-table-column>
          <el-table-column prop="totalWork" label="Số giờ làm" width="150px" align="center"></el-table-column>

        </el-table>
      </div>

      <el-pagination class="text-end"
                     background
                     layout="prev, pager, next"
                     :total="totalItems"
                     :page-size="pageSize"
                     @current-change="handlePageChange">
      </el-pagination>

    </div>
  </div>

</template>

<script>
// import UserService from '../services/user.service';
import ExcelService from "@/services/excel-service";
import LogdetailService from "@/services/logdetail-service";

export default {
  name: 'HomeVue',
  data() {
    return {
      user_code: "",
      dateRange: [],
      from: "",
      to: "",
      logs: [],
      totalItems: 0,
      page: 0,
      pageSize: 30,
    }
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
    currentUser() {
      console.log(localStorage.getItem('user'))
      // return JSON.parse(localStorage.getItem('user'));
      // console.log("dddd"+this.$store.state.auth.user)
      return this.$store.state.auth.user;

    },
  },
  created() {
    this.getUserCode();

  },
  mounted() {

    this.getAllByDate()
  },
  methods: {
    getUserCode() {
      if (this.$route.params.code == null) {
        this.user_code = this.currentUser.user.code;
        console.log(this.$route.params.code)
      } else {
        this.user_code = this.$route.params.code
        console.log(this.$route.params.code)
      }
    },
    getAllByDate() {
      this.from = this.dateRange !== null ? this.dateRange.at(0) : null;
      this.to = this.dateRange !== null ? this.dateRange.at(1) : null;
      const params = {
        "page": this.page,
        "size": this.size,
        'code': this.user_code,
        'from': this.from,
        'to': this.to
      }
      LogdetailService.getByDate_UserCode(params).then(response => {
        this.logs = response.data.content;
        // this.page = response.data.pageable;
        this.totalItems = response.data.totalElements;
      }).catch(error => {
        console.log(error);
      })

    },
    a() {
      const a = String(this.logs.date_log).split("T")[0]
      this.logs.data_log = a
    },
    exportExcel() {
      ExcelService.exportExcel();
    },
    handlePageChange(value) {
      this.page = value - 1;
      this.getAllByDate();
      // if (this.category !== null) {
      //   this.getBlogs();
      // }else {
      //   this.getBlogs();
      // }
    },
    tableRowClassName({rowIndex}) {
      if (rowIndex % 2 === 1) {
        return 'warning-row';
      } else if (rowIndex % 2 === 0) {
        return 'success-row';
      }
      return 'success-row';
    }
  },
};
</script>

<style>


.el-table .warning-row {
  background: #EDEDED;
}


.el-table .success-row {
  background: #F5F5F5;
}
</style>