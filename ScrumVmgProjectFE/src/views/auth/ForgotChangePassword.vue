<template>
  <div style="padding-bottom: 430px">
    <div v-if="check" class="container register" style="border-radius: 10px">

      <div class="row" >
        <div class="col-md-3 register-left">
          <img style="width: 200px; line-height: 100%" src="../../assets/new_logo.png"/>
        </div>
        <div class="col-md-9 register-right">


          <div class="tab-content" id="myTabContent">
            <div
                class="tab-pane fade show active"
                id="home"
                role="tabpanel"
                aria-labelledby="home-tab">
              <el-form :model="form" :rules="rules" ref="user" label-width="120px" id="register-form"
                       class="text-start">
                <div class="row register-form">
                  <div class="col-md-8">
                    <table class="text-start" style="margin-left: 150px;">
                      <tr style="height: 70px">
                        <td style="width: 300px">
                          <el-form-item label="Mật khẩu mới" prop="new_password" label-width="200px">
                            <el-input type="password" v-model="form.new_password" autocomplete="off" show-password
                                      style="width: 200px"></el-input>
                          </el-form-item>
                        </td>
                      </tr>
                      <tr style="height: 70px">
                        <td style="width: 300px">
                          <el-form-item label="Nhập lại mật khẩu mới" prop="new_password_confirm" label-width="200px">
                            <el-input type="password" v-model="form.new_password_confirm" autocomplete="off"
                                      show-password></el-input>
                          </el-form-item>
                        </td>
                      </tr>


                      <tr style="height: 60px">
                        <td style="width: 200px">

                          <el-form-item style="margin-left: 80px">
                            <span style="color: orangered">{{ message }}</span>
                            <el-button type="" @click="submit('user')" class="btn btn-signup" col>Thay đổi mật khẩu
                            </el-button>
                          </el-form-item>
                        </td>
                        <td style="width: 0px" class="text-start">

                        </td>
                      </tr>
                    </table>
                    <br/>

                  </div>
                </div>
              </el-form>

            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="!check" id="notfound">
      <div class="notfound">
        <div class="notfound-404">
          <h1>Oops!</h1>
          <h2>404 - Link của bạn đã hết hạn</h2>
        </div>
        <a href="/login" >Đến trang đăng nhập</a>
      </div>
    </div>
  </div>

</template>
<script>
import AuthService from "@/services/auth.service";

export default {
  name: "ForgotChangePassword",
  data() {
    return {
      rules: {
        new_password: [
          {required: true, message: 'Nhập mật khẩu mới', trigger: 'blur'}
        ],
        new_password_confirm: [
          {required: true, message: 'Nhập lại mật khẩu mới', trigger: 'blur'}
        ]
      },
      form: {
        new_password: "",
        new_password_confirm:""
      },
      formData: {
        token:'',
        newPassword: ""
      },
      message: "",
      check:""

    };
  },
  mounted() {
    const params = {
      "token": this.$route.query.token,
    }
    this.formData.token=this.$route.query.token;
    AuthService.checkChangePasswordForgot(params).then(response =>{
      this.check=response.data
    }).catch(error=>{
      console.log(error)
    })
  },
  methods: {
    submit(formName) {
      this.$refs[formName].validate((valid) => {
        if (this.form.new_password == "" || this.form.new_password_confirm == "" ) {
          this.message = 'Chưa nhập đủ thông tin!'
        }
        if (this.form.new_password !== this.form.new_password_confirm) {
          this.message = 'Mật khẩu mới không khớp !'
        }
        else if (valid && this.form.new_password === this.form.new_password_confirm) {
            this.formData.newPassword = this.form.new_password;
            AuthService.changePasswordForgot(this.formData).then(() => {
                this.$router.push('/login')
                this.$swal.fire(
                    {
                      title: "Thay đổi mật khẩu thành công",
                      icon: 'success',
                      timer: 2000,
                      timerProgressBar: true,
                    }
                )
              }
            )
            .catch(error => {
              this.$refs[formName].resetFields();
              this.$swal.fire({
                title: "Lỗi",
                text: error.response.data.error.message,
                icon: "error",
                timer: 2000,
                timerProgressBar: true,
              })
              this.message = ''
            })
          }
      });
    },

  },
};
</script>
<style scoped>
#my-strictly-unique-vue-upload-multiple-image {

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
  color: #ffffff
}

.btn-signup:hover {
  background-color: #a31c20;

}
</style>
