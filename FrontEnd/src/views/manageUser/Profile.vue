<template>
  <div style="padding-bottom: 500px">
    <section
        class="section about-section gray-bg"
        id="about"
        style="margin-top: 150px"
    >
      <div
          style="
          border: 2px solid black;
          box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
          border-radius: 10px;
          background-color: white;
        "
          class="container"
      >
        <div class="row align-items-center flex-row-reverse">
          <p
              style="
              text-align: center;
              background-color: #d9d9d9;
              padding: 20px;
              font-weight: bold;
            "
          >
            THÔNG TIN NHÂN VIÊN
          </p>
          <div class="col-lg-6">
            <div class="about-text go-to">
              <div class="row about-list">
                <div
                    style="border-bottom: 1px solid black; width: 500px"
                    class="media"
                >
                  <span style="font-size: 20px"> Mã nhân viên: </span>
                  <span
                      style="float: right; margin-right: 6px; font-size: 20px"
                  >
                    {{ currentUser.user.code }}
                  </span>
                </div>
                <div
                    style="border-bottom: 1px solid black; width: 500px"
                    class="media"
                >
                  <span style="font-size: 20px"> Họ và tên nhân viên: </span>
                  <span
                      style="float: right; margin-right: 6px; font-size: 20px"
                  >
                    {{ currentUser.user.fullName }}
                  </span>
                </div>
                <div
                    style="border-bottom: 1px solid black; width: 500px"
                    class="media"
                >
                  <span style="font-size: 20px"> Giới tính: </span>
                  <span style="float: right; margin-right: 6px; font-size: 20px">
                    {{ currentUser.user.gender }}
                  </span>
                </div>
                <div style="border-bottom: 1px solid black; width: 500px"
                     class="media">
                  <span style="font-size: 20px"> Vị trí: </span>
                  <span style="float: right; margin-right: 6px; font-size: 20px">
                    {{ currentUser.user.position.name }}
                  </span
                  >
                </div>
                <div
                    style="border-bottom: 1px solid black; width: 500px"
                    class="media"
                >
                  <span style="font-size: 20px"> Email: </span>
                  <span style="float: right; margin-right: 6px; font-size: 20px">
                    {{ currentUser.user.username }}
                  </span>
                </div>
                <div
                    style="border-bottom: 1px solid black; width: 500px"
                    class="media"
                >
                  <span style="font-size: 20px"> Phòng ban: </span>
                  <span style="float: right; margin-right: 6px; font-size: 20px">
                    {{ currentUser.user.departments.name }}
                  </span>
                </div>
                <div
                    style="
                    border-bottom: 1px solid black;
                    width: 500px;
                    margin-bottom: 50px;
                  "
                    class="media"
                >
                  <span style="font-size: 20px"> Ngày đi làm: </span>
                  <span
                      style="float: right; margin-right: 6px; font-size: 20px"
                  >
                    {{ currentUser.user.startWork }}
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div class="col-lg-6">
            <div style="margin-left: 100px" class="about-avatar">
              <!--            currentUser.user.cover-->
              <img
                  v-if="currentUser.user.cover != null"
                  v-bind:src="`http://localhost:8100/` + currentUser.user.cover"
                  width="250px"
              />
              <img
                  v-if="currentUser.user.cover == null"
                  src="../../assets/user.jpg"
                  width="250px"
              />
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
<script>
export default {
  name: "ProfileVue",
  data() {
    return {};
  },
  methods: {
    getUser() {
      console.log(this.currentUser.user)
    }
  },
  computed: {
    currentUser() {
      // return JSON.parse(localStorage.getItem('user'));
      console.log(this.$store.state.auth.user)
      return this.$store.state.auth.user;
    },
  },
  created() {
    this.getUser();
  },

  mounted() {
    if (!this.currentUser) {
      this.$router.push("/login");
    }
  },
};
</script>
<style scoped>
* {
  font-size: 16px;
}

.about-text h3 {
  font-size: 45px;
  font-weight: 700;
  margin: 0 0 6px;
}

@media (max-width: 767px) {
  .about-text h3 {
    font-size: 35px;
  }
}

.about-text h6 {
  font-weight: 600;
  margin-bottom: 15px;
}

@media (max-width: 767px) {
  .about-text h6 {
    font-size: 18px;
  }
}

.about-text p {
  font-size: 18px;
  max-width: 450px;
}

.about-text p mark {
  font-weight: 600;
  color: #20247b;
}

.about-list {
  padding-top: 10px;
}

.about-list .media {
  padding: 5px 0;
}

.about-list label {
  color: #20247b;
  font-weight: 600;
  width: 88px;
  margin: 0;
  position: relative;
}

.about-list label:after {
  content: "";
  position: absolute;
  top: 0;
  bottom: 0;
  right: 11px;
  width: 1px;
  height: 12px;
  background: #20247b;
  -moz-transform: rotate(15deg);
  -o-transform: rotate(15deg);
  -ms-transform: rotate(15deg);
  -webkit-transform: rotate(15deg);
  transform: rotate(15deg);
  margin: auto;
  opacity: 0.5;
}

.about-list p {
  margin: 0;
  font-size: 15px;
}

@media (max-width: 991px) {
  .about-avatar {
    margin-top: 30px;
  }
}

.about-section .counter {
  padding: 22px 20px;
  background: #ffffff;
  border-radius: 10px;
  box-shadow: 0 0 30px rgba(31, 45, 61, 0.125);
}

.about-section .counter .count-data {
  margin-top: 10px;
  margin-bottom: 10px;
}

.about-section .counter .count {
  font-weight: 700;
  color: #20247b;
  margin: 0 0 5px;
}

.about-section .counter p {
  font-weight: 600;
  margin: 0;
}

mark {
  background-image: linear-gradient(
      rgba(252, 83, 86, 0.6),
      rgba(252, 83, 86, 0.6)
  );
  background-size: 100% 3px;
  background-repeat: no-repeat;
  background-position: 0 bottom;
  background-color: transparent;
  padding: 0;
  color: currentColor;
}

.theme-color {
  color: #fc5356;
}

.dark-color {
  color: #20247b;
}
</style>
