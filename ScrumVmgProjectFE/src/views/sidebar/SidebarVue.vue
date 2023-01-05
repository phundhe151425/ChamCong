<template>
  <nav class="navbar fixed-top container-fluid" style="padding: 14px 0">
    <div class="user-detail nav-item dropdown " v-if="currentUser ">
      <a
          class="nav-link dropdown-toggle"
          href="#"
          role="button"
          data-bs-toggle="dropdown"
          aria-expanded="false"
      >
        <b-avatar v-if="currentUser.user.cover == null" class="mr-3"></b-avatar>
        <b-avatar v-if="currentUser.user.cover != null" class="mr-3" v-bind:src="`http://localhost:8080/` + currentUser.user.cover"></b-avatar>
        {{ currentUser.user.fullName }}
      </a>
      <ul class="dropdown-menu" style="left: -90px">
        <li>
          <a class="dropdown-item">
            <router-link to="/profile" class="nav-link sel">
              <font-awesome-icon icon="user" />
              Thông tin cá nhân
            </router-link>
          </a>
        </li>
        <li>
          <a class="dropdown-item">
            <router-link to="/changepassword" class="nav-link">
              <i class="el-icon-refresh-left"></i>
              Thay đổi mật khẩu
            </router-link>
          </a>
        </li>
        <li>
          <hr class="dropdown-divider" />
        </li>
        <li>
          <a class="dropdown-item" href="#">
            <a class="nav-link sel" href @click.prevent="logOut">
              <font-awesome-icon icon="sign-out-alt" />
              Đăng xuất
            </a>
          </a>
        </li>
      </ul>
    </div>
    <div class="container-fluid">
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="offcanvas"
        data-bs-target="#offcanvasDarkNavbar"
        aria-controls="offcanvasDarkNavbar"
        v-if="currentUser"
        style="border: none"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <ul class="dropdown-menu navbar-brand" style="float: right">
        <li>
          <a class="dropdown-item">
            <router-link to="/profile" class="nav-link sel">
              <font-awesome-icon icon="user" />
              Thông tin cá nhân
            </router-link>
          </a>
        </li>
        <li>
          <a class="dropdown-item">
            <router-link to="/changepassword" class="nav-link">
              <i class="el-icon-refresh-left"></i>
              Thay đổi mật khẩu
            </router-link>
          </a>
        </li>
        <li>
          <hr class="dropdown-divider" />
        </li>
        <li>
          <a class="dropdown-item" href="#">
            <a class="nav-link sel" href @click.prevent="logOut">
              <font-awesome-icon icon="sign-out-alt" />
              Đăng xuất
            </a>
          </a>
        </li>
      </ul>
      <div
        class="offcanvas offcanvas-start"
        tabindex="-1"
        id="offcanvasDarkNavbar"
        aria-labelledby="offcanvasDarkNavbarLabel"
      >
        <div class="offcanvas-header" style="margin: auto">
          <h5 class="offcanvas-title" id="offcanvasDarkNavbarLabel">
            <img src="../../assets/new_logo.png" width="50px" />
          </h5>
          <button
            type="button"
            class="btn-close btn-close-white text-center"
            data-bs-dismiss="offcanvas"
            aria-label="Close"
          ></button>
        </div>
        <div class="offcanvas-body">
          <ul class="navbar-nav justify-content-end flex-grow-1 pe-3">
            <li class="nav-item">
              <a class="nav-link active" aria-current="page" href="/profile">
                <router-link to="/calender" v-if="currentUser" class="nav-link sel">
                  <font-awesome-icon icon="home" />
                  Trang chủ
                </router-link>
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link">
                <router-link to="/user" v-if="currentUser" class="nav-link sel">
                  <i class="el-icon-question"></i>
                  Chấm công của tôi
                </router-link>
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link">
                <router-link to="/myFurlough" v-if="currentUser" class="nav-link sel">
                  <i class="el-icon-question"></i>
                  Nghỉ phép của tôi
                </router-link>
              </a>
            </li>
            <li class="nav-item" v-if="showAdminBoard || showModeratorBoard">
              <a class="nav-link">
                <router-link to="/report" class="nav-link sel">
                  <i class="el-icon-document-copy"> </i> Quản lý chấm công
                </router-link>
              </a>
            </li>
            <li class="nav-item" v-if="showModeratorBoard">
              <a class="nav-link">
                <router-link to="/timesheetmod" class="nav-link sel">
                  <i class="el-icon-files"> </i> Quản lý chấm công
                </router-link>
              </a>
            </li>
            <li class="nav-item" v-if="showAdminBoard">
              <a class="nav-link">
                <router-link to="/manage" class="nav-link sel">
                  <i class="el-icon-files"> </i> Quản lý nhân viên
                </router-link>
              </a>
            </li>
            <li class="nav-item" v-if="showAdminBoard">
              <a class="nav-link">
                <router-link to="/manageholiday" class="nav-link sel">
                  <i class="el-icon-files"> </i> Quản lí nghỉ lễ
                </router-link>
              </a>
            </li>

            <li class="nav-item" v-if="currentUser">
              <a class="nav-link">
                <router-link to="/managerequest" class="nav-link sel">
                  <i class="el-icon-files"> </i> Quản lý đề xuất
                </router-link>
              </a>
            </li>

            <li class="nav-item" v-if="showAdminBoard || showModeratorBoard">
              <a class="nav-link">
                <router-link to="/reportFurlough" class="nav-link sel">
                  <i class="el-icon-document-copy"></i> Thống kê nghỉ phép
                </router-link>
              </a>
            </li>

            <li class="nav-item" v-if="showAdminBoard">
              <a class="nav-link">
                <router-link to="/timesheetadmin" class="nav-link sel">
                  <i class="el-icon-files"></i>  Thống kê chấm công
                </router-link>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
export default {
  data() {
    return {};
  },
  props: {},
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
  methods: {
    logOut() {
      this.$store.dispatch("auth/logout");
      window.location.replace("http://localhost:8081/login");
    },
  },
};
</script>
<style>
</style>

<style scoped>
* {
  font-family: "Montserrat", sans-serif;
}
.navbar {
  background-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  font-weight: 600;
}
.user-detail{
  position: fixed;
  top: 10px;
  right: 10px;
  z-index: 1030;
}

#offcanvasDarkNavbar{
  width:16%;
}


.nav-link:hover{
  color: #75c4c0;
}

.nav-link:focus{
  color: #ed9696;
}
@media screen and (max-width: 700px) {
      #offcanvasDarkNavbar{
          width:60%
        }
}

@media screen and (min-width: 750px) and (max-width: 950px) {
      #offcanvasDarkNavbar{
          width:30%
        }
}

@media screen and (min-width: 1024px) and (max-width: 1280px) {
      #offcanvasDarkNavbar{
          width:25%
        }
}


</style>
