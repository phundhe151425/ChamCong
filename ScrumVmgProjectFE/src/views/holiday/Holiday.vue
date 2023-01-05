<template>
  <div>
    <span>{{publicHoliday}}</span>
  </div>
</template>

<script>

// import axios from "axios";
import axios from "axios";

export default {
  name: "HolidayPublic",
  data (){
    return{
      publicHoliday:''
    }

  },
  mounted(){
    const param ={
      api_key:'14d71e06c50423ba0eb79afce876844807499a1a',
      country: 'VN',
      year:new Date().getFullYear(),
      type:'national',
    }
    axios.get("https://calendarific.com/api/v2/holidays",{params:param}).then(response => {
      this.publicHoliday =response.data.response.holidays
    })
    console.log(this.publicHoliday)
  }
}
</script>

<style scoped>

</style>