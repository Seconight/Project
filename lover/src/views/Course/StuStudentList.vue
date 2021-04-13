<template>
  <div class="body">
    <van-nav-bar
      title="学生名单"
      left-text="返回"
      right-text="课程信息"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
      safe-area-inset-top
    />
    <div class="studentList">
      <van-cell-group>
        <template #title>
          <div @click="showOrderPicker = true">
            <span>学生({{ students.length }}人)</span>
            <span style="float: right">
              {{ orderName }}
            </span>
          </div>
        </template>

        <van-cell
          v-for="(student, index) in students"
          :key="student.id"
          :title="student.name"
          :value="student.rate"
          @click="onClickStudent(index)"
          clickable
        />
      </van-cell-group>
    </div>

    <van-popup v-model="showOrderPicker" position="bottom">
      <van-picker
        show-toolbar
        :columns="orderColumns"
        @confirm="onOrderConfirm"
        @cancel="showOrderPicker = false"
      />
    </van-popup>
    <van-action-sheet v-model="showCourseInfo" title="课程信息" position="top">
      <van-cell-group>
        <van-cell title="课程号" :value="course.id" />
        <van-cell title="课程名" :value="course.name" />
        <van-cell title="任课老师" :value="course.teachername" />
        <van-cell title="上课时间" :value="course.time" />
        <van-cell title="周次" :value="course.week" />
        <van-cell title="学期" :value="course.semester" />
      </van-cell-group>
    </van-action-sheet>
    <van-action-sheet
      v-model="showStudentInfo"
      title="学生信息"
      position="bottom"
    >
      <van-cell-group v-if="students.length != 0">
        <van-cell title="姓名" :value="students[choice].name" />
        <van-cell title="班级" :value="students[choice].class" />
        <van-cell title="学号" :value="students[choice].id" />
        <van-cell title="出勤次数" :value="students[choice].times" />
        <van-cell title="出勤率" :value="students[choice].rate" />
      </van-cell-group>
    </van-action-sheet>
  </div>
</template>

<script>
export default {
  props: ["course"],
  data() {
    return {
      showCourseInfo: false,
      showPopover: false,
      showOrderPicker: false,
      showStudentInfo: false,
      orderName: "出勤率",
      orderColumns: ["姓名", "学号", "出勤率从大到小", "出勤率从小到大"],
      students: [],
      choice: 0,
    };
  },
  created() {
    let courseID = this.course.id;
    var axios = require("axios");
    var config = {
      method: "get",
      url: this.GLOBAL.port + "/teacher/getCourseStudent?id=" + courseID,
      headers: {},
    };
    let _this = this;
    axios(config)
      .then(function (response) {
        if (response.data.code == 0) {
          _this.$toast.fail("获取学生名单失败！");
        } else {
          _this.students = response.data.data;
          //出勤率保留2位小数
          for (let i = 0; i < _this.students.length; i++) {
            _this.students[i].rate =
              parseInt(_this.students[i].rate.toFixed(2) * 100) + "%";
          }
        }
      })
      .catch(function (error) {
        console.log(error);
      });
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    onClickRight() {
      this.showCourseInfo = true;
    },

    compareById(id) {
      return function (a, b) {
        return a[id] - b[id];
      };
    },

    compareByName(name) {
      return function (a, b) {
        return a[name] - b[name];
      };
    },

    compareByRateUpToDown(rate) {
      return function (a, b) {
        return b[rate] - a[rate];
      };
    },

    compareByRateDownToUp(rate) {
      return function (a, b) {
        return a[rate] - b[rate];
      };
    },

    onOrderConfirm(value) {
      if (value == "学号")
        console.log(this.students.sort(this.compareById("id")));
      if (value == "姓名")
        console.log(this.students.sort(this.compareByName("name")));
      if (value == "出勤率从大到小")
        console.log(this.students.sort(this.compareByRateUpToDown("rate")));
      if (value == "出勤率从小到大")
        console.log(this.students.sort(this.compareByRateDownToUp("rate")));
      this.showOrderPicker = false;
    },

    onClickStudent(index) {
      this.showStudentInfo = true;
      this.choice = index;
    },
  },
};
</script>

<style>
.studentList {
  position: relative;
  top: 2vh;
  left: 0;
  right: 0;
  margin: 0 5px;
  background: #fff;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
  animation-duration: 0.8s;
}
</style>