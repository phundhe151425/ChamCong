<template>
  <html lang="en">
  <body>
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

      <form name="form" @submit.prevent="handleLogin">
        <div class="form-outline mb-4">
          <input
              placeholder="Nhập email"
              v-model="user.username"
              v-validate="'required'"
              type="text"
              class="form-control"
              name="username"
          />
          <small v-if="messageEmail !== null" style="color: red">
            {{ messageEmail }}
          </small>
        </div>
        <div class="form-group">
          <input
              placeholder="Nhập mật khẩu"
              v-model="user.password"
              v-validate="'required'"
              type="password"
              class="form-control"
              name="password"
          />
          <small v-if="messagePass !== null" style="color: red">
            {{ messagePass }}
          </small>
        </div>
        <div class="text-center pt-1 mb-5 pb-1">
          <button :disabled="loading" class="submit">Login</button>
          <br />
          <a
              style="margin-top: 20px; color: #33acff"
              href="http://localhost:8081/forgotPassword"
          >Quên mât khẩu</a
          >
          <br />
          <small v-if="messageForm" style="color: red">{{
              messageForm
            }}</small>
          <br />
          <small style="color: red" v-if="message">{{ message }}</small>
        </div>
      </form>
    </div>
  </section>
  </body>
  </html>
</template>
<script>
import User from "../../models/user";
export default {
  name: "LoginVue",
  data() {
    return {
      user: new User("", ""),
      loading: false,
      message: "",
      a: "",
      errAvail: "",
      checkAvail: true,
      errEmail: "",
      checkEmail: true,
      errPass: "",
      checkPass: true,
      check: true,
      messageEmail: "",
      messagePass: "",
      messageForm: "",
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  created() {
    this.$router.push("/login");
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    validEmail: function (email) {
      var re =
          /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },
    handleLogin() {
      if (!this.user.username && !this.user.password) {
        this.messageForm = "Vui lòng nhập thông tin tài khoản";
        this.messageEmail = "";
        this.messagePass = "";
        this.message = "";
        this.check = false;
      } else {
        this.messageForm = "";
      }

      if (!this.user.username && this.user.password) {
        this.messageEmail = "Vui lòng nhập email";
        this.messageForm = "";
        this.message = "";
        this.check = false;
      } else {
        this.messageEmail = "";
      }
      if (!this.user.password && this.user.username) {
        this.messagePass = "Vui lòng nhập mật khẩu";
        this.messageForm = "";
        this.message = "";
        this.check = false;
      } else {
        this.messagePass = "";
      }
      if (this.user.username && this.user.password) {
        this.check = true;
      }
      if (this.check === true) {
        this.loading = true;
        this.$validator.validateAll().then((isValid) => {
          if (!isValid) {
            this.loading = false;
            return;
          }
          if (this.user.username && this.user.password) {
            this.$store.dispatch("auth/login", this.user).then(
                () => {
                  this.$router.push("/calender");
                },
                (error) => {
                  this.loading = false;
                  this.a = error.response && error.response.data;
                  if (
                      error.response.data.message ==
                      "Account have been lock by admin"
                  ) {
                    this.message =
                        "Tài khoản của bạn bị khóa hiện tại chưa thể đăng nhập";
                  } else {
                    this.message = "Email hoặc mật khẩu không chính xác";
                  }
                }
            );
          }
        });
      }
    },
  },
};
</script>

<style scoped>
/* @media (min-width: 768px) {
  .gradient-form {
    height: 100vh !important;
  }
}
@media (min-width: 769px) {
  .gradient-custom-2 {
    border-top-right-radius: 0.3rem;
    border-bottom-right-radius: 0.3rem;
  }
} */
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

/* .form-control {
  width: 100%;
  position: relative;
  margin-bottom: 24px;
} */

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
