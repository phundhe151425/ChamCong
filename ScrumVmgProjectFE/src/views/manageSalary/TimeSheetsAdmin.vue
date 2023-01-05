<template>
  <div style="padding-bottom: 36px;">
    <div className="container" style="text-align: center; width: 90%;margin: auto">
      <el-row :gutter="20">
        <el-col :md="6" :lg="6" :xl="5" >
          <div class="grid-content" style="margin-bottom: 20px">
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

        <el-col :md="6" :lg="6" :xl="5" style="margin-bottom: 20px;">
          <div class="grid-content">
            <span>Phòng ban</span> &ensp;
            <el-select
                v-model="departmentId"
                @change="getAll"
                placeholder="Chon Phòng ban"
            >
              <el-option value="0" label="All Users"></el-option>
              <el-option
                  v-for="item in departments"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </div>
        </el-col>
        <el-col :md="6" :lg="6" :xl="5" style="">
          <div class="grid-content">
            <span style="">Tìm kiếm</span> &ensp;
            <el-input v-model="search" @input="getAll" size="medium" placeholder="Tìm theo tên, email, phòng ban"
                      style="width: 240px;margin-bottom: 10px"/>
          </div>
        </el-col>
        <el-col :md="6" :lg="6" :xl="5" >
          <div class="grid-content buttons">
            <div >
              <import-excel header="Thêm dữ liệu chấm công" format=2 @getData="getAll" @downloadExample="downloadExample"/>
            </div>
          </div>
        </el-col>
      </el-row>
      <br/>
      <div>
        <el-table
            height="780px"
            :default-sort="{prop: 'date_log', order: 'descending'}"
            :data="logs"
            :header-cell-style="{ background: '#D9D9D9', color: 'black', align: 'center'}"
            style="width: 100%; display: inline-block; border-radius: 10px;box-shadow: rgb(149 157 165 / 20%) 0px 8px 24px;"
            :row-class-name="tableRowClassName">
          <el-table-column label="STT" type="index" align="center" width="100px"></el-table-column>
          <el-table-column label="Mã nhân viên" prop="user.code" align="center" width="150px"></el-table-column>
          <el-table-column label="Họ và tên" prop="user.fullName" align="center"></el-table-column>
          <el-table-column label="Phòng ban" prop="user.departments.name" align="center"></el-table-column>
          <el-table-column label="Email" prop="user.username" align="center"></el-table-column>
          <el-table-column label="Ngày" prop="dateLog" align="center" sortable width="150px"></el-table-column>
          <el-table-column label="Giờ vào" prop="timeIn" align="center" width="150px"></el-table-column>
          <el-table-column label="Giờ ra" prop="timeOut" align="center" width="150px"></el-table-column>
          <el-table-column prop="totalWork" label="Số giờ làm" width="150px" align="center"></el-table-column>

        </el-table>
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
  </div>

</template>

<script>
import ExcelService from "@/services/excel-service";
import LogdetailService from "@/services/logdetail-service";
import DepartmentService from "@/services/department.service";
import ImportExcel from "@/views/excel/ImportExcel";

export default {
  name: "HomeVue",
  components: {ImportExcel},
  data() {
    return {
      hoverRowIndex: null,
      user_code: "",
      dateRange: "",
      from: "",
      to: "",
      logs: [],
      search: "",
      totalItems: 0,
      page: 0,
      pageSize: 30,
      departments: [],
      departmentId: "",
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
  mounted() {
    this.getAll();
    this.getUserCode();
  },

  created() {
    DepartmentService.getAllDepartment()
        .then((response) => {
          this.departments = response.data;
          console.log(this.departments);
        })
        .catch((e) => {
          console.log(e);
        });
  },
  methods: {
    getUserCode() {
      this.user_code = this.currentUser.user.code;
      console.log("user code" + this.user_code);
      // console.log("user code"+curr)
    },
    downloadExample(){
      const link = document.createElement('a')
      link.href = 'http://localhost:8080/LogChamCong_FileMau.xlsx'
      link.setAttribute('download', 'LogChamCong.xlsx')
      document.body.appendChild(link)
      link.click()
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
      this.getAll();
    },
    searchLogs() {
      const params = {
        page: this.page,
        pageSize: this.pageSize,
        key: this.search,
        date: this.search,
      };
      console.log(params);
      LogdetailService.search(params)
          .then((response) => {
            this.logs = response.data.blogs;
            this.totalItems = response.data.totalItems;
            // this.page = response.data.page;
          })
          .catch((error) => {
            console.log(error);
          });
    },
    tableRowClassName({rowIndex}) {
      if (rowIndex % 2 === 1) {
        return 'warning-row';
      } else if (rowIndex % 2 === 0) {
        return 'success-row';
      }
      return 'success-row';
    },
    cellClassName({row, rowIndex}) {
      row.rowIndex = rowIndex
      if (rowIndex === this.hoverRowIndex) {
        return 'select-row'
      }
    },
  },
};
</script>
<style>

* {
  font-size: 16px;
}

.el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: #c9f5eb !important;

}

.el-date-picker:focus {
  cursor: default;
}

.el-table .warning-row {
  background: #EDEDED;
}


.el-table .success-row {
  background: #F5F5F5;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
  text-align: left;
}
@media only screen and (min-width: 280px){
  .el-col-md-6 {
    width: 80%;
  }

  .buttons{
    text-align: left;
  }

}

@media only screen and (min-width: 992px){
  .el-col-md-6 {
    width: 100%;
  }
  .buttons{
    text-align: left;
  }
}

@media only screen and (min-width: 1665px){
  .el-col-md-6 {
    width: 23%;
  }

  .buttons{
    text-align: right;
  }
}


</style>