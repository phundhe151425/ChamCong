<template>
  <html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
        rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css"
    />
    <link rel="stylesheet" href="./style.css" />
    <link
        href="https://fonts.googleapis.com/css2?family=Montserratdisplay=swap"
        rel="stylesheet"
    />
    <title>Forgot</title>
  </head>
  <body>
  <div class="loading" id="loading">
    <img
        src="https://img.pikbest.com/png-images/20190918/cartoon-snail-loading-loading-gif-animation_2734139.png!bw700"
        alt="loading"
    />
  </div>
  <section class="side">
    <img src="../../assets/Login.png" alt="" />
  </section>

  <section class="main">
    <div class="login-container">
      <img
          src="http://danhbaict.vn/uploads/images/vmg%20logo.jpg"
          style="width: 185px"
          alt="logo"
      />
      <div class="separator"></div>
      <p class="welcome-message">Nhập thông tin đăng nhập</p>

      <form v-loading="loading" accept-charset="utf-8">
        <div class="form-outline mb-4">
          <input
              placeholder="Enter Your Email"
              v-model="email"
              v-validate="{ required: true }"
              type="email"
              class="form-control"
              name="email"
          />
          <small v-if="errEmail !== null" style="color: red">
            {{ errEmail }}
          </small>
        </div>
        <div class="text-center pt-1 mb-5 pb-1">
          <el-button class="submit" @click="handleForgot"
          >Xác Nhận</el-button
          >
          <br />
          <div style="margin-top: 20px">
            <a style="color: #33acff" href="http://localhost:8081/login"
            >Quay lại trang Đăng nhập</a
            >
          </div>
          <br />
        </div>
      </form>
    </div>
  </section>
  </body>
  </html>
</template>
<script>
import UserService from "@/services/user.service";
export default {
  name: "ForgotVue",
  data() {
    return {
      message: "",
      email: "",
      errEmail: "",
      checkEmail: true,
      users: [],
      a: "",
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
  mounted() {},
  methods: {
    showLoading: function () {
      const iconLoading = document.getElementById("loading");
      iconLoading.style.display = "flex";
    },

    hideLoading: function () {
      const iconLoading = document.getElementById("loading");
      iconLoading.style.display = "none";
    },

    validEmail: function (email) {
      var re =
          /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },

    async handleForgot() {
      let response = await UserService.getAllUser();
      this.users = response.data;

      for (let i = 0; i < this.users.length; i++) {
        if (this.email === this.users[i].username) {
          this.errEmail = "";
          this.checkEmail = true;
          break;
        } else {
          this.errEmail = "Email không tồn tại";
          this.checkEmail = false;
        }
      }

      if (!this.email) {
        this.errEmail = "Vui lòng nhập email nhân viên";
        this.checkEmail = false;
      } else if (!this.validEmail(this.email)) {
        this.errEmail = "Vui lòng nhập đúng định dạng email";
        this.checkEmail = false;
      } else if (
          this.validEmail(this.email) &&
          this.email &&
          this.checkEmail === true
      ) {
        this.errEmail = "";
        this.checkEmail = true;
      }

      if (this.checkEmail === true) {
        this.showLoading();
        setTimeout(() => {
          this.loading = true;
          this.$validator.validateAll().then((isValid) => {
            if (!isValid) {
              this.loading = false;
              return;
            }
            if (this.email) {
              const params = {
                email: this.email,
              };
              UserService.forgotPassword(params).then((response) => {
                if (response.data == true) {
                  this.$router.push("/confirmForgot");
                } else {
                  (error) => {
                    this.message =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();
                  };
                }
              });
            }
          });
        }, 2000);
      }
    },
  },
};
</script>
<style scoped>
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
@media (min-width: 768px) {
  .gradient-form {
    height: 100vh !important;
  }
}
@media (min-width: 769px) {
  .gradient-custom-2 {
    border-top-right-radius: 0.3rem;
    border-bottom-right-radius: 0.3rem;
  }
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Montserrat", sans-serif;
  color: #303433;
}

body {
  min-height: 88vh;
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
}

section {
  display: flex;
  justify-content: center;
  align-items: center;
}

section.side {
  background: url(../../assets/bk1.png)
  no-repeat;
  background-size: 100% 102%;
}

.side img {
  width: 50%;
  max-width: 50%;
}

.login-container {
  max-width: 450px;
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.title {
  text-transform: uppercase;
  font-size: 3em;
  font-weight: bold;
  text-align: center;
  letter-spacing: 1px;
}

.separator {
  width: 150px;
  height: 4px;
  background-color: #c73b4b;
  margin: 24px;
}

.welcome-message {
  text-align: center;
  font-size: 1.1em;
  line-height: 28px;
  margin-bottom: 30px;
  color: #696969;
}

.login-form {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.form-control {
  width: 100%;
  position: relative;
  margin-bottom: 24px;
}

input,
button {
  border: none;
  outline: none;
  /* border-radius: 30px; */
  font-size: 1.1em;
}

input {
  width: 100%;
  background: #e6e6e6;
  color: #333;
  letter-spacing: 0.5px;
  padding: 14px 64px;
}

input ~ i {
  position: absolute;
  left: 32px;
  top: 50%;
  transform: translateY(-50%);
  color: #888;
  transition: color 0.4s;
}

input:focus ~ i {
  color: #843bc7;
}

button.submit {
  color: #fff;
  padding: 14px 64px;
  margin: 32px auto;
  text-transform: uppercase;
  letter-spacing: 2px;
  font-weight: bold;
  background-image: linear-gradient(to right, #c54233, #e16015);
  cursor: pointer;
  transition: opacity 0.4s;
}

button.submit:hover {
  opacity: 0.9;
}

/* ----  Responsiveness   ----  */
@media (max-width: 780px) {
  body {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .side {
    display: none;
  }
}
</style>
