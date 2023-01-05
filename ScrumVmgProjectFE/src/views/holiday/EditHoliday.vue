<template>
  <el-dialog
      style="text-align: center; font-weight: bold"
      title="Chỉnh sửa ngày nghỉ lễ"
      :visible.sync="dialogFormVisible"
  >

    <form id="formAddHoliday">
      <div class="row register-form">
        <div class="col-md-8">
          <table class="text-start">
            <tr style="height: 70px">
              <td style="width: 150px">
                Họ và tên<span style="color: red">*</span>
              </td>
              <td style="width: 500px">
                <div class="form-group">
                  <el-input
                      v-model="form.name"
                      type="text"
                      name="name"
                      placeholder="Họ và tên"
                      autocomplete="off"
                  >
                  </el-input>
                  <small v-if="errName !== null" style="color: red">
                    {{ errName }}
                  </small>
                </div>
              </td>
            </tr>
            <tr style="height: 70px">
              <td style="width: 150px">
                Email<span style="color: red">*</span>
              </td>
              <td style="width: 500px">
                <div class="form-group">
                  <el-date-picker
                      v-model="form.dateFrom"
                      placeholder="Chọn ngày bắt đầu"
                      value=""
                      name="dateFrom"
                      autocomplete="off"
                  >
                  </el-date-picker>
                </div>
                <small v-if="errDateFrom !== null" style="color: red">
                  {{ errDateFrom }}
                </small>
              </td>
            </tr>
            <tr style="height: 70px">
              <td style="width: 150px">
                Mã nhân viên<span style="color: red">*</span>
              </td>
              <td style="width: 500px">
                <div class="form-group">
                  <el-date-picker
                      placeholder="Chọn ngày kết thúc"
                      v-model="form.dateTo"
                      name="dateTo"
                      autocomplete="off"
                  >
                  </el-date-picker>
                </div>
                <small v-if="errDateTo !== null" style="color: red">
                  {{ errDateTo }}
                </small>
              </td>
            </tr>
          </table>
          <br />
        </div>
        <small style="color: green">
          {{ message }}
        </small>
      </div>
    </form>
    <span slot="footer" class="dialog-footer">
            <el-button @click="removeValidate(false)">Hủy</el-button>

            <el-button type="primary" @click="sendForm">Lưu</el-button>
          </span>
  </el-dialog>
</template>

<script>
export default {
  name: "EditHoliday"
}
</script>

<style scoped>

</style>