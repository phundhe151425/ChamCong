<template>
<span>
  <div   v-for="(note,index) in noteSort" :key="index">
    <div class="tab">
          <!--  Admin Edit-->
    <div v-if="note.noteCatergory.note_catergory_id==1">
      <div class="tab-type edit"><span class="dot" style="background-color: #F5DD29"></span>Edit</div>
      <div class="avatar">
        <b-avatar class="me-3" size="30px" v-bind:src="`http://localhost:8080/` + note.adminEdit.cover"></b-avatar>
        <span class="fw-bold">{{note.adminEdit.fullName}}</span>
      </div>
      <div class="content">
        {{note.content}}
      </div>
    </div>
      <!--  Khuôn mặt -->
    <div v-if="note.noteCatergory.note_catergory_id==2">
      <div class="tab-type face"><span class="dot" style="background-color: #FFAF3F"></span>FaceKeep</div>
    </div>
      <!--  Request-->
    <div v-if="note.noteCatergory.note_catergory_id==3">
      <div class="tab-type request"><span class="dot" style="background-color: #EF7564"></span>Đề xuất</div>
      <div class="avatar">
        <b-avatar class="me-3" size="30px" v-bind:src="`http://localhost:8080/` + note.approversRequest.cover"></b-avatar>
        <span class="fw-bold">{{note.approversRequest.fullName}}</span>
      </div>
      <div class="content">
        {{note.content}}
      </div>
    </div>
      <!--  LỄ-->
    <div v-if="note.noteCatergory.note_catergory_id==4">
      <div class="tab-type holiday"><span class="dot" style="background-color: #5BA4CF"></span>Ngày lễ</div>
       <div class="content">
         {{note.content}}
      </div>
    </div>
    <div v-if="note.lastSign!=null" class="tab-history">
        {{note.lastSign.name}} <i class="el-icon-right"></i> {{note.signChange.name}}
    </div>
    <div v-if="note.lastSign==null" class="tab-history">
         <i class="el-icon-right"></i> {{note.signChange.name}}
    </div>
    <div class="time">
        {{note.createDate.replace("T"," ").split(".")[0].toString()}}
    </div>
  </div>
  </div>
</span>
</template>

<script>
export default {
  name: "NoteLog",
  props: {
    notes:Array
  },
  data() {
    return {
      noteSort:[]
    };

  },
  mounted() {
    this.noteSort = this.$props.notes.sort(function(a, b){
      var a1= a.note_log_id, b1= b.note_log_id;
      if(a1== b1) return 0;
      return a1> b1? 1: -1;
    });
  }
}
</script>

<style scoped>
.time{
  font-size: small;
}
.content{
  margin-bottom: 10px;
  margin-top: 10px;
}
.long-text{
  width : 200px;
  overflow:hidden;
  display:inline-block;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.tab{
  border-bottom: 1px solid #BDBDBD;
  margin-bottom: 10px;
}
.tab-history{
  width: fit-content;
  height:fit-content;
  border-radius: 8px;
  font-weight: bold;
  color: #172b4d;
  background-color: #EDDBF4;
  padding-left: 5px;
  padding-right: 5px;
  margin-bottom: 10px;
}
.tab-type{
  width: 120px;
  height:fit-content;
  border-radius: 8px;
  font-weight: bold;
  color: #172b4d;
  margin-bottom: 10px;
}
.request{
  background-color: #F5D3CE;
}
.holiday{
  background-color:  #BCD9EA;
}
.edit{
  background-color: #FAF3C0;
}
.face{
  background-color: #FCE6C6;
}
.dot {
  height: 10px;
  width: 10px;
  border-radius: 50%;
  display: inline-block;
  margin-right: 10px;
  margin-left: 8px;
}

</style>