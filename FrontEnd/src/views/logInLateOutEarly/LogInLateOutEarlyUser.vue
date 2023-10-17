
<template>
    <div style="padding-bottom: 36px;">
        <div className="container" style="text-align: center; width: 90%;margin: auto">
            <h2 class="text-start" style="font-weight: bold">Log In Late/Out Early</h2>
            <br>
            <el-row :gutter="20">
                <el-col :md="6" :lg="6" :xl="5">
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
                                        @change="getData"

                        >
                        </el-date-picker>
                    </div>
                </el-col>
            </el-row>

            <div>
                <el-table
                        height="650px"
                        :default-sort="{prop: 'date_check', order: 'descending'}"
                        :data="logs"
                        :header-cell-style="{ background: '#D9D9D9', color: 'black', align: 'center'}"
                        style="width: 100%; display: inline-block; border-radius: 10px;box-shadow: rgb(149 157 165 / 20%) 0px 8px 24px;"
                        :row-class-name="tableRowClassName">
                    <el-table-column label="STT" type="index" align="center" width="100px"></el-table-column>
                    <el-table-column
                            prop="user.code"
                            label="Mã nhân viên"
                            align="center" sortable >
                    </el-table-column>
                    <el-table-column
                            prop="user.fullName"
                            label="Họ và tên"
                            align="center" sortable>
                    </el-table-column>
                    <el-table-column
                            prop="user.departments.name"
                            label="Phòng ban"
                            align="center" sortable>
                    </el-table-column>
                    <el-table-column label="Email" prop="user.username" align="center"></el-table-column>
                    <el-table-column
                            prop="dateCheck"
                            label="Ngày"
                            align="center" sortable width="150px">
                    </el-table-column>
                    <el-table-column
                        prop="timeEnd"
                        label="Giờ vào muộn"
                        align="center" sortable >
                    </el-table-column>
                    <el-table-column
                            prop="timeStart"
                            label="Giờ ra sớm"
                            align="center" sortable width="150px">
                    </el-table-column>
                    <el-table-column
                        prop="duration"
                        label="Thời gian"
                        align="center" sortable width="150px">
                    </el-table-column>
                    <el-table-column
                        prop="iokind"
                        label="Loại"
                        align="center" sortable width="150px">
                    </el-table-column>
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
// import LogdetailService from "@/services/logdetail-service";
import LogInLateOutEarlyService from "@/services/log-InLateOutEarly-service";
import DepartmentService from "@/services/department.service";

export default {
    name: "LogInLateOutEarlyAdmin",
    data() {
        return {
            user_code: "",
            departmentName: "",
            fullName: "",
            dateRange: [],
            from: "",
            to: "",
            logs: [],
            totalItems: 0,
            page: 0,
            pageSize: 30,
            departments: [],
            departmentId: "",
            search: ''
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
        DepartmentService.getAllDepartment()
            .then((response) => {
                this.departments = response.data;
                console.log(this.departments);
            })
            .catch((e) => {
                console.log(e);
            });
        this.getUserCode();
        this.getParams();
    },
    mounted() {

        this.getData();

    },
    methods: {
        getParams() {
            this.departmentName = this.$route.params.departmentName
            this.fullName = this.$route.params.fullName

        },
        getUserCode() {
            if (this.$route.params.code == null) {
                this.user_code = this.currentUser.user.code;
                console.log(this.$route.params.code)
            } else {
                this.user_code = this.$route.params.code
                console.log(this.$route.params.code)
            }
        },
        getData() {
            this.from = this.dateRange.at(0);
            this.to = this.dateRange.at(1);
            let params;
            if (this.from == null) {
                params = {
                    'page': this.page,
                    'size': this.pageSize,
                    'code': this.user_code,
                    'departmentId': "",
                    'search': this.search,
                    'from': "",
                    'to': this.to
                }
            }
            if (this.to == null) {
                params = {
                    'page': this.page,
                    'size': this.pageSize,
                    'code': this.user_code,
                    'departmentId': "",
                    'search': this.search,
                    'from': this.to,
                    'to': ""
                }
            }
            if (this.to == null && this.from == null) {
                params = {
                    'page': this.page,
                    'size': this.pageSize,
                    'code': this.user_code,
                    'departmentId': "",
                    'search': this.search,
                    'from': "",
                    'to': ""
                }
            }
            if(this.to != null && this.from != null){
                params = {
                    'page': this.page,
                    'size': this.pageSize,
                    'code': this.user_code,
                    'departmentId': "",
                    'search': this.search,
                    'from': this.from,
                    'to': this.to
                }
            }

            LogInLateOutEarlyService.getData(params).then(response => {
                this.logs = response.data.content;
                console.log(this.logs)
                this.page = response.data.pageable.pageNumber;
                this.totalItems = response.data.totalElements;
                console.log(this.page + " " + this.totalItems)
            }).catch(error => {
                console.log(error);
            })
        },
        // getAllByDate() {
        //   this.from = this.dateRange.at(0);
        //   this.to = this.dateRange.at(1);
        //   console.log(this.from, this.to)
        //   const params = {
        //     'code': this.user_code,
        //     'from': this.from,
        //     'to': this.to
        //   }
        //   LogdetailService.getByDate_UserCode(params).then(response => {
        //     this.logs = response.data.content;
        //     this.page = response.data.pageable;
        //     this.totalItems = response.data.totalElements;
        //   }).catch(error => {
        //     console.log(error);
        //   })
        // },
        handlePageChange(value) {
            this.page = value - 1;
            this.getData();
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

@media only screen and (min-width: 280px) {
    .el-col-md-6 {
        width: 80%;
    }

    .buttons {
        text-align: left;
    }

}

@media only screen and (min-width: 992px) {
    .el-col-md-6 {
        width: 100%;
    }

    .buttons {
        text-align: left;
    }
}

@media only screen and (min-width: 1665px) {
    .el-col-md-6 {
        width: 23%;
    }

    .buttons {
        text-align: right;
    }
}


</style>