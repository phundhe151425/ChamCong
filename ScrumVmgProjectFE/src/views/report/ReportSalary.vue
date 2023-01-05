<template>
  <div style="padding-bottom: 20px">
    <!--     Thanh chọn-->

    <div class="d-flex flex-row mt-3 ms-0">
      <div class="col-9">
        <el-select
          v-if="showAdminBoard"
          class="mx-3 my-3"
          v-model="department"
          @change="getLog"
          placeholder="Chọn Phòng Ban"
        >
          <el-option label="Tất cả" value="" selected> </el-option>
          <el-option
            v-for="(item, index) in departments"
            :key="index"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
        <span v-if="showModeratorBoard" class="mx-3 my-3 fw-bold"
          >Phòng {{ accountDepartment.name }}</span
        >
        <el-input
          class="mx-3 my-3"
          size="medium"
          placeholder="Tìm kiếm nhân viên"
          v-model="search"
          clearable
          @input="handelSearch"
          style="width: 200px"
        >
        </el-input>
        <el-select
          class="mx-3 my-3"
          v-model="currentMonth"
          @change="getLog"
          placeholder="Tháng"
        >
          <el-option
            v-for="item in 12"
            :key="item"
            :value="item"
            :label="`Tháng ` + item"
          >
            Tháng {{ item }}
          </el-option>
        </el-select>

        <!--        Tooltip chú thích-->

        <el-tooltip placement="right" effect="light">
          <el-button
            type="primary"
            class="el-icon-info el-button--info my-3"
            style="border-radius: 10px"
          >
            Chú thích</el-button
          >
          <div class="note-wrapper" slot="content">
            <div class="d-flex flex-column">
              <div style="font-weight: bold">Chú thích</div>
              <div>
                <div class="d-flex flex-row">
                  <el-button round class="h-25 me-3"></el-button>
                  <div style="text-align: justify">
                    <p>
                      <span style="font-weight: bold">H</span>: Làm hành chính
                    </p>
                    <p>
                      <span style="font-weight: bold">P</span> : Nghỉ phép
                    </p>
                    <p>
                      <span style="font-weight: bold">KL</span>: Nghỉ không lương
                    </p>
                    <p>
                      <span style="font-weight: bold">L</span> : Nghỉ lễ
                    </p>
                    <p>
                      <span style="font-weight: bold">Ô</span> : Nghỉ ốm
                    </p>
                    <p>
                      <span style="font-weight: bold">TC</span> : Nghỉ tiêu chuẩn
                    </p>
                    <p>
                      <span style="font-weight: bold">CĐ</span> : Nghỉ chế độ
                    </p>
                  </div>
                </div>
                <div class="d-flex flex-row">
                  <el-button
                    round
                    class="h-25 me-3"
                    style="background-color: #f8cbad"
                  ></el-button>
                  <p><span style="font-weight: bold">NT</span>: Nghỉ tuần</p>
                </div>
                <div class="d-flex flex-row">
                  <el-button
                    round
                    class="h-25 me-3"
                    style="background-color: #e24146"
                  ></el-button>
                  <p>Chỉnh sửa</p>
                </div>
                <div class="d-flex flex-row">
                  <el-button round class="h-25 me-3 position-relative">
                    <svg
                      class="position-absolute top-0 end-0"
                      x="0px"
                      y="0px"
                      viewBox="0 0 512 512"
                      style="
                        enable-background: new 0 0 512 512;
                        width: 10px;
                        fill: #a843a8ff;
                      "
                      xml:space="preserve"
                    >
                      <g>
                        <polygon
                          points="5.8,0.6 0,6.4 0,378.3 5.8,384 384,384 512,512 512,5.8 506.3,0 6.4,0 	"
                        />
                      </g>
                    </svg>
                  </el-button>
                  <p>Comment</p>
                </div>
              </div>
            </div>
          </div>
        </el-tooltip>
      </div>

      <!--     Nút chọn-->

      <div class="col-3">
        <el-button
          @click="exportExcel()"
          type="danger"
          class="el-icon-download float-end ms-3 my-3"
          round
        >
          Xuất File
        </el-button>
        <el-button
          v-if="showAdminBoard"
          v-b-modal="'save-modal'"
          type="primary"
          class="el-icon-edit-outline float-end my-3"
          round
        >
          Cập nhật
        </el-button>
      </div>
    </div>
    <!--   BẢNG CHẤM CÔNG-->
    <div
      class="table-responsive-xxl"
      style="
        margin-top: 50px;
        background-color: white;
        width: 100%;
        display: inline-block;
        box-shadow: rgb(149 157 165 / 20%) 0px 8px 24px;
      "
    >
      <table v-if="showAdminBoard" class="table table-bordered align-middle">
        <thead style="background-color: #c2c2c2">
          <tr>
            <th rowspan="2" class="text-center">TT</th>
            <th rowspan="2" class="text-center" style="white-space: pre">
              Họ tên
            </th>
            <th colspan="31" class="text-center">
              Ngày trong tháng {{ currentMonth }}
            </th>
            <th rowspan="2" style="white-space: pre">
              Tổng số <br />ngày làm <br />việc
            </th>
            <th rowspan="2" style="white-space: pre">
              Tổng số <br />ngày hưởng <br />lương
            </th>
          </tr>
          <tr>
            <th class="text-center" v-for="(n, index) in 31" :key="index">
              {{ n }}
            </th>
          </tr>
        </thead>

        <tbody v-if="!checkNone">
          <tr v-for="(user, indexLog) in users" :key="indexLog">
            <td>{{ indexLog + 1 }}</td>
            <td style="white-space: pre">{{ user.name }}</td>
            <td
              v-b-modal="'my-modal'"
              class="fix text-center position-relative"
              :class="{
                edited: log.status,
                weekend: checkWeekend(index + 1) && !log.status,
              }"
              v-on:click="infoEdit(index, user.code, log.sign, log.reason)"
              v-for="(log, index) in user.log"
              :key="index"
            >
              {{ log.sign }}
              <el-tooltip
                popper-class="reason-popper"
                v-if="log.reason != null"
                placement="right"
                effect="light"
              >
                <div slot="content">
                  <div class="note-wrapper">{{ log.reason }}</div>
                </div>
                <svg
                  class="position-absolute top-0 end-0"
                  x="0px"
                  y="0px"
                  viewBox="0 0 512 512"
                  style="
                    enable-background: new 0 0 512 512;
                    width: 10px;
                    fill: #a843a8ff;
                  "
                  xml:space="preserve"
                >
                  <g>
                    <polygon
                      points="5.8,0.6 0,6.4 0,378.3 5.8,384 384,384 512,512 512,5.8 506.3,0 6.4,0 	"
                    />
                  </g>
                </svg>
              </el-tooltip>
            </td>
            <td class="text-center">{{ user.dayWork }}</td>
            <td
              class="fix text-center"
              @click="infoDayEdit(user.dayEarn, user.code)"
              v-b-modal="'my-modal1'"
            >
              {{ user.dayEarn }}
            </td>
          </tr>
        </tbody>

        <!--      Check không có dữ liệu hiện "NO DATA"-->

        <tbody v-if="checkNone">
          <tr>
            <td colspan="35" class="text-center">No data</td>
          </tr>
        </tbody>
      </table>
      <table
        v-if="showModeratorBoard"
        class="table table-bordered align-middle"
      >
        <thead style="background-color: #c2c2c2">
          <tr>
            <th rowspan="2">TT</th>
            <th rowspan="2" style="white-space: pre">Họ tên</th>
            <th colspan="31" class="text-center">Ngày trong tháng</th>
            <th rowspan="2" style="white-space: pre">
              Tổng số <br />ngày làm <br />việc
            </th>
            <th rowspan="2" style="white-space: pre">
              Tổng số <br />ngày hưởng <br />lương
            </th>
          </tr>
          <tr>
            <th class="text-center" v-for="(n, index) in 31" :key="index">
              {{ n }}
            </th>
          </tr>
        </thead>

        <tbody v-if="!checkNone">
          <tr v-for="(user, indexLog) in users" :key="indexLog">
            <td>{{ indexLog + 1 }}</td>
            <td style="white-space: pre">{{ user.name }}</td>
            <td
              class="fix text-center position-relative"
              :class="{ weekend: checkWeekend(index + 1) }"
              v-for="(log, index) in user.log"
              :key="index"
            >
              {{ log.sign }}
              <el-tooltip
                popper-class="reason-popper"
                v-if="log.reason != null"
                placement="right"
                effect="light"
              >
                <div slot="content">
                  <div class="note-wrapper">{{ log.reason }}</div>
                </div>
                <svg
                  class="position-absolute top-0 end-0"
                  x="0px"
                  y="0px"
                  viewBox="0 0 512 512"
                  style="
                    enable-background: new 0 0 512 512;
                    width: 10px;
                    fill: #a843a8ff;
                  "
                  xml:space="preserve"
                >
                  <g>
                    <polygon
                      points="5.8,0.6 0,6.4 0,378.3 5.8,384 384,384 512,512 512,5.8 506.3,0 6.4,0 	"
                    />
                  </g>
                </svg>
              </el-tooltip>
            </td>
            <td class="text-center">{{ user.dayWork }}</td>
            <td class="fix text-center">{{ user.dayEarn }}</td>
          </tr>
        </tbody>

        <!--      Check không có dữ liệu hiện "NO DATA"-->

        <tbody v-if="checkNone">
          <tr>
            <td colspan="35" class="text-center">No data</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!--   MODAL Chỉnh sửa chấm công-->

    <b-modal id="my-modal" centered size="sm">
      <template #modal-header="{ close }">
        <!-- Emulate built in modal header close button action -->
        <h5>Chỉnh sửa chấm công</h5>
        <b-icon-x-circle-fill style="color: #d8363a" size="sm" @click="close()">
        </b-icon-x-circle-fill>
      </template>
      <b-form-radio
        v-model="selected"
        v-for="(sign, index) in signs"
        :key="index"
        :name="sign"
        :value="sign"
        >{{ sign }}</b-form-radio
      >
      <div class="mt-2 my-2">Nhập lý do :</div>
      <b-form-input
        v-model="currenReason"
        size="sm"
        autofocus
        type="text"
      ></b-form-input>
      <template #modal-footer="{ ok }">
        <b-button
          size="sm"
          variant="success"
          @click="handleEdit(dateEdit, codeEdit, selected, currenReason), ok()"
        >
          OK
        </b-button>
      </template>
    </b-modal>

    <!--    MODAL Chỉnh sửa số ngày hưởng lương-->
    <b-modal id="my-modal1" centered size="sm">
      <template #modal-header="{ close }">
        <!-- Emulate built in modal header close button action -->
        <h5>Số ngày hưởng lương</h5>
        <b-icon-x-circle-fill style="color: #d8363a" size="sm" @click="close()">
        </b-icon-x-circle-fill>
      </template>
      <b-form-input
        v-model="dayEarn"
        size="sm"
        min="0"
        max="31"
        type="number"
        >{{ dayEarn }}</b-form-input
      >
      <template #modal-footer="{ ok }">
        <b-button
          size="sm"
          variant="success"
          @click="handleDayEarn(dayEarn, codeEdit), ok()"
        >
          OK
        </b-button>
      </template>
    </b-modal>
    <!--    MODAL Lưu chỉnh sửa -->
    <b-modal id="save-modal" centered style="width: 100px" size="md">
      <template #modal-header="{}">
        <!-- Emulate built in modal header close button action -->
        <h5>Xác nhận lưu tất cả chỉnh sửa</h5>
      </template>
      <span style="color: #75c4c0">Lưu</span><span> hoặc</span
      ><span style="color: #ff0000"> Xóa</span><span> tất cả chỉnh sửa</span>
      <template #modal-footer="{ ok }">
        <b-button variant="danger" @click="handleDeleteEdit(), ok()"
          >Xoá</b-button
        >
        <b-button @click="ok()">Tiếp tục chỉnh sửa</b-button>
        <b-button
          style="background-color: #75c4c0"
          @click="handelUpdate(), ok()"
        >
          Lưu
        </b-button>
      </template>
    </b-modal>
  </div>
</template>

<script>
// import UserService from '../services/user.service';
import ExcelService from "@/services/excel-service";
import LogService from "@/services/logdetail-service";

export default {
  name: "ReportAdmin",
  data() {
    return {
      accountDepartment: "",
      users: [],
      checkNone: false,
      search: "",
      department: null,
      departments: [],
      signs: ["H", "KL", "NT", "H_KL", "KL_H", "_"],
      selected: "",
      dateEdit: "",
      codeEdit: "",
      logsEdit: [],
      dayEarn: 0,
      currenReason: "",
      currentMonth: new Date().getMonth() + 1,
      currentYear: new Date().getFullYear(),
      currentDepartment: "",
    };
  },

  methods: {
    //event handler
    infoEdit(date, code, sign, reason) {
      this.dateEdit = date;
      this.codeEdit = code;
      this.selected = sign;
      this.currenReason = reason;
    },
    infoDayEdit(dayEarn, code) {
      this.codeEdit = code;
      this.dayEarn = dayEarn;
    },
    handleDayEarn(dayEarn, code) {
      for (let user of this.users) {
        if (user.code == code) {
          user.dayEarn = dayEarn;
        }
      }
    },
    handelSearch() {
      this.getLog();
    },
    handleEdit(date, code, sign, reason) {
      for (let user of this.users) {
        if (user.code == code) {
          if (
            user.log[date].sign == sign &&
            user.log[date].status === false &&
            user.log[date].reason == null
          ) {
            return;
          }
          if (reason == "") reason = null;
          if (user.log[date].sign == sign && user.log[date].status === true)
            user.log[date].reason = reason;

          user.log[date].sign = sign;
          user.log[date].status = true;
          user.dayWork = this.caculateDayWork(user.log);
          user.dayEarn = this.caculateDayEarn(user.log);
          user.log[date].reason = reason;

          this.logsEdit = this.logsEdit.filter((log) => {
            return (
              (log.code == code && log.date !== user.log[date].date) ||
              log.code != code
            );
          });
          if (sign == "_")
            this.logsEdit.push({
              date: user.log[date].date,
              code: code,
              sign: null,
              reason: reason,
              codeAdminEdit:this.currentUser.user.code
            });
          else
            this.logsEdit.push({
              date: user.log[date].date,
              code: code,
              sign: sign,
              reason: reason,
              codeAdminEdit:this.currentUser.user.code
            });
        }
      }
    },
    handleDeleteEdit() {
      this.getLog();
      this.$swal.fire({
        title: "Xóa chỉnh sửa thành công",
        type: "success",
        icon: "success",
        showCloseButton: true,
      });
    },
    handelUpdate() {
      LogService.updateLog(this.logsEdit)
        .then((respone) => {
          console.log(respone);
          this.$swal.fire({
            title: "Cập nhật thành công",
            type: "success",
            icon: "success",
            showCloseButton: true,
          });
          this.users.map((user) => {
            user.log.map((sign) => {
              sign.status = false;
            });
          });
          this.logsEdit = [];
        })
        .catch((error) => {
          this.$swal.fire({
            title: "Cập nhật thất bại",
            type: "error",
            text: error.response.data.message,
            icon: "error",
            showCloseButton: true,
          });
        });
    },
    //CaculateDayWork
    caculateDayEarn(signs) {
      let dayEarn = 0;
      for (let sign of signs) {
        if (sign.sign == "H") dayEarn = dayEarn + 1;
        if (sign.sign == "P_H") dayEarn = dayEarn + 1;
        if (sign.sign == "H_P") dayEarn = dayEarn + 1;
        if (sign.sign == "H_KL") dayEarn = dayEarn + 0.5;
        if (sign.sign == "KL_H") dayEarn = dayEarn + 0.5;
        if (sign.sign == "P") dayEarn = dayEarn + 1;
        if (sign.sign == "P_KL") dayEarn = dayEarn + 0.5;
        if (sign.sign == "KL_P") dayEarn = dayEarn + 0.5;
        if (sign.sign == "L") dayEarn = dayEarn + 1;
        if (sign.sign == "Ô") dayEarn = dayEarn + 1;
        if (sign.sign == "TC") dayEarn = dayEarn + 1;
        if (sign.sign == "CĐ") dayEarn = dayEarn + 1;
      }
      return dayEarn;
    },
    // Số ngày hưởng lương
    caculateDayWork(signs) {
      let dayWork = 0;
      for (let sign of signs) {
        if (sign.sign == "H") dayWork = dayWork + 1;
        if (sign.sign == "P_H") dayWork = dayWork + 0.5;
        if (sign.sign == "H_P") dayWork = dayWork + 0.5;
        if (sign.sign == "H_KL") dayWork = dayWork + 0.5;
        if (sign.sign == "KL_H") dayWork = dayWork + 0.5;
      }
      return dayWork;
    },
    checkWeekend(date) {
      let formDate = this.currentYear + "-" + this.currentMonth + "-" + date;
      const today = new Date(formDate);
      const day = today.getDay();
      if (day == 0 || day == 6) return true;
      else return false;
    },
    // Call API method

    exportExcel() {
      this.$swal
        .fire({
          title: "Xuất bảng chấm công?",
          showDenyButton: true,
          confirmButtonColor: "#75C4C0",
          confirmButtonText: "Xuất",
          denyButtonColor: "#ED9696",
          denyButtonText: "Hủy",
          customClass: {
            actions: "my-actions",
            cancelButton: "order-1 right-gap",
            confirmButton: "order-2",
            denyButton: "order-3",
          },
        })
        .then((result) => {
          if (result.isConfirmed) {
            let params = null;
            if (this.showModeratorBoard) {
              params = {
                id: this.accountDepartment.id,
                month: this.currentMonth,
              };
            } else {
              params = {
                id: this.department,
                month: this.currentMonth,
              };
            }
            ExcelService.exportExcelReport(params);
          } else if (result.isDenied) {
            this.$swal.fire({
              title: "Hủy xuất file",
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
    },
    getDepartment() {
      LogService.getDepartment().then((respone) => {
        this.departments = respone.data;
      });
    },
    getLog() {
      if (this.showModeratorBoard) {
        this.department = this.currentUser.user.departments.id;
      }
      LogService.getAllByMonthAndDepartment({
        month: this.currentMonth,
        id: this.department,
        search: this.search,
      }).then((respone) => {
        let dataCall = [];
        let data = respone.data;
        for (let users of data) {
          let signs = [];
          for (let i = 1; i <= 31; i++) {
            //get All LogDetail
            let dates = [];
            for (let user of users.logDetail) {
              let date = Number(user.dateLog.split("-")[2]);
              dates.push(date);

              if (date == i) {
                if (user.signs == null)
                  signs.push({
                    sign: "_",
                    status: false,
                    date: user.dateLog,
                    reason: user.reason,
                  });
                else
                  signs.push({
                    sign: user.signs.name,
                    status: false,
                    date: user.dateLog,
                    reason: user.reason,
                  });
              }
            }
            if (!dates.includes(i)) {
              if (i < 10) i = "0" + i;
              let formDate =
                this.currentYear + "-" + this.currentMonth + "-" + i;
              signs.push({
                sign: "_",
                status: false,
                date: formDate,
                reason: null,
              });
            }
          }
          dataCall.push({
            code: users.code,
            log: signs,
            name: users.name,
            dayWork: this.caculateDayWork(signs),
            dayEarn: this.caculateDayEarn(signs),
          });
        }
        if (data.length > 0) {
          this.users = dataCall;
          this.checkNone = false;
        }
        if (data.length == 0) {
          this.users = [];
          this.checkNone = true;
        }
      });
    },
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showAdminBoard() {
      if (this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_ADMIN");
      }
      return false;
    },
    showModeratorBoard() {
      if (this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_MANAGE");
      }
      return false;
    },
  },
  mounted() {
    this.getDepartment();
    this.getLog();
    this.accountDepartment = this.currentUser.user.departments;
  },
};
</script>
<style>
* {
  font-size: 16px;
}
.boder-round {
  border-radius: 6px;
}
.fix:hover {
  background-color: #e24146;
  color: white;
}
.fix {
  padding: 10px !important;
}
.edited {
  background-color: #e24146 !important;
  color: white;
}
.weekend {
  background-color: #f8cbad !important;
}
.reason-popper {
  border: #a843a8 1px;
}
</style>
