<template>
  <div className="container" style="text-align: center;">
    <br>
    <h5 style="font-weight: 600;">
      Phòng ban: {{departmentName}}&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
      Nhân viên: {{fullName}}

    </h5>
    <br><br>
    <form ac>
      <div className="block" class="text-end">
        <span className="demonstration">Thời gian</span> &ensp;&ensp;&ensp;&ensp;
        <el-date-picker style="width: 30%; margin-right: 140px"
                        v-model="dateRange"
                        type="daterange"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        range-separator=""
                        start-placeholder="Từ ngày"
                        end-placeholder="Đến ngày"
                        @change="getAllByDate">
        </el-date-picker>
      </div>
    </form>
    <br><br>
    <div >
      <el-table
          :data="logs"
          :header-cell-style="{ background: '#D9D9D9', color: 'black', align: 'center'}"
          style="width: 85%; display: inline-block"
          :row-class-name="tableRowClassName">
        <el-table-column
            prop="user.code"
            label="Mã nhân viên">
        </el-table-column>
        <el-table-column
            prop="user.fullName"
            label="Họ và tên">
        </el-table-column>
        <el-table-column
            prop="date_log"
            label="Ngày">
        </el-table-column>
        <el-table-column
            prop="timeIn"
            label="Giờ vào">
        </el-table-column>
        <el-table-column
            prop="timeOut"
            label="Giờ ra">
        </el-table-column>
      </el-table>
    </div>

    <el-pagination class="text-end" style=" margin-right: 140px"
        background
        layout="prev, pager, next"
        :total="totalItems"
        :page-size="pageSize"
        @current-change="handlePageChange">
    </el-pagination>
  </div>
</template>

<script>
import LogdetailService from "@/services/logdetail-service";
export default {
  name: 'HomeVue',
  data() {
    return {
      user_code:  "",
      departmentName: "",
      fullName: "",
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
      return this.$store.state.auth.user;
    },
  },
  created() {
    this.getUserCode();
    this.getParams();
  },
  mounted() {

    this.getAllByDate()

    this.getAllByDate();

  },
  methods: {
    getParams(){
      this.departmentName = this.$route.params.departmentName
      this.fullName = this.$route.params.fullName

    },
    getUserCode(){
      if(this.$route.params.code == null){
        this.user_code = this.currentUser.user.code;
        console.log(this.$route.params.code)
      }
      else{
        this.user_code = this.$route.params.code
        console.log(this.$route.params.code)
      }
    },

    getAllByDate(){
      this.from = this.dateRange.at(0);
      this.to = this.dateRange.at(1);
      console.log(this.from,this.to)
      const params ={
        'code': this.user_code,
        'from': this.from,
        'to': this.to
      }
      LogdetailService.getByDate_UserCode(params).then(response => {
        this.logs = response.data.content;
        this.page = response.data.pageable;
        this.totalItems = response.data.totalElements;
      }).catch(error => {
        console.log(error);
      })
    },
    handlePageChange(value) {
      this.page = value - 1;
      this.getAllByDate();
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