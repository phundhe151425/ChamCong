<template>
  <div class="container register">
    <div class="row">
      <div class="col-md-3 register-left">
        <img src="https://image.ibb.co/n7oTvU/logo_white.png" alt="" />
        <img
          style="width: 200px; line-height: 100%"
          src="../../assets/vmg_logo.png"
        />
      </div>
      <div class="col-md-9 register-right">
        <ul
          class="nav nav-tabs nav-justified"
          id="myTab"
          role="tablist"
          style="background-color: #e24146"
        >
          <li class="nav-item">
            <a
              class="nav-link active"
              id="home-tab"
              data-toggle="tab"
              href="#home"
              role="tab"
              aria-controls="home"
              aria-selected="true"
              >Tạo</a
            >
          </li>
          <li class="nav-item">
            <a
              class="nav-link"
              id="profile-tab"
              data-toggle="tab"
              href="#profile"
              role="tab"
              aria-controls="profile"
              aria-selected="false"
            >
              Nhân viên
            </a>
          </li>
        </ul>
        <div class="tab-content" id="myTabContent">
          <div
            class="tab-pane fade show active"
            id="home"
            role="tabpanel"
            aria-labelledby="home-tab"
          >
            <form id="formRegister" @submit.prevent="sendForm">
              <div class="row register-form">
                <div class="col-md-4">
                  <input
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
                          <input
                            v-model="user.fullName"
                            type="text"
                            class="form-control"
                            name="fullName"
                            placeholder="Họ và tên"
                            value=""
                            v-validate="{ required: true, min: 6 }"
                          />
                          <div
                            v-if="errors.has('fullName')"
                            class="alert alert-danger"
                            role="alert"
                          >
                            Nhập họ và tên
                          </div>
                        </div>
                      </td>
                    </tr>

                    <tr style="height: 70px">
                      <td style="width: 100px">
                        Email<span style="color: red">*</span>
                      </td>
                      <td style="width: 300px">
                        <div class="form-group">
                          <input
                            autocomplete="off"
                            v-model="user.username"
                            type="email"
                            class="form-control"
                            placeholder="Email *"
                            value=""
                            v-validate="{ required: true, min: 5 }"
                            name="username"
                          />
                        </div>
                        <div
                          v-if="errors.has('username')"
                          class="alert alert-danger"
                          role="alert"
                        >
                          Nhập email
                        </div>
                      </td>
                    </tr>

                    <tr style="height: 70px">
                      <td style="width: 100px">
                        Mã nhân viên<span style="color: red">*</span>
                      </td>
                      <td style="width: 300px">
                        <div class="form-group">
                          <input
                            v-model="user.code"
                            class="form-control"
                            placeholder="Employee ID *"
                            name="code"
                            autocomplete="off"
                          />
                          <small v-if="errId !== null" style="color: red">
                            {{errId}}
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
                          >&nbsp; Nam</el-radio
                        >
                        <el-radio
                          v-model="user.gender"
                          name="gender"
                          value="Nữ"
                          label="Nữ"
                          border
                          >&nbsp; Nữ</el-radio
                        >
                      </td>
                    </tr>

                    <tr style="height: 70px">
                      <td style="width: 100px">
                        Phòng ban<span style="color: red">*</span>
                      </td>
                      <td style="width: 300px">
                        <div class="form-group">
                          <select
                            name="department"
                            v-validate="{ required: true }"
                            class="form-control"
                            v-model="user.department"
                          >
                            <option>Thực tập PTPM</option>
                            <option>Phòng kế toán</option>
                            <option>Phòng phát triển phần mềm</option>
                            <option>Phòng nhân sự</option>
                          </select>
                        </div>
                        <div
                          v-if="errors.has('department')"
                          class="alert alert-danger"
                          role="alert"
                        >
                          Chọn phòng ban
                        </div>
                      </td>
                    </tr>

                    <tr style="height: 40px">
                      <td style="width: 100px">
                        Chức vụ<span style="color: red">*</span>
                      </td>
                      <td style="width: 300px">
                        <input
                          type="radio"
                          id="admin"
                          value="admin"
                          v-model="user.role"
                          name="role"
                        />
                        <label for="admin">&nbsp; Admin</label>
                      </td>
                    </tr>
                    <tr style="height: 40px">
                      <td style="width: 100px"></td>
                      <td style="width: 300px">
                        <input
                          type="radio"
                          id="manage"
                          value="manage"
                          v-model="user.role"
                          name="role"
                        />
                        <label for="manage">&nbsp; Manage</label>
                      </td>
                    </tr>
                    <tr style="height: 40px">
                      <td style="width: 100px"></td>
                      <td style="width: 300px">
                        <input
                          type="radio"
                          id="user"
                          value="user"
                          v-model="user.role"
                          name="role"
                        />
                        <label for="user">&nbsp; User</label>
                      </td>
                    </tr>

                    <tr style="height: 60px">
                      <td style="width: 100px"></td>
                      <td style="width: 300px">
                        <div class="form-group">
                          <button class="btn btn-block btn-signup">
                            Sign Up
                          </button>
                        </div>
                      </td>
                    </tr>
                  </table>
                  <br />
                </div>
              </div>
            </form>
            <div
              v-if="message"
              class="alert"
              :class="successful ? 'alert-success' : 'alert-danger'"
            >
              {{ message }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import authService from "@/services/auth.service";
import userService from '../../services/user.service';

export default {
  name: "AdminVue",
  data() {
    return {
      content: "",
      value: new Date(),
      user: {
        username: "",
        fullName: "",
        role: [],
        department: "",
        code: "",
        gender: "",
      },
      submitted: false,
      successful: false,
      message: "",
      newImage: "",
      cover: {},
      users: [],
      errId: "",
      checkId: true,
    };
  },

  mounted() {
    userService.getAdminBoard().then(
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
  methods: {
   async sendForm() {
   console.log(this.user.code)
    let response = await userService.getAllUser()
     this.users = response.data;
      for (let i = 0; i < this.users.length; i++) {
           console.log(this.users[i].code)
        if (this.user.code == this.users[i].code) {
          this.errId = 'Mã nhân sự đã tồn tại';
          this.checkId = false;
          break;
        } else {
          this.checkId = true;
        }
      }
      
      if (!this.user.code) {
        this.errId = 'Hãy nhập mã nhân sự'
        this.checkId = false;
      }


      if (this.checkId === true){
      this.submitted = true;
      let form = document.querySelector("#formRegister");
      if (
        this.user.code == "" ||
        this.user.username == "" ||
        this.user.fullName == "" ||
        this.user.department == "" ||
        this.user.gender == ""
      ) {
        this.$swal.fire({
          title: "Chưa nhập đủ thông tin!",
          icon: "info",
          timer: 2000,
          timerProgressBar: true,
        });
      } else {
        let response = authService.register(form);
        if (response) {
          this.$swal.fire({
            title: "Tạo tài khoản thành công!",
            icon: "success",
            timer: 2000,
            timerProgressBar: true,
          });
          return this.$router.push("/add-user");
        } else {
          this.$swal.fire({
            title: "Tạo tài khoản thất bại!",
            icon: "success",
            timer: 2000,
            timerProgressBar: true,
          });
          return this.$router.push("/add-user");
        }

      }
      }
    },
    previewFiles(event) {
      const file = event.target.files[0];

      const theReader = new FileReader();
      theReader.onloadend = async () => {
        this.newImage = await theReader.result;
      };
      theReader.readAsDataURL(file);
    },
  },
};
</script>
<style scoped>
#my-strictly-unique-vue-upload-multiple-image {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /*text-align: center;*/
  color: #2c3e50;
}

h1,
h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}

.register {
  background: -webkit-linear-gradient(left, rgba(52, 58, 64, 255), #ffffff);
  margin-top: 3%;
  padding: 3%;
}

.register-left {
  text-align: center;
  color: #fff;
  margin-top: 4%;
}

.register-left input {
  border: none;
  border-radius: 1.5rem;
  padding: 2%;
  width: 60%;
  background: #f8f9fa;
  font-weight: bold;
  color: #383d41;
  margin-top: 30%;
  margin-bottom: 3%;
  cursor: pointer;
}

.register-right {
  background: #f8f9fa;
  border-top-left-radius: 10% 50%;
  border-bottom-left-radius: 10% 50%;
}

.register-left img {
  margin-top: 15%;
  margin-bottom: 5%;
  width: 25%;
  -webkit-animation: mover 2s infinite alternate;
  animation: mover 1s infinite alternate;
}

@-webkit-keyframes mover {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-20px);
  }
}

@keyframes mover {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-20px);
  }
}

.register-left p {
  font-weight: lighter;
  padding: 12%;
  margin-top: -9%;
}

.register .register-form {
  padding: 20px;
  margin-top: 10%;
}

.btnRegister {
  float: right;
  margin-top: 10%;
  border: none;
  border-radius: 1.5rem;
  padding: 2%;
  background: red;
  color: #fff;
  font-weight: 600;
  width: 50%;
  cursor: pointer;
}

.register .nav-tabs {
  margin-top: 3%;
  border: none;
  background: red;
  border-radius: 1.5rem;
  width: 28%;
  float: right;
}

.register .nav-tabs .nav-link {
  padding: 2%;
  height: 34px;
  font-weight: 600;
  color: #fff;
  border-top-right-radius: 1.5rem;
  border-bottom-right-radius: 1.5rem;
}

.register .nav-tabs .nav-link:hover {
  border: none;
}

.register .nav-tabs .nav-link.active {
  width: 100px;
  color: red;
  border: 2px solid red;
  border-top-left-radius: 1.5rem;
  border-bottom-left-radius: 1.5rem;
}

.register-heading {
  text-align: center;
  margin-top: 8%;
  margin-bottom: -15%;
  color: #495057;
}

.tab-content {
  padding: 0;
}

.btn-signup {
  background-color: #e24146;
  color: #ffffff;
}

.btn-signup:hover {
  background-color: #a31c20;
}
</style>
