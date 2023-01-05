<template style="font-size: 16px">
  <div style="padding-bottom: 4px;">
    <div className="container" style="text-align: center; width: 90%;margin: auto">

      <el-row :gutter="20">
        <el-col :md="6" :lg="6" :xl="5" style="margin-bottom: 20px">
          <div class="grid-content text-start">
            <h5 style="font-weight: 600;">
              Phòng ban: {{ departmentName }}&emsp;
            </h5>
          </div>
        </el-col>

        <el-col :md="6" :lg="6" :xl="5">
          <div class="grid-content text-start" style="margin-bottom: 20px">
            <span>Ngày</span> &ensp;
            <el-date-picker style="width: 250px"
                            v-model="dateRange"
                            type="daterange"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            range-separator=""
                            start-placeholder="Chọn Ngày"
                            :editable="false"
                            @change="getAll"
            >
            </el-date-picker>
          </div>
        </el-col>
        <el-col :md="6" :lg="6" :xl="5">
          <div class="grid-content text-start">
            <span style="">Tìm kiếm</span> &ensp;
            <el-input v-model="search" @input="getAll" size="medium" placeholder="Tìm theo tên, email, phòng ban"
                      style="width: 240px;"/>
          </div>
        </el-col>
        <el-col :md="6" :lg="6" :xl="5">
          <div class="grid-content ">

          </div>
        </el-col>
      </el-row>

<!--      <div style="" class="d-flex">-->
<!--        <div className="block" class="text-start col-10">-->
<!--          <h5 style="font-weight: 600;">-->
<!--            Phòng ban: {{ departmentName }}&emsp;-->
<!--          </h5>-->
<!--          <span className="demonstration">Ngày</span> &ensp;-->
<!--          <el-date-picker style="width: 20%;font-size: 16px;margin-right: 180px"-->
<!--                          v-model="dateRange"-->
<!--                          type="daterange"-->
<!--                          format="yyyy-MM-dd"-->
<!--                          value-format="yyyy-MM-dd"-->
<!--                          range-separator=""-->
<!--                          start-placeholder="Chọn thời gian"-->
<!--                          :editable="false"-->
<!--                          @change="getAll">-->
<!--          </el-date-picker>-->
<!--          <span style="">Tìm kiếm</span> &ensp;-->
<!--          <el-input v-model="search" @input="getAll" size="medium" placeholder="Tìm theo tên, email, phòng ban"-->
<!--                    style="width: 20%;"/>-->
<!--        </div>-->

<!--      </div>-->

      <br/>
      <div>
        <el-table class="text-center " :data="logs"
                  height="780px"
                  :default-sort="{prop: 'date_log', order: 'descending'}"
                  :header-cell-style="{ background: '#D9D9D9', color: 'black', align: 'center'}"
                  style="width: 100%; display: inline-block; font-size: 16px;border-radius: 10px"
                  :row-class-name="tableRowClassName"
        >
            <el-table-column label="ID" type="index" align="center"></el-table-column>
            <el-table-column label="Mã nhân viên" prop="user.code" width="150px" align="center"></el-table-column>
            <el-table-column label="Họ và tên" prop="user.fullName" align="center"></el-table-column>
            <el-table-column label="Phòng ban" prop="user.departments.name" align="center"></el-table-column>
            <el-table-column label="Email" prop="user.username" align="center"></el-table-column>
            <el-table-column label="Ngày" prop="dateLog" sortable width="150px" align="center"></el-table-column>
            <el-table-column label="Giờ vào" prop="timeIn" width="150px" align="center"></el-table-column>
            <el-table-column label="Giờ ra" prop="timeOut" width="150px" align="center"></el-table-column>
          <el-table-column prop="totalWork" label="Số giờ làm" width="150px" align="center"></el-table-column>

        </el-table>

    </div>

    <el-pagination class="text-end"
                   background
                   layout="prev, pager, next"
                   :total="totalItems"
                   :page-size="pageSize"
                   @current-change="handlePageChange"
    >
    </el-pagination>
  </div>
  </div>

</template>

<script>
// import UserService from '../services/user.service';
import ExcelService from "@/services/excel-service";
import LogdetailService from "@/services/logdetail-service";
// import {router} from "@/router";

export default {
  name: "HomeVue",
  data() {
    return {
      user_code: "",
      dateRange: "",
      from: "",
      to: "",
      logs: [],
      search: "",
      departmentId: 0,
      departmentName: "",

      totalItems: 0,
      page: 0,
      pageSize: 30,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  created() {
    this.getDepartment();
  },
  mounted() {
    this.getAll();
    this.getUserCode();
  },
  methods: {
    getUserCode() {
      this.user_code = this.currentUser.user.code;
      // this.user_code = this.currentUser.user.user_code;
      console.log("user code" + this.user_code);
    },

    getDepartment() {
      this.departmentId = this.currentUser.user.departments.id
      this.departmentName = this.currentUser.user.departments.name
    },
    getAll() {
      this.from = this.dateRange !== null ? this.dateRange.at(0) : null;
      this.to = this.dateRange !== null ? this.dateRange.at(1) : null;
      const params = {
        "page": this.page,
        "size": this.size,
        "id": this.departmentId,
        'from': this.from,
        'to': this.to,
        'search': this.search
      }
      LogdetailService.getLogsByDate_Department(params)
          .then((response) => {
            this.logs = response.data.content;
            this.page = response.data.pageable.pageNumber;
            this.totalItems = response.data.totalElements;
            console.log(this.departmentId + "fdafdsafsd")
          })
          .catch((error) => {
            console.log(error);
          });
    },
    exportExcel() {
      ExcelService.exportExcelReport();
    },
    handlePageChange(value) {
      this.page = value - 1;
      this.getAll()
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
* {
  font-size: 16px;
}

.el-table .warning-row {
  background: #EDEDED;
}


.el-table .success-row {
  background: #F5F5F5;
}

.el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: #c9f5eb !important;

}
</style>