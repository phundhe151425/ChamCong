import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const router = new Router({
  mode: "history",
  routes: [
    {
      path: "/login",
      component: () => import("./views/auth/Login.vue"),
    },
    {
      path: "/",
      redirect: "/login",
    },
    {
      path: "/register",
      component: () => import("./views/manageUser/ManageUser.vue"),
    },

    {
      path: "/add-user",
      name: "",
      component: () => import("./views/manageUser/Add-User.vue"),
    },
    {
      path: "/user",
      name: "user",
      component: () => import("./views/manageUser/BoardUser.vue"),
    },
    {
      path: "/user/:code/:departmentName/:fullName",
      name: "user",
      component: () => import("./views/manageSalary/ViewDetailLogs.vue"),
    },
    {
      path: "/report",
      name: "report",
      component: () => import("./views/report/ReportSalary.vue"),
    },
    {
      path: "/forgotPassword",
      name: "forgotPassword",
      component: () => import("./views/auth/ForgotPassword.vue"),
    },
    {
      path: "/confirmForgot",
      name: "confirmForgot",
      component: () => import("./views/auth/ConfirmForgot.vue"),
    },
    {
      path: "/changepassword",
      name: "changepassword",
      component: () => import("./views/auth/ChangePassword.vue"),
    },
    {
      path: "/calender",
      name: "calender",
      component: () => import("./views/calendar/CalenderReport.vue"),
    },
    {
      path: "/timesheetmod",
      name: "timesheetmod",
      component: () => import("./views/manageSalary/TimeSheetMod.vue"),
    },

    {
      path: "/user/:id",
      name: "edit",
      component: () => import("./views/manageUser/EditUser.vue"),
    },

    {
      path: "/timesheetadmin",
      name: "timesheetadmin",
      component: () => import("./views/manageSalary/TimeSheetsAdmin.vue"),
    },
    {
      path: "/manage",
      name: "manage",
      component: () => import("./views/manageUser/ManageUser.vue"),
    },
    {
      path: "/profile",
      name: "",
      component: () => import("./views/manageUser/Profile.vue"),
    },
    {
      path: "/unpermist",
      name: "unpermist",
      component: () => import("./views/error/UnPermist.vue"),
    },
    {
      path: "/selfie",
      name: "selfie",
      component: () => import("./views/Selfie.vue"),
    },
    {
      path: "/managerequest",
      name: "managerequest",
      component: () => import("./views/request/ManageRequest.vue"),
    },
    {
      path: "/requestdetail",
      name: "requestdetail",
      component: () => import("./views/request/RequestDetail.vue"),
    },
    {
      path: "/manageholiday",
      name: "manageholiday",
      component: () => import("./views/holiday/ManageHoliday.vue"),
    },
    {
      path: "/reportFurlough",
      name: "reportFurlough",
      component: () => import("./views/report/ReportFurlough.vue"),
    },
    {
      path: "/reset_password-tokenLink",
      component: () => import("./views/auth/ForgotChangePassword.vue"),
    },
    {
      path: "/myFurlough",
      component: () => import("./views/manageUser/FurloughSelf.vue"),
    },
  ],
});

router.beforeEach((to, from, next) => {
  const publicPages = [
    "/login",
    "/register",
    "/home",
    "/forgotPassword",
    "/confirmForgot",
    "/",
      "/selfie"
  ];
  const userPages = [
    "/user",
    "/calender",
    "/profile",
    "/changepassword",
    "/unpermist",
    "/myFurlough",
    "/managerequest",
    "/requestdetail"
  ];
  const adminPages = [
    "/add-user",
    "/manage",
    "/timesheetadmin",
    "/report",
    "/reportFurlough",
    "/manageholiday"
  ];
  const managePages = ["/timesheetmod", "/report", "/reportFurlough"];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem("user");
  if (
    authRequired &&
    !loggedIn &&
    !to.path.startsWith("/reset_password-tokenLink")
  ) {
    next("/login");
  }
  if (
    (!authRequired && !loggedIn) ||
    to.path.startsWith("/reset_password-tokenLink")
  ) {
    next();
  } else {
    const admin = JSON.parse(localStorage.getItem("user")).roles.includes(
      "ROLE_ADMIN"
    );
    const manage = JSON.parse(localStorage.getItem("user")).roles.includes(
      "ROLE_MANAGE"
    );
    if (
      ((adminPages.includes(to.path) || to.path.startsWith("/user/")) && admin) ||
      (managePages.includes(to.path) && manage) ||
      userPages.includes(to.path) ||
      publicPages.includes(to.path)
    )
      next();
    else next("/unpermist");
  }
});
export default router;
