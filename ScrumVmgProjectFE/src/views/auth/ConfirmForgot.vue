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
      <div class="card-body p-md-5 mx-md-4">
        <div class="text-center">
          <h4 class="mt-1 mb-5 pb-1">PHÁT TRIỂN CÙNG CÔNG NGHỆ</h4>
        </div>
        Hệ thống đã gửi link đổi mật khẩu mới về EMAIL của bạn
        KIỂM TRA EMAIL và đổi  mật khẩu mới để đăng nhập
        <br /><br />
        <a
            style="margin-top: 20px; color: #33acff"
            href="http://localhost:8081/login"
        >Trở lai trang đăng nhâp</a
        >
      </div>
    </div>
  </section>
  </body>
  </html>
</template>
<style scoped>
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
