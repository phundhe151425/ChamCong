<template>
  <div
    v-loading="loading"
    element-loading-text="Loading..."
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
    style="width: 100%"
  >
    <h1>Danh sách đề xuất</h1>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane style="width: 100%" label="Đề xuất của tôi" name="first">
        <div>
          <br />
          <div className="container" style="text-align: center">
            <div class="grid-content text-start"></div>
          </div>
          <div className="container" style="text-align: center">
            <el-row :gutter="20">
              <el-col :md="6" :lg="6" :xl="6">
                <div class="grid-content" style="margin-bottom: 20px">
                  <span style="">Trạng thái</span> &ensp;
                  <el-select
                    v-model="statusId"
                    @change="getAll"
                    placeholder="Trạng thái"
                  >
                    <el-option value="0" label="Tất cả"></el-option>
                    <el-option label="Chờ phê duyệt" value="1"></el-option>
                    <el-option label="Đã chấp thuận" value="2"></el-option>
                    <el-option label="Đã từ chối" value="3"></el-option>
                    <el-option label="Đã hủy" value="4"></el-option>
                  </el-select>
                </div>
              </el-col>

              <el-col :md="6" :lg="6" :xl="6" style="margin-bottom: 20px">
                <div class="grid-content"></div>
              </el-col>
              <el-col :md="6" :lg="6" :xl="6" style="margin-bottom: 20px">
                <div class="grid-content"></div>
              </el-col>

              <el-col :md="6" :lg="6" :xl="6" class="div-buttons">
                <div class="grid-content div-buttons">
                  <el-dropdown>
                    <el-button type="danger">
                      Tạo đề xuất<i
                        class="el-icon-arrow-down el-icon--right"
                      ></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item>
                        <el-link
                          class=""
                          type="danger"
                          style="margin-left: 20px; margin-right: 20px"
                          :underline="false"
                          round
                          @click="openFormNghi"
                          ><i class="el-icon-plus"></i>Đề xuất nghỉ
                        </el-link>
                      </el-dropdown-item>
                      <el-dropdown-item>
                        <el-link
                          class=""
                          type="danger"
                          style="margin-left: 20px; margin-right: 20px"
                          :underline="false"
                          round
                          @click="openFormChamCong"
                          ><i class="el-icon-plus"></i>Đề xuất chấm công
                        </el-link>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
              </el-col>
            </el-row>
          </div>

          <el-dialog
            style="text-align: center; font-weight: bold"
            title="TẠO ĐỀ XUẤT NGHỈ"
            :visible.sync="dialogFormNghi"
          >
            <el-form
              id="formNghi"
              ref="form"
              :model="form"
              :rules="rules"
              label-width="150px"
            >
              <el-input
                type="text"
                placeholder=""
                v-model="username"
                style="width: 100%"
                name="creator"
                hidden
              ></el-input>
              <el-input
                type="text"
                placeholder=""
                v-model="categoryRequestId"
                style="width: 100%"
                name="catergoryRequest"
                hidden
              ></el-input>
              <el-input
                type="text"
                placeholder=""
                v-model="form.approveStatus"
                style="width: 100%"
                name="approveStatus"
                hidden
              ></el-input>
              <el-form-item label="Họ và tên *">
                <el-col :span="16">
                  <el-input
                    type="text"
                    placeholder=""
                    v-model="fullName"
                    style="width: 100%"
                    readonly
                  ></el-input>
                </el-col>
              </el-form-item>
              <el-form-item label="Bộ phận *">
                <el-col :span="16">
                  <el-input
                    type="text"
                    v-model="departmentName"
                    style="width: 100%"
                    readonly
                  ></el-input>
                </el-col>
              </el-form-item>
              <el-form-item label="Tên đề xuất" prop="title">
                <el-col :span="16">
                  <el-input
                    placeholder="Họ và tên - Loại đề xuất - Thời gian nghỉ"
                    v-model="form.title"
                    name="title"
                  ></el-input>
                </el-col>
              </el-form-item>
              <el-form-item label="Loại đề xuất" required prop="categoryReason">
                <el-col :span="15">
                  <b-form-select
                    style="width: 107%; padding: 9px 0"
                    v-model="form.categoryReason"
                    placeholder="Chọn loại đề xuất nghỉ"
                    name="categoryReason"
                  >
                    <template #first>
                      <b-form-select-option :value="null" disabled
                        >Chọn loại đề xuất
                      </b-form-select-option>
                    </template>

                    <b-form-select-option
                      v-validate="{ required: true }"
                      v-for="(item, index) in categoryReasons"
                      :key="index"
                      :value="item.id"
                      >{{ item.name }}</b-form-select-option
                    >
                  </b-form-select>

                  <!-- <b-form-invalid-feedback id="input-2-live-feedback">{{
                    veeErrors.first("categoryReason")
                  }}</b-form-invalid-feedback> -->

                  <b-form-text v-if="form.categoryReason == 1" class="noti"
                    ><span class="noti"
                      >Số phép có thể sử dụng
                      {{ furlought.availableUsedTillMonth }}</span
                    ></b-form-text
                  >
                  <b-form-text v-if="form.categoryReason == 4" class="noti"
                    ><span class="noti"
                      >Bạn được nghỉ 3 ngày nếu kết hôn, cha mẹ/vợ hoặc
                      chồng/con mất</span
                    ></b-form-text
                  >
                  <br />
                  <b-form-text v-if="form.categoryReason == 4" class="noti"
                    ><span class="noti"
                      >Bạn được nghỉ 1 ngày nếu con ruột/con nuôi kết hôn</span
                    ></b-form-text
                  >
                  <b-form-text v-if="form.categoryReason == 3" class="noti">
                    <span class="noti"
                      >Đăng ký nghỉ ốm cần phải có giấy Bảo hiểm xã hội</span
                    >
                  </b-form-text>
                </el-col>
              </el-form-item>

              <el-form-item label="Nghỉ từ" required>
                <el-col :span="7">
                  <el-form-item prop="dateFrom">
                    <el-date-picker
                      type="date"
                      placeholder="Chọn ngày"
                      v-model="form.dateFrom"
                      style="width: 100%"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      name="dateFrom"
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col
                  v-if="form.categoryReason != 4 && form.categoryReason != 5"
                  class="line"
                  :span="2"
                  >-</el-col
                >
                <el-col :span="7">
                  <el-form-item prop="timeStart">
                    <el-time-select
                      :editable="false"
                      v-if="
                        form.categoryReason != 4 && form.categoryReason != 5
                      "
                      name="timeStart"
                      style="width: 100%"
                      v-model="form.timeStart"
                      placeholder="Chọn giờ bắt đầu"
                      :picker-options="{
                        start: '07:00',
                        step: '00:30',
                        end: '19:00',
                      }"
                      format="HH:mm"
                      value-format="HH:mm"
                    ></el-time-select>
                  </el-form-item>
                </el-col>
              </el-form-item>

              <el-form-item label="Nghỉ đến" required>
                <el-col :span="7">
                  <el-form-item prop="dateTo">
                    <el-date-picker
                      type="date"
                      placeholder="Chọn ngày"
                      v-model="form.dateTo"
                      style="width: 100%"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      name="dateTo"
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col
                  v-if="form.categoryReason != 4 && form.categoryReason != 5"
                  class="line"
                  :span="2"
                  >-</el-col
                >
                <el-col :span="7">
                  <el-form-item prop="timeEnd">
                    <el-time-select
                      :editable="false"
                      v-if="
                        form.categoryReason != 4 && form.categoryReason != 5
                      "
                      name="timeEnd"
                      style="width: 100%"
                      v-model="form.timeEnd"
                      placeholder="Chọn giờ kết thúc"
                      :picker-options="{
                        start: '07:00',
                        step: '00:30',
                        end: '19:00',
                      }"
                      format="HH:mm"
                      value-format="HH:mm"
                    ></el-time-select>
                  </el-form-item>
                </el-col>
              </el-form-item>

              <el-form-item label="Nhập nội dung" prop="content">
                <el-col :span="16">
                  <el-input
                    name="content"
                    style="width: 100%"
                    type="textarea"
                    v-model="form.content"
                  ></el-input>
                </el-col>
              </el-form-item>
              <el-form-item label="Người theo dõi" prop="followers">
                <el-col :span="16">
                  <div>
                    <b-form-tags
                      id="tags-with-dropdown"
                      no-outer-focus
                      class="mb-2"
                      v-model="form.followers"
                      name="followers"
                    >
                      <template v-slot="{ tags, disabled, addTag, removeTag }">
                        <ul
                          v-if="tags.length > 0"
                          class="list-inline d-inline-block mb-2"
                        >
                          <li
                            v-for="tag in tags"
                            :key="tag"
                            class="list-inline-item"
                          >
                            <b-form-tag
                              style="color: black; background-color: #abdbe3"
                              @remove="removeTag(tag)"
                              :title="tag"
                              :disabled="disabled"
                              variant="info"
                              >{{ tag }}
                            </b-form-tag>
                          </li>
                        </ul>
                        <b-dropdown
                          size="sm"
                          variant="outline-secondary"
                          block
                          menu-class="w-100"
                        >
                          <template #button-content>
                            <b-icon icon="tag-fill"></b-icon>
                            Gắn thẻ người theo dõi
                          </template>
                          <b-dropdown-form @submit.stop.prevent="() => {}">
                            <b-form-group
                              label="Search tags"
                              label-for="tag-search-input"
                              label-cols-md="auto"
                              class="mb-0"
                              label-size="sm"
                              :description="searchDesc"
                              :disabled="disabled"
                            >
                              <b-form-input
                                v-model="search"
                                id="tag-search-input"
                                type="search"
                                size="sm"
                                autocomplete="off"
                              ></b-form-input>
                            </b-form-group>
                          </b-dropdown-form>
                          <b-dropdown-divider></b-dropdown-divider>
                          <b-dropdown-item-button
                            v-for="option in availableOptionsNS"
                            :key="option"
                            :value="option.username"
                            @click="onOptionClick({ option, addTag })"
                          >
                            {{ option.fullName }}
                          </b-dropdown-item-button>
                          <b-dropdown-text v-if="availableOptions.length === 0">
                            Không có người nào được chọn
                          </b-dropdown-text>
                        </b-dropdown>
                      </template>
                    </b-form-tags>
                  </div>
                </el-col>
              </el-form-item>
              <el-form-item label="Người phê duyệt" prop="approvers">
                <el-col :span="16">
                  <div>
                    <b-form-tags
                      id="tags-with-dropdown"
                      no-outer-focus
                      class="mb-2"
                      v-model="form.approvers"
                      name="approvers"
                    >
                      <template v-slot="{ tags, disabled, addTag, removeTag }">
                        <ul
                          v-if="tags.length > 0"
                          class="list-inline d-inline-block mb-2"
                        >
                          <li
                            v-for="tag in tags"
                            :key="tag"
                            class="list-inline-item"
                          >
                            <b-form-tag
                              style="color: black; background-color: #abdbe3"
                              @remove="removeTag(tag)"
                              :title="tag"
                              :disabled="disabled"
                              variant="info"
                              >{{ tag }}
                            </b-form-tag>
                          </li>
                        </ul>
                        <b-dropdown
                          size="sm"
                          variant="outline-secondary"
                          block
                          menu-class="w-100"
                        >
                          <template #button-content>
                            <b-icon icon="tag-fill"></b-icon>
                            Gắn thẻ người phê duyệt
                          </template>
                          <b-dropdown-form @submit.stop.prevent="() => {}">
                            <b-form-group
                              label="Search tags"
                              label-for="tag-search-input"
                              label-cols-md="auto"
                              class="mb-0"
                              label-size="sm"
                              :description="searchDesc"
                              :disabled="disabled"
                            >
                              <b-form-input
                                v-model="search"
                                id="tag-search-input"
                                type="search"
                                size="sm"
                                autocomplete="off"
                              ></b-form-input>
                            </b-form-group>
                          </b-dropdown-form>
                          <b-dropdown-divider></b-dropdown-divider>
                          <b-dropdown-item-button
                            v-for="option in availableOptionsMN"
                            :key="option"
                            :value="option.username"
                            @click="onOptionClick({ option, addTag })"
                          >
                            {{ option.fullName }}
                          </b-dropdown-item-button>
                          <b-dropdown-text v-if="availableOptions.length === 0">
                            Không có người nào được chọn
                          </b-dropdown-text>
                        </b-dropdown>
                      </template>
                    </b-form-tags>
                  </div>
                </el-col>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button @click="resetForm('form')">Hủy</el-button>
              <el-button type="danger" @click="sendFormNghi"
                >Gửi đề xuất</el-button
              >
            </span>
          </el-dialog>

          <el-dialog
            style="text-align: center; font-weight: bold"
            title="TẠO ĐỀ XUẤT CHẤM CÔNG"
            :visible.sync="dialogFormChamCong"
          >
            <el-form
              id="formChamCong"
              ref="form"
              :model="form"
              :rules="rules"
              label-width="150px"
            >
              <el-input
                type="text"
                placeholder=""
                v-model="username"
                style="width: 100%"
                name="creator"
                hidden
              ></el-input>
              <el-input
                type="text"
                placeholder=""
                v-model="categoryRequestId"
                style="width: 100%"
                name="catergoryRequest"
                hidden
              ></el-input>
              <el-input
                type="text"
                placeholder=""
                v-model="form.approveStatus"
                style="width: 100%"
                name="approveStatus"
                hidden
              ></el-input>
              <el-form-item label="Họ và tên *">
                <el-col :span="16">
                  <el-input
                    type="text"
                    placeholder=""
                    v-model="fullName"
                    style="width: 100%"
                    readonly
                  ></el-input>
                </el-col>
              </el-form-item>
              <el-form-item label="Bộ phận *">
                <el-col :span="16">
                  <el-input
                    type="text"
                    v-model="departmentName"
                    style="width: 100%"
                    readonly
                  ></el-input>
                </el-col>
              </el-form-item>
              <el-form-item label="Tên đề xuất" prop="title">
                <el-col :span="16">
                  <el-input
                    placeholder="Họ và tên - Loại đề xuất - Thời gian nghỉ"
                    v-model="form.title"
                    name="title"
                  ></el-input>
                </el-col>
              </el-form-item>
              <el-form-item required label="Loại đề xuất">
                <el-col :span="15">
                  <b-form-select
                    style="width: 107%; padding: 9px 0"
                    v-model="form.categoryReason"
                    placeholder="Chọn loại đề xuất nghỉ"
                    name="categoryReason"
                  >
                    <template #first>
                      <b-form-select-option :value="null" disabled
                        >Chọn loại đề xuất
                      </b-form-select-option>
                    </template>
                    <b-form-select-option
                      v-for="(item, index) in categoryReasons"
                      :key="index"
                      :value="item.id"
                      >{{ item.name }}
                    </b-form-select-option>
                  </b-form-select>
                </el-col>
              </el-form-item>

              <el-form-item
                label="Nghỉ từ"
                required
                v-if="form.categoryReason != 6"
              >
                <el-col :span="7">
                  <el-form-item prop="dateFrom">
                    <el-date-picker
                      v-if="form.categoryReason != 6"
                      type="date"
                      placeholder="Chọn ngày"
                      v-model="form.dateFrom"
                      style="width: 100%"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      name="dateFrom"
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col class="line" :span="2" v-if="form.categoryReason != 6"
                  >-</el-col
                >
                <el-col :span="7">
                  <el-form-item prop="timeStart">
                    <el-time-select
                      :editable="false"
                      v-if="form.categoryReason != 6"
                      name="timeStart"
                      style="width: 100%"
                      v-model="form.timeStart"
                      placeholder="Chọn giờ bắt đầu"
                      :picker-options="{
                        start: '07:00',
                        step: '00:30',
                        end: '19:00',
                      }"
                      format="HH:mm"
                      value-format="HH:mm"
                    ></el-time-select>
                  </el-form-item>
                </el-col>
              </el-form-item>

              <el-form-item
                label="Quên ngày"
                required
                v-if="form.categoryReason == 6"
              >
                <el-col :span="16">
                  <el-date-picker
                    name="dateForget"
                    type="date"
                    placeholder="Chọn ngày"
                    v-model="form.dateForget"
                    style="width: 100%"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                  ></el-date-picker>
                </el-col>
              </el-form-item>

              <el-form-item
                label="Nghỉ đến"
                required
                v-if="form.categoryReason != 6"
              >
                <el-col :span="7">
                  <el-form-item prop="dateTo">
                    <el-date-picker
                      v-if="form.categoryReason != 6"
                      type="date"
                      placeholder="Chọn ngày"
                      v-model="form.dateTo"
                      style="width: 100%"
                      format="yyyy-MM-dd"
                      value-format="yyyy-MM-dd"
                      name="dateTo"
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col class="line" :span="2" v-if="form.categoryReason != 6"
                  >-</el-col
                >
                <el-col :span="7">
                  <el-form-item prop="timeEnd">
                    <el-time-select
                      :editable="false"
                      v-if="form.categoryReason != 6"
                      name="timeEnd"
                      style="width: 100%"
                      v-model="form.timeEnd"
                      placeholder="Chọn giờ kết thúc"
                      :picker-options="{
                        start: '07:00',
                        step: '00:30',
                        end: '19:00',
                      }"
                      format="HH:mm"
                      value-format="HH:mm"
                    ></el-time-select>
                  </el-form-item>
                </el-col>
              </el-form-item>

              <el-form-item label="Nhập nội dung" prop="content">
                <el-col :span="16">
                  <el-input
                    name="content"
                    style="width: 100%"
                    type="textarea"
                    v-model="form.content"
                  ></el-input>
                </el-col>
              </el-form-item>
              <el-form-item label="Người theo dõi" prop="followers">
                <el-col :span="16">
                  <div>
                    <b-form-tags
                      id="tags-with-dropdown"
                      no-outer-focus
                      class="mb-2"
                      v-model="form.followers"
                      name="followers"
                    >
                      <template v-slot="{ tags, disabled, addTag, removeTag }">
                        <ul
                          v-if="tags.length > 0"
                          class="list-inline d-inline-block mb-2"
                        >
                          <li
                            v-for="tag in tags"
                            :key="tag"
                            class="list-inline-item"
                          >
                            <b-form-tag
                              style="color: black; background-color: #abdbe3"
                              @remove="removeTag(tag)"
                              :title="tag"
                              :disabled="disabled"
                              variant="info"
                              >{{ tag }}
                            </b-form-tag>
                          </li>
                        </ul>
                        <b-dropdown
                          size="sm"
                          variant="outline-secondary"
                          block
                          menu-class="w-100"
                        >
                          <template #button-content>
                            <b-icon icon="tag-fill"></b-icon>
                            Gắn thẻ người theo dõi
                          </template>
                          <b-dropdown-form @submit.stop.prevent="() => {}">
                            <b-form-group
                              label="Search tags"
                              label-for="tag-search-input"
                              label-cols-md="auto"
                              class="mb-0"
                              label-size="sm"
                              :description="searchDesc"
                              :disabled="disabled"
                            >
                              <b-form-input
                                v-model="search"
                                id="tag-search-input"
                                type="search"
                                size="sm"
                                autocomplete="off"
                              ></b-form-input>
                            </b-form-group>
                          </b-dropdown-form>
                          <b-dropdown-divider></b-dropdown-divider>
                          <b-dropdown-item-button
                            v-for="option in availableOptionsNS"
                            :key="option"
                            :value="option.username"
                            @click="onOptionClick({ option, addTag })"
                          >
                            {{ option.fullName }}
                          </b-dropdown-item-button>
                          <b-dropdown-text v-if="availableOptions.length === 0">
                            Không có người nào được chọn
                          </b-dropdown-text>
                        </b-dropdown>
                      </template>
                    </b-form-tags>
                  </div>
                </el-col>
              </el-form-item>
              <el-form-item label="Người phê duyệt" prop="approvers">
                <el-col :span="16">
                  <div>
                    <b-form-tags
                      id="tags-with-dropdown"
                      no-outer-focus
                      class="mb-2"
                      v-model="form.approvers"
                      name="approvers"
                    >
                      <template v-slot="{ tags, disabled, addTag, removeTag }">
                        <ul
                          v-if="tags.length > 0"
                          class="list-inline d-inline-block mb-2"
                        >
                          <li
                            v-for="tag in tags"
                            :key="tag"
                            class="list-inline-item"
                          >
                            <b-form-tag
                              style="color: black; background-color: #abdbe3"
                              @remove="removeTag(tag)"
                              :title="tag"
                              :disabled="disabled"
                              variant="info"
                              >{{ tag }}
                            </b-form-tag>
                          </li>
                        </ul>
                        <b-dropdown
                          size="sm"
                          variant="outline-secondary"
                          block
                          menu-class="w-100"
                        >
                          <template #button-content>
                            <b-icon icon="tag-fill"></b-icon>
                            Gắn thẻ người phê duyệt
                          </template>
                          <b-dropdown-form @submit.stop.prevent="() => {}">
                            <b-form-group
                              label="Search tags"
                              label-for="tag-search-input"
                              label-cols-md="auto"
                              class="mb-0"
                              label-size="sm"
                              :description="searchDesc"
                              :disabled="disabled"
                            >
                              <b-form-input
                                v-model="search"
                                id="tag-search-input"
                                type="search"
                                size="sm"
                                autocomplete="off"
                              ></b-form-input>
                            </b-form-group>
                          </b-dropdown-form>
                          <b-dropdown-divider></b-dropdown-divider>
                          <b-dropdown-item-button
                            v-for="option in availableOptionsMN"
                            :key="option"
                            :value="option.username"
                            @click="onOptionClick({ option, addTag })"
                          >
                            {{ option.fullName }}
                          </b-dropdown-item-button>
                          <b-dropdown-text v-if="availableOptions.length === 0">
                            Không có người nào được chọn
                          </b-dropdown-text>
                        </b-dropdown>
                      </template>
                    </b-form-tags>
                  </div>
                </el-col>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button @click="resetForm('form')">Hủy</el-button>
              <el-button type="danger" @click="sendFormChamCong"
                >Gửi đề xuất</el-button
              >
            </span>
          </el-dialog>
          <br />
          <el-table :data="myRequests" height="700" style="width: 100%">
            <el-table-column v-slot:="data" label="Đề xuất" align="center">
              <router-link
                class="link"
                :to="{ name: 'requestdetail', params: { id: data.row.id } }"
                >{{ data.row.title }}
              </router-link>
            </el-table-column>
            <el-table-column
              prop="creator.fullName"
              label="Nhân viên"
              align="center"
              width="300"
            >
            </el-table-column>
            <el-table-column
              prop="creator.departments.name"
              label="Phòng ban"
              align="center"
              width="300"
            >
            </el-table-column>
            <el-table-column
              v-slot:="data"
              label="Trạng thái"
              align="center"
              width="200"
            >
              <button v-if="data.row.approveStatus.id == 1" class="btn-1">
                {{ data.row.approveStatus.name }}
              </button>
              <button v-if="data.row.approveStatus.id == 2" class="btn-2">
                {{ data.row.approveStatus.name }}
              </button>
              <button v-if="data.row.approveStatus.id == 3" class="btn-3">
                {{ data.row.approveStatus.name }}
              </button>
              <button v-if="data.row.approveStatus.id == 4" class="btn-4">
                {{ data.row.approveStatus.name }}
              </button>
              <button v-if="data.row.approveStatus.id == 5" class="btn-3">
                {{ data.row.approveStatus.name }}
              </button>
              <button v-if="data.row.approveStatus.id == 6" class="btn-2">
                {{ data.row.approveStatus.name }}
              </button>
            </el-table-column>
            <el-table-column
              v-slot:="data"
              label="Người duyệt"
              align="center"
              width="200"
            >
              <div
                v-for="(item, index) in data.row.approvers"
                :item="item"
                :index="index"
                :key="item.id"
              >
                <span>{{ item.fullName }}</span>
              </div>
            </el-table-column>
            <el-table-column
              v-slot:="data"
              label="Người theo dõi"
              align="center"
              width="200"
            >
              <div
                v-for="(item, index) in data.row.followers"
                :item="item"
                :index="index"
                :key="item.id"
              >
                <span>{{ item.fullName }}</span>
              </div>
            </el-table-column>

            <el-table-column
              prop=""
              label="Thao tác"
              align="center"
              width="200"
              v-slot:="data"
            >
              <el-button
                type="info"
                v-if="data.row.approveStatus.id == 1"
                @click="
                  changeStatus(
                    data.row.id,
                    4,
                    data.row.approveStatus.id,
                    currentUser.user.id
                  )
                "
                icon="el-icon-delete"
                circle
              ></el-button>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Tất cả" name="second">
        <br />
        <div className="container" style="text-align: center">
          <el-row :gutter="20">
            <el-col :md="6" :lg="6" :xl="6">
              <div class="grid-content" style="margin-bottom: 20px">
                <span>Phòng ban</span> &ensp;
                <el-select
                  v-model="departmentId"
                  placeholder="Chọn Phòng ban"
                  @change="getAll"
                >
                  <el-option value="0" label="Tất cả các phòng ban"></el-option>
                  <el-option
                    v-for="item in departments"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </div>
            </el-col>

            <el-col :md="6" :lg="6" :xl="6" style="margin-bottom: 20px">
              <div class="grid-content">
                <span style="">Tìm kiếm</span> &ensp;
                <el-input
                  v-model="search"
                  @input="getAll"
                  size="medium"
                  placeholder="Tìm theo tên, email"
                  style="width: 200px; padding: 2px 0"
                />
              </div>
            </el-col>
            <el-col :md="6" :lg="6" :xl="6" style="margin-bottom: 20px">
              <div class="grid-content">
                <span style="">Trạng thái</span> &ensp;
                <el-select
                  v-model="status"
                  @change="getAll"
                  placeholder="Trạng thái"
                >
                  <el-option value="0" label="Tất cả"></el-option>
                  <el-option label="Chờ phê duyệt" value="1"></el-option>
                  <el-option label="Đã chấp thuận" value="2"></el-option>
                  <el-option label="Đã từ chối" value="3"></el-option>
                  <el-option label="Đã hủy" value="4"></el-option>
                </el-select>
              </div>
            </el-col>

            <el-col :md="6" :lg="6" :xl="6" class="div-buttons">
              <div class="grid-content div-buttons"></div>
            </el-col>
          </el-row>
        </div>

        <el-table :data="requests" height="700" style="width: 100%">
          <el-table-column v-slot:="data" label="Đề xuất" align="center">
            <router-link
              class="link"
              :to="{ name: 'requestdetail', params: { id: data.row.id } }"
              >{{ data.row.title }}
            </router-link>
          </el-table-column>
          <el-table-column
            prop="creator.fullName"
            label="Nhân viên"
            align="center"
            width="300"
          >
          </el-table-column>
          <el-table-column
            prop="creator.departments.name"
            label="Phòng ban"
            align="center"
            width="300"
          >
          </el-table-column>
          <el-table-column
            v-slot:="data"
            label="Trạng thái"
            align="center"
            width="200"
          >
            <button v-if="data.row.approveStatus.id == 1" class="btn-1">
              {{ data.row.approveStatus.name }}
            </button>
            <button v-if="data.row.approveStatus.id == 2" class="btn-2">
              {{ data.row.approveStatus.name }}
            </button>
            <button v-if="data.row.approveStatus.id == 3" class="btn-3">
              {{ data.row.approveStatus.name }}
            </button>
            <button v-if="data.row.approveStatus.id == 4" class="btn-4">
              {{ data.row.approveStatus.name }}
            </button>
            <button v-if="data.row.approveStatus.id == 5" class="btn-3">
              {{ data.row.approveStatus.name }}
            </button>
            <button v-if="data.row.approveStatus.id == 6" class="btn-2">
              {{ data.row.approveStatus.name }}
            </button>
          </el-table-column>
          <el-table-column
            v-slot:="data"
            label="Người duyệt"
            align="center"
            width="200"
          >
            <div
              v-for="(item, index) in data.row.approvers"
              :item="item"
              :index="index"
              :key="item.id"
            >
              <span>{{ item.fullName }}</span>
            </div>
          </el-table-column>
          <el-table-column
            v-slot:="data"
            label="Người theo dõi"
            align="center"
            width="200"
          >
            <div
              v-for="(item, index) in data.row.followers"
              :item="item"
              :index="index"
              :key="item.id"
            >
              <span>{{ item.fullName }}</span>
            </div>
          </el-table-column>
          <!--          <el-table-column-->
          <!--            prop="date"-->
          <!--            label="Ngày tạo"-->
          <!--            align="center"-->
          <!--            width="200"-->
          <!--          >-->
          <!--          </el-table-column>-->
          <el-table-column
            prop=""
            label="Thao tác"
            align="center"
            width="200"
            v-slot:="data"
          >
            <div v-for="(item, index) in data.row.approvers" :key="index">
              <el-button
                type="success"
                v-if="
                  data.row.approveStatus.id == 1 &&
                  item.id == currentUser.user.id
                "
                @click="
                  changeStatus(
                    data.row.id,
                    2,
                    data.row.approveStatus.id,
                    currentUser.user.id
                  )
                "
                icon="el-icon-check"
                circle
              ></el-button>
              <el-button
                type="danger"
                v-if="
                  data.row.approveStatus.id == 1 &&
                  item.id == currentUser.user.id
                "
                @click="
                  changeStatus(
                    data.row.id,
                    3,
                    data.row.approveStatus.id,
                    currentUser.user.id
                  )
                "
                icon="el-icon-close"
                circle
              ></el-button>
              <el-button
                type="warning"
                v-if="
                  (data.row.approveStatus.id == 2 ||
                    data.row.approveStatus.id == 3) &&
                  item.id == currentUser.user.id
                "
                @click="
                  changeStatus(
                    data.row.id,
                    1,
                    data.row.approveStatus.id,
                    currentUser.user.id
                  )
                "
                icon="el-icon-refresh-left"
                circle
              ></el-button>
            </div>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import RequestService from "@/services/request-service";
import UserService from "@/services/user.service";
import DepartmentService from "@/services/department.service";

export default {
  data() {
    return {
      furlought: "",
      options: [],
      value: [],
      list: [],
      users: [],
      usersNS: [],
      activeName: "first",
      dialogFormNghi: false,
      dialogFormChamCong: false,
      fullName: "",
      username: "",
      departmentName: "",
      categoryRequestId: "",
      categoryReasons: [],
      approvestatus: "1",
      requests: [],
      myRequests: [],
      departments: [],
      departmentId: "",
      status: "",
      sendDepartmentId: 0,
      sendStatus: 0,
      search: "",
      totalItems: 0,
      page: 0,
      pageSize: 10,
      form: {
        title: "",
        creator: "",
        approvers: [],
        followers: [],
        content: "",
        approveStatus: 1,
        catergoryRequest: null,
        categoryReason: null,
        dateFrom: "",
        dateTo: "",
        dateForget: "",
        timeStart: "",
        timeEnd: "",
      },

      rules: {
        title: [
          {
            required: true,
            message: "Vui lòng nhập tên đề xuất",
            trigger: "blur",
          },
        ],
        dateFrom: [
          {
            required: true,
            message: "Vui lòng chọn ngày bắt đầu",
            trigger: "blur",
          },
        ],
        dateTo: [
          {
            required: true,
            message: "Vui lòng chọn ngày kết thúc",
            trigger: "blur",
          },
        ],
        timeStart: [
          {
            required: true,
            message: "Vui lòng chọn giờ bắt đầu",
            trigger: "blur",
          },
        ],
        timeEnd: [
          {
            required: true,
            message: "Vui lòng chọn giờ kết thúc",
            trigger: "blur",
          },
        ],
        content: [
          {
            required: true,
            message: "Vui lòng nhập nội dung",
            trigger: "blur",
          },
        ],
        followers: [
          {
            required: true,
            message: "Vui lòng chọn người theo dõi",
            trigger: "blur",
          },
        ],
        approvers: [
          {
            required: true,
            message: "Vui lòng chọn người duyệt",
            trigger: "blur",
          },
        ],
      },
      loading: false,
    };
  },

  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
    currentUser() {
      return this.$store.state.auth.user;
    },
    criteria() {
      // Compute the search criteria
      return this.search.trim().toLowerCase();
    },
    availableOptions() {
      const criteria = this.criteria;
      // Filter out already selected options
      const options = this.users.filter(
        (opt) => this.value.indexOf(opt) === -1
      );
      if (criteria) {
        // Show only options that match criteria
        return options.filter(
          (opt) => opt.fullName.toLowerCase().indexOf(criteria) > -1
        );
      }
      // Show all options available
      return options;
    },
    availableOptionsNS() {
      const criteria = this.criteria;
      // Filter out already selected options
      const options = this.usersNS.filter(
        (opt) => this.value.indexOf(opt) === -1
      );
      if (criteria) {
        // Show only options that match criteria
        return options.filter(
          (opt) => opt.fullName.toLowerCase().indexOf(criteria) > -1
        );
      }
      // Show all options available
      return options;
    },
    availableOptionsMN() {
      const criteria = this.criteria;
      // Filter out already selected options
      const options = this.usersMN.filter(
        (opt) => this.value.indexOf(opt) === -1
      );
      if (criteria) {
        // Show only options that match criteria
        return options.filter(
          (opt) => opt.fullName.toLowerCase().indexOf(criteria) > -1
        );
      }
      // Show all options available
      return options;
    },
    searchDesc() {
      if (this.criteria && this.availableOptions.length === 0) {
        return "Không có tên người bạn muốn tìm";
      }
      return "";
    },
  },
  created() {
    this.getParams();
    this.getAll();
    UserService.getAllUser()

      .then((response) => {
        this.users = response.data;
        this.list = this.users.map((item) => {
          return { username: `${item.username}`, fullName: `${item.fullName}` };
        });
      })
      .catch((e) => {
        console.log(e);
      });
    UserService.getAllUserByDepartmentId(2)

      .then((response) => {
        this.usersNS = response.data;
        this.list = this.usersNS.map((item) => {
          return { username: `${item.username}`, fullName: `${item.fullName}` };
        });
      })
      .catch((e) => {
        console.log(e);
      });
    UserService.getAllUserByPositionId()
      .then((response) => {
        this.usersMN = response.data;
        this.list = this.usersMN.map((item) => {
          return { username: `${item.username}`, fullName: `${item.fullName}` };
        });
      })
      .catch((e) => {
        console.log(e);
      });
    DepartmentService.getAllDepartment().then((response) => {
      this.departments = response.data;
    });
  },

  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogFormNghi = false;
      this.dialogFormChamCong = false;
    },
    onOptionClick({ option, addTag }) {
      addTag(option.username);
      this.search = "";
    },
    getParams() {
      this.fullName = this.currentUser.user.fullName;
      this.departmentName = this.currentUser.user.departments.name;
      this.username = this.currentUser.user.username;
    },
    changeStatus(requestId, newStatusId, oldStatusId, approvedId) {
      if (newStatusId == 1) {
        this.$swal
          .fire({
            title: "Xác nhận hoàn tác",
            showDenyButton: true,
            icon: "question",
            confirmButtonColor: "#75C4C0",
            confirmButtonText: "Xác nhận",
            denyButtonColor: "#ED9696",
            denyButtonText: "Đóng",
            customClass: {
              actions: "my-actions",
              cancelButton: "order-1 right-gap",
              confirmButton: "order-2",
              denyButton: "order-3",
            },
          })
          .then((result) => {
            if (result.isConfirmed) {
              RequestService.changeStatus(
                requestId,
                newStatusId,
                oldStatusId,
                approvedId
              ).then((response) => {
                this.$swal
                  .fire({
                    title: response.data.message,
                    icon: "success",
                    timer: 2000,
                    timerProgressBar: true,
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    width: "24em",
                  })
                  .catch((error) => {
                    this.$swal.fire({
                      title: "Lỗi",
                      text: error.response.data.error.message,
                      icon: "error",
                      timer: 2000,
                      timerProgressBar: true,
                    });
                    this.message = "";
                  });
                this.getAll();
              });
            } else if (result.isDenied) {
              this.$swal.fire({
                title: "Thay đổi thất bại",
                icon: "error",
                timer: 2000,
                timerProgressBar: true,
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                width: "24em",
              });
            }
          });
      }
      if (newStatusId == 2) {
        this.$swal
          .fire({
            title: "Xác nhận chấp thuận",
            showDenyButton: true,
            icon: "question",
            confirmButtonColor: "#75C4C0",
            confirmButtonText: "Xác nhận",
            denyButtonColor: "#ED9696",
            denyButtonText: "Đóng",
            customClass: {
              actions: "my-actions",
              cancelButton: "order-1 right-gap",
              confirmButton: "order-2",
              denyButton: "order-3",
            },
          })
          .then((result) => {
            if (result.isConfirmed) {
              RequestService.changeStatus(
                requestId,
                newStatusId,
                oldStatusId,
                approvedId
              )
                .then((response) => {
                  this.$swal.fire({
                    title: response.data.message,
                    icon: "success",
                    timer: 2000,
                    timerProgressBar: true,
                    toast: true,
                    position: "top-end",
                    showConfirmButton: false,
                    width: "24em",
                  });
                  this.getAll();
                })
                .catch((error) => {
                  this.$swal.fire({
                    title: "Lỗi",
                    text: error.response.data.error.message,
                    icon: "error",
                    timer: 2000,
                    timerProgressBar: true,
                  });
                  this.message = "";
                });
            } else if (result.isDenied) {
              this.$swal.fire({
                title: "Thay đổi thất bại",
                icon: "error",
                timer: 2000,
                timerProgressBar: true,
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                width: "24em",
              });
            }
          });
      }
      if (newStatusId == 3) {
        this.$swal
          .fire({
            title: "Xác nhận từ chối",
            showDenyButton: true,
            icon: "question",
            confirmButtonColor: "#75C4C0",
            confirmButtonText: "Xác nhận",
            denyButtonColor: "#ED9696",
            denyButtonText: "Đóng",
            customClass: {
              actions: "my-actions",
              cancelButton: "order-1 right-gap",
              confirmButton: "order-2",
              denyButton: "order-3",
            },
          })
          .then((result) => {
            if (result.isConfirmed) {
              RequestService.changeStatus(
                requestId,
                newStatusId,
                oldStatusId,
                approvedId
              ).then((response) => {
                this.$swal.fire({
                  title: response.data.message,
                  icon: "success",
                  timer: 2000,
                  timerProgressBar: true,
                  toast: true,
                  position: "top-end",
                  showConfirmButton: false,
                  width: "24em",
                });
                this.getAll();
              });
            } else if (result.isDenied) {
              this.$swal.fire({
                title: "Thay đổi thất bại",
                icon: "error",
                timer: 2000,
                timerProgressBar: true,
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                width: "24em",
              });
            }
          });
      }
      if (newStatusId == 4) {
        this.$swal
          .fire({
            title: "Xác nhận hủy yêu cầu",
            showDenyButton: true,
            icon: "question",
            confirmButtonColor: "#75C4C0",
            confirmButtonText: "Xác nhận",
            denyButtonColor: "#ED9696",
            denyButtonText: "Đóng",
            customClass: {
              actions: "my-actions",
              cancelButton: "order-1 right-gap",
              confirmButton: "order-2",
              denyButton: "order-3",
            },
          })
          .then((result) => {
            if (result.isConfirmed) {
              RequestService.changeStatus(
                requestId,
                newStatusId,
                oldStatusId,
                approvedId
              ).then((response) => {
                this.$swal.fire({
                  title: response.data.message,
                  icon: "success",
                  timer: 2000,
                  timerProgressBar: true,
                  toast: true,
                  position: "top-end",
                  showConfirmButton: false,
                  width: "24em",
                });
                this.getAll();
              });
            } else if (result.isDenied) {
              this.$swal.fire({
                title: "Thay đổi thất bại",
                icon: "error",
                timer: 2000,
                timerProgressBar: true,
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                width: "24em",
              });
            }
          });
      }
    },

    async sendFormNghi() {
      if (
        this.form.title == "" ||
        this.form.categoryReason == null ||
        // this.form.dateFrom == "" ||
        // this.form.dateTo == "" ||
        // this.form.timeStart == "" ||
        // this.form.timeEnd == "" ||
        this.form.content == "" ||
        this.form.approvers == "" ||
        this.form.followers == ""
      ) {
        this.$notify.warning({
          message: "Bạn hãy nhập đủ các trường trường thông tin",
          title: "Thất bại",
          timer: 2000,
          timerProgressBar: true,
        });
      } else {
        this.loading = true;
        this.creator = this.currentUser.user.username;
        this.catergoryRequest = this.categoryRequestId;
        this.approveStatus = 1;
        this.dialogFormNghi = false;
        let form = document.querySelector("#formNghi");
        console.log(this.form);
        RequestService.addRequest(form)
          .then(() => {
            this.$notify.success({
              message: "Tạo đề xuất thành công",
              title: "Thành Công",
              timer: 2000,
              timerProgressBar: true,
            });

            this.resetForm("form");
            this.loading = false;
            this.getAll();
          })
          .catch((e) => {
            this.$notify.error({
              message: e.response.data.error.message,
              title: "Lỗi",
              timer: 2000,
              timerProgressBar: true,
            });
            this.loading = false;
          });
      }
    },

    async sendFormChamCong() {
      if (
        this.form.title == "" ||
        this.form.categoryReason == null ||
        // this.form.dateFrom == "" ||
        // this.form.dateTo == "" ||
        // this.form.timeStart == "" ||
        // this.form.timeEnd == "" ||
        this.form.content == "" ||
        this.form.approvers == "" ||
        this.form.followers == ""
      ) {
        this.$notify.warning({
          message: "Bạn hãy nhập đủ các trường trường thông tin",
          title: "Thất bại",
          timer: 2000,
          timerProgressBar: true,
        });
      } else {
        this.loading = true;
        this.creator = this.currentUser.user.username;
        this.catergoryRequest = this.categoryRequestId;
        this.approveStatus = 1;
        this.dialogFormChamCong = false;
        let form = document.querySelector("#formChamCong");
        console.log(this.form);
        RequestService.addRequest(form)
          .then(() => {
            this.$notify.success({
              message: "Tạo đề xuất thành công",
              title: "Thành Công",
              timer: 2000,
              timerProgressBar: true,
            });
            this.resetForm("form");
            this.loading = false;
            this.getAll();
          })
          .catch((e) => {
            this.$notify.error({
              message: e.response.data.error.message,
              title: "Lỗi",
              timer: 2000,
              timerProgressBar: true,
            });
            this.loading = false;
          });
      }
    },
    openFormNghi() {
      this.dialogFormNghi = true;
      this.categoryRequestId = 1;
      RequestService.getCategoryReason(this.categoryRequestId).then(
        (response) => {
          this.categoryReasons = response.data;
          console.log(this.categoryReasons);
        }
      );
    },
    openFormChamCong() {
      this.dialogFormChamCong = true;
      this.categoryRequestId = 2;
      RequestService.getCategoryReason(this.categoryRequestId).then(
        (response) => {
          this.categoryReasons = response.data;
        }
      );
    },
    getAll() {
      let params = null;
      if (this.departmentId == "" || this.departmentId == null) {
        this.sendDepartmentId = 0;
      } else {
        this.sendDepartmentId = this.departmentId;
      }
      if (this.status == "" || this.status == null) {
        this.sendStatus = 0;
      } else {
        this.sendStatus = this.status;
      }
      params = {
        user_id: this.currentUser.user.id,
        depart_id: this.sendDepartmentId,
        search: this.search,
        status: this.sendStatus,
      };
      RequestService.getAll(params)
        .then((response) => {
          this.requests = response.data;
          console.log("haha" + this.requests);
        })
        .catch((error) => {
          console.log(error);
        });
      // get My requests
      if (this.statusId == "" || this.statusId == null) {
        this.sendStatusId = 0;
      } else {
        this.sendStatusId = this.statusId;
      }
      RequestService.getMyRequest(
        this.currentUser.user.id,
        this.sendStatusId
      ).then((response) => {
        this.myRequests = response.data;
        console.log(this.myRequests);
      });
      RequestService.getFurloughinMonth(this.currentUser.user.id).then(
        (response) => {
          this.furlought = response.data;
        }
      );
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    onSubmit() {
      console.log("submit!");
    },
  },
};
</script>

<style>
.link {
  text-decoration: none;
  color: #06bdb3;
  font-weight: 600;
}

.btn-1 {
  cursor: none;
  color: #df8620;
  background-color: #faecdb;
  border: none;
  border-radius: 20px;
  padding: 3px 20px;
}

.btn-2 {
  cursor: none;
  color: #2bbb6e;
  background-color: #def7ea;
  border: none;
  border-radius: 20px;
  padding: 3px 20px;
}

.btn-3 {
  cursor: none;
  color: #bf2c31;
  background-color: #f7dedf;
  border: none;
  border-radius: 20px;
  padding: 3px 20px;
}

.btn-4 {
  cursor: none;
  color: #6c6f93;
  background-color: #e4e5f1;
  border: none;
  border-radius: 20px;
  padding: 3px 20px;
}

.b-form-tag > button.b-form-tag-remove {
  color: white;
  font-size: 125%;
  line-height: 1;
  float: none;
  margin-left: 0.25rem;
  border-radius: 5px;
  border: none;
  background-color: black;
  font-weight: 600;
}

.btn-submit {
  background-color: #f56c6c;
}

.noti {
  color: red;
}
</style>
