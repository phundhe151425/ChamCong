<template>
  <div style="padding-bottom: 20px">
    <h2 style="font-weight: 700;line-height:29.26px;margin-left: 40px">Thống kê nghỉ phép của tôi</h2>
    <div class="selected_year">
      <span class="demonstration" style="margin-right: 10px">Năm </span>
      <el-date-picker
          style="font-weight: bold"
          v-model="currentYear"
          type="year"
          placeholder="Chọn một năm"
          value-format="yyyy"
          @change="getAllByYearAndUser"
      >
      </el-date-picker>
    </div>
    <div style="margin-top: 100px;margin-left: 15%">
    <div>
      <div class="content-wrap">Số phép được nghỉ năm {{currentYear}}<span class="value-content">{{data.availibleCurrentYear}}</span></div>
      <div class="content-wrap">Số phép dư đầu kỳ năm {{currentYear}}<span class="value-content">{{data.availibleUsePresentMonth}}</span></div>
      <div class="content-wrap" @click="handleDrop">Số phép đã sử dụng theo tháng<span class="value-content">
        <i v-show="!isDrop" class="el-icon-arrow-right"  style="font-weight: bold"></i>
        <i v-show="isDrop" class="el-icon-arrow-down"  style="font-weight: bold"></i></span>
      </div>
      <div :class="{
          drop:isDrop,
          'not-drop':!isDrop,
      }"
           v-for="(n,index) in data.furloughs "
           :key="index"
      >
        <div class="content-wrap-month">Tháng {{index +1}} <span class="value-content">{{n.usedInMonth}}</span></div>
      </div>
      <div class="content-wrap">Tổng số phép nghỉ trước tháng 4<span class="value-content">{{data.usedBeforeApril}}</span></div>
      <div class="d-flex flex-row">
        <div class="halfContent-wrap">Tổng số phép còn lại năm {{currentYear -1}}<span class="value-content">{{data.leftLastYear}}</span></div>
        <div class="halfContent-wrap">Tổng số phép còn lại năm {{ currentYear }}<span class="value-content">{{data.leftCurentYear}}</span></div>
      </div>
      <div class="d-flex flex-row">
        <div class="halfContent-wrap">Tổng số phép nghỉ năm {{ currentYear }}<span class="value-content">{{data.usedInYear}}</span></div>
        <div class="halfContent-wrap">Trả phép<span class="value-content">{{data.payFurlough}}</span></div>
      </div>

      <div class="content-wrap">Tổng số phép được sử dụng đến tháng hiện tại<span class="value-content">{{data.furloughs[currentMonth].availableUsedTillMonth}}</span></div>

    </div>
    </div>
  </div>
</template>

<script>
import FurloughService from "@/services/furlough.service";

export default {
  name: "FurloughSelf",
  data(){
    return{
      currentYear:new Date().getFullYear().toString(),
      currentMonth:new Date().getMonth(),
      data:'',
      isDrop:false
    }
  },
  methods : {
    handleDrop(){
      this.isDrop=!this.isDrop
    },
    getAllByYearAndUser(){
      FurloughService.getAllByYearAndUser({
        'year':this.currentYear,
        'userCode':this.currentUser.user.code
      }).then(respone =>{
        this.data=respone.data
        console.log(this.data)
      })

    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    this.getAllByYearAndUser();
  }
}
</script>

<style scoped>
.content-wrap{
  width: 70vw;
  height:51px;
  background-color: #E0E0E0;
  border-radius: 8px;
  padding-left: 16px;
  padding-top: 12px;
  padding-bottom: 5px;
  font-size: 20px;
  font-weight: bold;
  line-height: 24.38px;
  margin-bottom: 15px;
}
.content-wrap-month{
  width: 70vw;
  height:51px;
  border-bottom: 1px solid #BDBDBD;
  padding-left: 16px;
  padding-top: 12px;
  padding-bottom: 5px;
  font-size: 20px;
  font-weight: bold;
  line-height: 24.38px;
  margin-bottom: 15px;
}
.halfContent-wrap{
  width: 34.4vw;
  height:51px;
  background-color: #E0E0E0;
  border-radius: 8px;
  padding-left: 16px;
  padding-top: 12px;
  padding-bottom: 5px;
  font-size: 20px;
  font-weight: bold;
  line-height: 24.38px;
  margin-bottom: 15px;
  margin-right: 22px;
}
.selected_year{
  position: absolute;
  right: 14%;
}
.value-content{
  font-size: 20px;
  font-weight: bold;
  line-height: 24.38px;
  float: right;
  padding-right: 16px;
}
.not-drop{
  display: none;
  height: 0;
}
.drop{
  height: 100%;
  transition: height 2s ease-in-out;
}
</style>