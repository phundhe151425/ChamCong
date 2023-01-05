<template style="font-size: 16px">
  <div>
    <div class="loading" id="loading">
      <img
        src="https://img.pikbest.com/png-images/20190918/cartoon-snail-loading-loading-gif-animation_2734139.png!bw700"
        alt="loading"
      />
    </div>
    <div style="padding-bottom: 20px">
      <div className="" style="width: 100%; margin: auto">
        <el-row :gutter="20">
          <el-col :md="6" :lg="6" :xl="6">
            <div class="grid-content" style="margin-bottom: 20px">
              <span>Phòng ban</span> &ensp;
              <el-select
                v-model="departmentId"
                @change="getAll"
                placeholder="Chọn Phòng ban"
              >
                <el-option value="0" label="Tất cả các phòng ban"></el-option>
                <el-option
                  v-for="item in departments"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </div>
          </el-col>

          <el-col :md="6" :lg="6" :xl="6" style="margin-bottom: 20px">
            <div class="grid-content">
              <span style="">Tìm kiếm</span> &ensp;
              <el-input
                v-model="search"
                @input="getAll"
                size="medium"
                placeholder="Tìm theo tên, email"
                style="width: 200px; padding: 2px 0"
              />
            </div>
          </el-col>
          <el-col :md="6" :lg="6" :xl="6" style="margin-bottom: 20px">
            <div class="grid-content">
              <span style="">Trạng thái</span> &ensp;
              <el-select
                v-model="status"
                @change="getAll"
                placeholder="Trạng thái"
              >
                <el-option value="" label="Tất cả"></el-option>
                <el-option label="Có hiệu lực" value="1"></el-option>
                <el-option label="Vô hiệu lực" value="0"></el-option>
              </el-select>
            </div>
          </el-col>

          <el-col :md="7" :lg="7" :xl="7" class="div-buttons">
            <div class="grid-content div-buttons">
              <import-excel
                class="text-start buttons btn-import"
                header="Thêm nhân viên"
                format="1"
                @getData="getAll"
                @downloadExample="downloadExample"
                style="margin-right: 10px"
              />
              <el-button
                class="buttons btn-add"
                type="danger"
                style=""
                round
                @click="dialogFormVisible = true"
                ><i class="el-icon-plus"></i> Thêm nhân viên
              </el-button>
              <el-button
                  class="buttons btn-add"
                  type="danger"
                  style=""
                  round
                  @click="exportUsers"
              ><i class="el-icon-plus"></i> Xuất file
              </el-button>
            </div>
          </el-col>
        </el-row>

        <el-dialog title="Thêm nhân viên" :visible.sync="dialogFormVisible">
          <form id="formRegister">
            <div class="row register-form">
              <div class="col-md-4">
                <input
                  id="fileUser"
                  type="file"
                  name="cover"
                  class="form-control"
                  placeholder="Title"
                  @change="previewFiles($event)"
                />
                <br /><br />
                <img
                  alt=""
                  :src="
                    newImage ||
                    'https://www.namepros.com/attachments/empty-png.89209/'
                  "
                  style="width: 270px"
                />
              </div>
              <div class="col-md-8">
                <table class="text-start">
                  <tr style="height: 70px">
                    <td style="width: 100px">
                      Họ và tên<span style="color: red">*</span>
                    </td>
                    <td style="width: 300px">
                      <div class="form-group">
                        <el-input
                          v-model="user.fullName"
                          type="text"
                          name="fullName"
                          placeholder="Họ và tên"
                          value=""
                          autocomplete="off"
                        >
                        </el-input>
                        <small v-if="errName !== null" style="color: red">
                          {{ errName }}
                        </small>
                      </div>
                    </td>
                  </tr>
                  <tr style="height: 70px">
                    <td style="width: 100px">
                      Email<span style="color: red">*</span>
                    </td>
                    <td style="width: 300px">
                      <div class="form-group">
                        <el-input
                          v-model="user.username"
                          type="email"
                          placeholder="Email"
                          value=""
                          name="username"
                          autocomplete="off"
                        >
                        </el-input>
                      </div>
                      <small v-if="errEmail !== null" style="color: red">
                        {{ errEmail }}
                      </small>
                    </td>
                  </tr>
                  <tr style="height: 70px">
                    <td style="width: 100px">
                      Mã nhân viên<span style="color: red">*</span>
                    </td>
                    <td style="width: 300px">
                      <div class="form-group">
                        <!-- VMG_
                          <input
                            style="width: 300px"
                            v-model="user.code"
                            placeholder="Mã nhân viên"
                            name="code"

                            autocomplete="off"
                          /> -->
                        <el-input
                          style="width: 300px"
                          placeholder="Mã nhân viên"
                          v-model="user.code"
                          name="code"
                          autocomplete="off"
                        >
                          <template slot="prepend">VMG_</template>
                        </el-input>
                        <small v-if="errId !== null" style="color: red">
                          {{ errId }}
                        </small>
                      </div>
                    </td>
                  </tr>

                  <tr style="height: 70px">
                    <td style="width: 150px">
                      Giới tính<span style="color: red">*</span>
                    </td>
                    <td style="width: 300px">
                      <el-radio
                        v-model="user.gender"
                        name="gender"
                        value="Nam"
                        label="Nam"
                        border
                        >&nbsp; Nam
                      </el-radio>
                      <el-radio
                        v-model="user.gender"
                        name="gender"
                        value="Nữ"
                        label="Nữ"
                        border
                        >&nbsp; Nữ
                      </el-radio>
                      <br />
                      <small v-if="errGender !== null" style="color: red">
                        {{ errGender }}
                      </small>
                    </td>
                  </tr>

                  <tr style="height: 70px">
                    <td style="width: 100px">
                      Phòng ban<span style="color: red">*</span>
                    </td>
                    <td style="width: 300px">
                      <div class="form-group">
                        <el-select
                          name="department"
                          v-model="user.department"
                          @change="getAll"
                          placeholder="Chọn phòng ban"
                        >
                          <el-option
                            v-for="item in departments"
                            :key="item.id"
                            :label="item.name"
                            :value="item.name"
                          >
                          </el-option>
                        </el-select>
                      </div>
                      <small v-if="errDepartment !== null" style="color: red">
                        {{ errDepartment }}
                      </small>
                    </td>
                  </tr>

                  <tr style="height: 70px">
                    <td style="width: 100px">
                      Ngày vào làm<span style="color: red">*</span>
                    </td>
                    <td style="width: 300px">
                      <div class="form-group">
                        <el-date-picker
                          v-model="user.startWork"
                          placeholder="Chọn ngày"
                          value=""
                          name="startWork"
                          autocomplete="off"
                          :editable="false"
                        >
                        </el-date-picker>
                      </div>
                      <small v-if="errStartWork !== null" style="color: red">
                        {{ errStartWork }}
                      </small>
                    </td>
                  </tr>

                  <tr style="height: 40px">
                    <td style="width: 100px">
                      Vị trí<span style="color: red">*</span>
                    </td>
                    <div class="form-group">
                      <b-form-select
                        style="width: 100%; padding: 9px 0"
                        name="position"
                        v-model="user.position"
                        @change="getAll"
                        required
                      >
                        <template #first>
                          <b-form-select-option :value="null" disabled
                            >Chọn vị trí
                          </b-form-select-option>
                        </template>
                        <b-form-select-option
                          v-for="(item, index) in positions"
                          :key="index"
                          :value="item.id"
                          >{{ item.name }}
                        </b-form-select-option>
                      </b-form-select>
                    </div>
                  </tr>

                  <tr style="height: 40px">
                    <td style="width: 100px"></td>
                    <td style="width: 300px">
                      <small v-if="errRole !== null" style="color: red">
                        {{ errRole }}
                      </small>
                    </td>
                  </tr>
                </table>
                <br />
              </div>
              <small style="color: green">
                {{ message }}
              </small>
            </div>
          </form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="removeValidate(false)">Hủy</el-button>
            <el-button type="primary" @click="sendForm"
              >Thêm nhân viên</el-button
            >
          </span>
        </el-dialog>

        <br />
        <div>
          <el-table
            :data="users"
            height="780px"
            :header-cell-style="{
              background: '#D9D9D9',
              color: 'black',
              align: 'center',
            }"
            style="
              width: 100%;
              display: inline-block;
              font-size: 16px;
              border-radius: 10px;
              box-shadow: rgb(149 157 165 / 20%) 0px 8px 24px;
            "
            :row-class-name="tableRowClassName"
          >
            >
            <el-table-column
              label="STT"
              type="index"
              align="center"
              width="100px"
            ></el-table-column>
            <el-table-column
              label="Mã NV"
              v-slot:="data"
              align="center"
              width="120px"
              >{{ data.row.code }}</el-table-column
            >
            <el-table-column
              label="Ho và tên"
              prop="fullName"
              align="center"
            ></el-table-column>
            <el-table-column
              label="Phòng ban"
              prop="departments.name"
              align="center"
            >
            </el-table-column>
            <el-table-column
                label="Ngày vào làm"
                prop="startWork"
                align="center"
            >
            </el-table-column>
            <el-table-column
              label="Email"
              prop="username"
              align="center"
            ></el-table-column>
            <el-table-column
              label="Ảnh"
              v-slot:="data"
              align="center"
              width="210px"
            >
              <el-image
                style="width: 100px; height: 100px"
                v-if="data.row.cover != null"
                v-bind:src="`http://localhost:8080/` + data.row.cover"
                :fit="fit"
              ></el-image>
              <el-image
                style="width: 100px; height: 100px"
                v-if="data.row.cover == null"
                src="../assets/user.jpg"
                :fit="fit"
              ></el-image>
            </el-table-column>
            <el-table-column
              v-slot:="data"
              label="Chức vụ"
              width="150px"
              align="center"
            >
              <p
                class="text-muted mb-0"
                v-for="(role, index) in data.row.position"
                :key="index"
              >
                <span v-if="role == 1">Nhân sự</span>
                <span v-if="role == 2">Giám đốc trung tâm</span>
                <span v-if="role == 3">Trưởng phòng</span>
                <span v-if="role == 4">Trưởng nhóm</span>
                <span v-if="role == 5">Chuyên viên</span>
                <span v-if="role == 6">Nhân viên</span>
                <span v-if="role == 7">Cộng tác viên</span>
                <span v-if="role == 8">Thực tập </span>
              </p>
            </el-table-column>
            <el-table-column
              v-slot:="data"
              label="Trạng thái"
              width="150px"
              align="center"
            >
              <button v-if="data.row.avalible == true" class="tt1">
                Có hiệu lực
              </button>
              <button v-if="data.row.avalible != true" class="tt2">
                Vô hiệu lực
              </button>
            </el-table-column>
            <el-table-column
              v-slot:="data"
              label="Thao tác"
              width="150px"
              align="center"
            >

              <router-link :to="`/user/${data.row.id}`">
                <!--            <el-button type="danger" icon="el-icon-edit-outline" circle></el-button>-->
                <button style="margin-right: 10px" class="btn-action">
                  <i class="el-icon-edit-outline" style="width: 30px"></i>
                </button>
              </router-link>

              <button
                v-if="
                  data.row.avalible == 1 && data.row.id == currentUser.user.id
                "
                class="btn-action"
                @click="
                  changeStatus(
                    data.row.id,
                    data.row.fullName,
                    data.row.avalible
                  )
                "
                disabled
              >
                <i class="el-icon-unlock" style="width: 30px"></i>
              </button>
              <button
                v-if="
                  data.row.avalible == 0 && data.row.id == currentUser.user.id
                "
                class="btn-action"
                @click="
                  changeStatus(
                    data.row.id,
                    data.row.fullName,
                    data.row.avalible
                  )
                "
                disabled
              >
                <i class="el-icon-lock" style="width: 30px"></i>
              </button>
              <!--          </div>-->
              <!--          <div v-if="data.row.id != currentUser.user.id">-->
              <button
                v-if="
                  data.row.avalible == 1 && data.row.id != currentUser.user.id
                "
                class="btn-action"
                @click="
                  changeStatus(
                    data.row.id,
                    data.row.fullName,
                    data.row.avalible
                  )
                "
              >
                <i class="el-icon-unlock" style="width: 30px"></i>
              </button>
              <button
                v-if="
                  data.row.avalible == 0 && data.row.id != currentUser.user.id
                "
                class="btn-action"
                @click="
                  changeStatus(
                    data.row.id,
                    data.row.fullName,
                    data.row.avalible
                  )
                "
              >
                <i class="el-icon-lock" style="width: 30px"></i>
              </button>
              <!--          </div>-->
            </el-table-column>
          </el-table>
        </div>
        <el-pagination
          class="text-end"
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
import DepartmentService from "@/services/department.service";
import UserService from "@/services/user.service";
import AuthService from "@/services/auth.service";
import ImportExcel from "@/views/excel/ImportExcel";
import ExcelService from "@/services/excel-service";
import PositionService from "@/services/position.service";


export default {
  name: "HomeVue",
  components: { ImportExcel },
  data() {
    return {
      user_code: "",
      date: "",
      users: [],
      search: "",
      totalItems: 0,
      page: 0,
      pageSize: 10,
      fit: "fill",
      departments: [],
      departmentId: "",
      status: "",
      dialogFormVisible: false,
      roleData: "",
      user: {
        username: "",
        fullName: "",
        position: null,
        department: "",
        code: "",
        gender: "",
        startWork: "",
      },
      submitted: false,
      successful: false,
      message: "",
      newImage: "",
      cover: null,
      formLabelWidth: "120px",
      errStartWork: "",
      checkStartWork: true,
      errId: "",
      checkId: true,
      errEmail: "",
      checkEmail: true,
      errName: "",
      checkName: true,
      errDepartment: "",
      checkDepartment: true,
      errGender: "",
      checkGender: true,
      errRole: "",
      checkRole: true,
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
    UserService.getAdminBoard().then(
      (response) => {
        this.content = response.data;
      },
      (error) => {
        this.content =
          (error.response && error.response.data) ||
          error.message ||
          error.toString();
      }
    );
  },

  created() {
    this.getAll();
    this.getUserCode();
    DepartmentService.getAllDepartment()
      .then((response) => {
        this.departments = response.data;
      })
      .catch((e) => {
        console.log(e);
      });
    PositionService.getAllPosition()
      .then((response) => {
        this.positions = response.data;
      })
      .catch((e) => {
        console.log(e);
      });
  },

  methods: {
    validEmail: function (email) {
      var re =
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },

    validCode: function (code) {
      var re = /^(\\-)?[0-9]{4}?$/;
      return re.test(code);
    },

    validName: function (name) {
      var re =
        /^[\sa-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_]+$/;
      return re.test(name);
    },

    showLoading: function () {
      const iconLoading = document.getElementById("loading");
      iconLoading.style.display = "flex";
    },

    hideLoading: function () {
      const iconLoading = document.getElementById("loading");
      iconLoading.style.display = "none";
    },

    removeValidate(check) {
      (this.dialogFormVisible = check),
        (this.errId = ""),
        (this.checkId = false),
        (this.errEmail = ""),
        (this.checkEmail = false),
        (this.errName = ""),
        (this.checkName = false),
        (this.errDepartment = ""),
        (this.checkDepartment = false),
        (this.errGender = ""),
        (this.checkGender = false),
        (this.errRole = ""),
        (this.checkRole = false),
        (this.errStartWork = "");
      this.checkStartWork = false;
      this.user.gender = "";
      this.user.fullName = "";
      this.user.position = null;
      this.user.code = "";
      this.user.department = "";
      this.user.username = "";
      this.user.startWork = "";
      this.cover = "";
    },
    downloadExample(){
      const link = document.createElement('a')
      link.href = 'http://localhost:8080/ThongTinNhanVien_FileMau.xlsx'
      link.setAttribute('download', 'ThongTinNhanVien.xlsx')
      document.body.appendChild(link)
      link.click()
    },
    async sendForm() {
      // this.user.role = [];
      // this.user.role.push(this.roleData);
      let response = await UserService.getAllUser();
      this.users = response.data;

      if (!this.user.fullName) {
        this.errName = "Vui lòng nhập ho và tên";
        this.checkName = false;
      } else if (!this.validName(this.user.fullName)) {
        this.errName = "Vui lòng nhập đúng định dạng ho và tên";
        this.checkName = false;
      } else {
        this.errName = "";
        this.checkName = true;
      }

      for (let i = 0; i < this.users.length; i++) {
        if (this.user.code == this.users[i].code) {
          this.errId = "Mã nhân sự đã tồn tại";
          this.checkId = false;
          this.message = "";
          break;
        } else {
          this.errId = "";
          this.checkId = true;
        }
      }

      if (!this.user.code) {
        this.errId = "Vui lòng nhập mã nhân viên";
        this.checkId = false;
      } else if (!this.validCode(this.user.code)) {
        this.errId = "Mã nhân viên chỉ bao gồm 4 số";
        this.checkId = false;
      } else if (
        this.validCode(this.user.code) &&
        this.user.code &&
        this.checkId === true
      ) {
        this.errId = "";
        this.checkId = true;
      }

      for (let i = 0; i < this.users.length; i++) {
        if (this.user.username === this.users[i].username) {
          this.errEmail = "Email đã tồn tại";
          this.checkEmail = false;
          this.message = "";
          break;
        } else {
          this.errEmail = "";
          this.checkEmail = true;
        }
      }

      if (!this.user.username) {
        this.errEmail = "Vui lòng nhập email nhân viên";
        this.checkEmail = false;
      } else if (!this.validEmail(this.user.username)) {
        this.errEmail = "Vui lòng nhập đúng định dạng email";
        this.checkEmail = false;
      } else if (
        this.validEmail(this.user.username) &&
        this.user.username &&
        this.checkEmail === true
      ) {
        this.errEmail = "";
        this.checkEmail = true;
      }

      if (!this.user.department) {
        this.errDepartment = "Hãy chon phòng ban";
        this.checkDepartment = false;
      } else {
        this.errDepartment = "";
        this.checkDepartment = true;
      }

      console.log(20, this.user.gender);

      if (this.user.gender === "") {
        this.errGender = "Hãy chọn giới tính";
        this.checkGender = false;
      } else {
        console.log(21);
        this.errGender = "";
        this.checkGender = true;
      }

      if (!this.user.position) {
        this.errRole = "Hãy chọn vị trí";
        this.checkRole = false;
      } else {
        this.errRole = "";
        this.checkRole = true;
      }

      if (!this.user.startWork) {
        this.errStartWork = "Hãy chọn ngày bắt đầu làm việc";
        this.checkStartWork = false;
      } else {
        this.errStartWork = "";
        this.checkStartWork = true;
      }

      console.log(11, this.checkId);
      console.log(12, this.checkEmail);
      console.log(13, this.checkGender);
      console.log(14, this.checkDepartment);
      if (
        this.checkId === true &&
        this.checkEmail === true &&
        this.checkName === true &&
        this.checkGender === true &&
        this.checkDepartment === true &&
        this.checkRole === true &&
        this.checkStartWork === true
      ) {
        this.showLoading();
        this.dialogFormVisible = false;
        setTimeout(() => {
          this.submitted = true;
          let form = document.querySelector("#formRegister");
          console.log(14, form.cover.value);
          AuthService.register(form).then(() => {
            this.$notify.success({
              message: "Tạo tài khoản thành công",
              title: "Success",
              timer: 2000,
              timerProgressBar: true,
            });
            this.hideLoading();
            this.getAll();
          });
        }, 2000).catch(() => {
          this.message = "";
        });
      }
    },
    getUserCode() {
      this.user_code = this.currentUser.user.user_code;
      console.log("user code" + this.user_code);
    },
    exportUsers(){
      const params = {
        "id": this.departmentId
      }
      ExcelService.exportExcelUser(params)
    },
    getAll() {
      let params = null;
      params = {
        page: this.page,
        size: this.pageSize,
        departid: this.departmentId,
        search: this.search,
        status: this.status,
      };
      UserService.getUser_Department(params)
        .then((response) => {
          this.users = response.data.content;
          this.page = response.data.pageable.pageNumber;
          console.log(response.data.pageable.pageNumber);
          this.totalItems = response.data.totalElements;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    previewFiles(event) {
      const file = event.target.files[0];

      const theReader = new FileReader();
      theReader.onloadend = async () => {
        this.newImage = await theReader.result;
      };
      theReader.readAsDataURL(file);
    },
    changeStatus(id, name, status) {
      if (status == 1) {
        this.$swal
          .fire({
            title: "Khóa tài khoản " + name + "?",
            showDenyButton: true,
            confirmButtonColor: "#ED9696",
            confirmButtonText: "Khóa",
            denyButtonColor: "#75C4C0",
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
              AuthService.lockAccount(id).then((response) => {
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
                this.getAll();
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
      } else {
        this.$swal
          .fire({
            title: "Mở tài khoản " + name + "?",
            showDenyButton: true,
            confirmButtonColor: "#75C4C0",
            confirmButtonText: "Mở",
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
              AuthService.lockAccount(id).then((response) => {
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
                this.getAll();
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
    handlePageChange(value) {
      this.page = value - 1;
      this.getAll();
    },
    tableRowClassName({ rowIndex }) {
      if (rowIndex % 2 === 1) {
        return "warning-row";
      } else if (rowIndex % 2 === 0) {
        return "success-row";
      }
      return "success-row";
    },
  },
  watch: {
    dialogFormVisible: function () {
      console.log(1212);
      this.user.role = "";
      console.log(document.getElementById("fileUser").value);
      if (!this.dialogFormVisible) {
        document.getElementById("fileUser").removeAttribute("value");
      }
      console.log(this.cover);
    },
  },
};
</script>
<style>
* {
  font-size: 16px;
}

.el-table .warning-row {
  background: #ededed;
}

.el-table .success-row {
  background: #f5f5f5;
}

.el-table .tt1 {
  cursor: default;
  color: black;
  background-color: #75c4c0;
  border: none;
  border-radius: 5px;
  padding: 3px 20px;
}

.el-table .tt2 {
  cursor: default;
  color: black;
  background-color: #ed9696;
  border: none;
  border-radius: 5px;
  padding: 3px 20px;
}

.el-table .btn-action {
  border: none;
  padding: 5px 5px;
  background-color: #f8cbad;
  border-radius: 5px;
}

.el-table .btn-action:hover {
  border: none;
  padding: 5px 5px;
  background-color: #f4e4d4;
  border-radius: 5px;
}

.el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: #c9f5eb !important;
}

/* The switch - the box around the slider */
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

/* Hide default HTML checkbox */
.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

/* The slider */
.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: 0.4s;
  transition: 0.4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: 0.4s;
  transition: 0.4s;
}

input:checked + .slider {
  background-color: #2196f3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196f3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

.loading {
  position: absolute;
  z-index: 1;
  width: 100%;
  height: 100%;
  display: none;
  align-items: center;
  justify-content: center;
  /* background: rgba(0, 0, 0, 0.479); */
}

.loading img {
  width: 25rem;
}

@media only screen and (min-width: 150px) {
  .el-col-md-6 {
    width: 108%;
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

@media only screen and (min-width: 1440px) {
  .el-col-md-6 {
    width: 23%;
  }

  .buttons {
    text-align: right;
  }

  .div-buttons {
    float: right;
  }
}

@media only screen and (min-width: 1689px) {
  .el-col-md-6 {
    width: 23%;
  }

  .buttons {
    text-align: right;
  }

  .div-buttons {
    float: right;
  }
}

@media only screen and (min-width: 1920px) {
  .el-col-md-6 {
    width: 23%;
  }

  .buttons {
    text-align: right;
  }

  .div-buttons {
    float: right;
  }
}
</style>
