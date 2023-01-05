<template>
  <div id="app">
    <Sidebar />
    <div class="container-fluid">
      <router-view />
    </div>
  </div>
</template>
<script>
import Sidebar from '@/views/sidebar/SidebarVue'
import { sidebarWidth } from '@/views/sidebar/state'
import { collapsed, toggleSidebar } from '@/views/sidebar/state'
export default {
  components:{Sidebar},
  setup() {
    return { sidebarWidth,collapsed, toggleSidebar }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showAdminBoard() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_ADMIN');
      }
      return false;
    },
    showModeratorBoard() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_MANAGE');
      }
      return false;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    }
  }
};
</script>
<style scoped>
#app {
  /*min-height: 100%;*/
  /*width: 100%;*/
  /*padding-top: 80px;*/
  /*!*padding-bottom: 80px;*!*/
  /*background-image: url("@/assets/background3.jpg") ;*/
  /*background-position: center; !* Center the image *!*/
  /*background-repeat: no-repeat; !* Do not repeat the image *!*/
  /*background-size: cover; !**/
  font-size: 16px;
  font-family: 'Montserrat', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
#nav {
  padding: 30px;
}
#nav a {
  font-weight: bold;
  color: #2c3e50;
}
#nav a.router-link-exact-active {
  color: #42b983;
}

</style>
