<template>
  <div>
    <van-nav-bar title="添加课程" left-text="返回" @click-left="onClickLeft" />
    <form action="/">
      <van-search
        v-model="searchValue"
        show-action
        left-icon=""
        placeholder="请输入课程名搜索关键词或课程号查找课程"
        @search="onSearch"
        @clear="onClear"
      >
        <template #action>
          <van-icon
            name="search"
            @click="onSearch"
            color="#1989fa"
            size="20"
            style="padding: 10px"
          />
        </template>
      </van-search>
    </form>
    <van-dropdown-menu active-color="#1989fa">
      <van-dropdown-item v-model="semesterItem" :options="semesterOptions" />
    </van-dropdown-menu>
    <van-cell-group>
      <van-cell
        :title="course.name"
        center
        :label="course.teachername"
        v-for="course in allCourses[semesterItem]"
        :key="course.id"
        @click="showCourseDetail(course)"
      >
        <template #default>
          <van-button
            plain
            type="primary"
            size="mini"
            @click.stop="addCourse(course)"
            >添加课程</van-button
          >
        </template>
      </van-cell>
    </van-cell-group>
    <van-action-sheet
      v-model="showCourseInfo"
      title="课程信息"
      position="bottom"
    >
      <div class="content">
        <van-cell-group>
          <van-cell title="课程号" :value="courseDetail.id" />
          <van-cell title="课程名" :value="courseDetail.name" />
          <van-cell title="任课老师" :value="courseDetail.teachername" />
          <van-cell title="上课时间" :value="courseDetail.time" />
          <van-cell title="周次" :value="courseDetail.week" />
        </van-cell-group>
      </div>
    </van-action-sheet>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchValue: "",
      semesterItem: 0,
      semesterOptions: [{ text: "全部课程", value: 0 }],
      allCourses: [[]],
      courseDetail: [],
      showCourseInfo: false,
      coursesStorage: [],
    };
  },
  created() {
    //以课程名为""搜索全部可添加课程
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    var axios = require("axios");
    var config = {
      method: "get",
      url:
        this.GLOBAL.port +
        "/user/searchByCourseName?courseName=" +
        "" +
        "&studentId=" +
        userInfo.id,
      headers: {},
    };
    let _this = this;
    axios(config)
      .then(function (response) {
        console.log(response.data);
        _this.coursesStorage = response.data.data;
        _this.loadCourse(response.data.data);
      })
      .catch(function (error) {
        console.log(error);
      });
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    onClear() {
      this.loadCourse(this.coursesStorage);
    },
    onSearch() {
      //调用接口
      //暂时使用搜素自己课程的接口进行测试
      console.log("search " + this.searchValue);
      var sign = 0;
      for (var i = 0; i < this.searchValue.length; i++) {
        if (
          this.searchValue.charAt(i) >= "0" &&
          this.searchValue.charAt(i) <= "9"
        ) {
          sign = 1;
        }
      }
      const _this = this;

      let userInfo = localStorage.getItem("userInfo");
      let _userInfo = JSON.parse(userInfo);
      console.log(_userInfo.id);

      if (sign == 1) {
        var axios = require("axios");
        if (_userInfo.id.length == 13) {
          var config = {
            method: "get",
            url:
              this.GLOBAL.port +
              "/user/searchByCourseId?courseId=" +
              this.searchValue +
              "&studentId=" +
              _userInfo.id,
            headers: {},
          };
        } else {
          var config = {
            method: "get",
            url:
              this.GLOBAL.port +
              "/teacher/searchById?courseId=" +
              this.searchValue +
              "&teacherId=" +
              _userInfo.id,
            headers: {},
          };
        }

        axios(config)
          .then(function (response) {
            if (response.data.code == 0) {
              //搜索失败
              _this.$toast("课程号搜索失败，请重新检查输入的课程号！");
              _this.searchValue = "";
              return;
            }
            let temp = [];
            temp.push(response.data.data);
            console.log(temp);
            _this.loadCourse(temp);
          })
          .catch(function (error) {
            console.log(error);
          });
      } else {
        var axios = require("axios");

        if (_userInfo.id.length == 13) {
          var config = {
            method: "get",
            url:
              this.GLOBAL.port +
              "/user/searchByCourseName?courseName=" +
              this.searchValue +
              "&studentId=" +
              _userInfo.id,
            headers: {},
          };
        } else {
          var config = {
            method: "get",
            url:
              this.GLOBAL.port +
              "/teacher/searchByName?courseName=" +
              this.searchValue +
              "&teacherId=" +
              _userInfo.id,
            headers: {},
          };
        }

        axios(config)
          .then(function (response) {
            console.log(response.data);
            _this.loadCourse(response.data.data);
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    },
    dayChange(day) {
      switch (day) {
        case 1:
          return "星期一";
        case 2:
          return "星期二";
        case 3:
          return "星期三";
        case 4:
          return "星期四";
        case 5:
          return "星期五";
        case 6:
          return "星期六";
        case 7:
          return "星期天";
      }
    },
    getTime(days, stime, etime) {
      var time = "";
      for (let i = 0; i < days.length; i++) {
        time += this.dayChange(days[i]) + " ";
      }
      return time + stime + "-" + etime + "节 ";
    },
    loadCourse(data) {
      this.allCourses = [[]];
      this.semesterOptions = [];
      this.semesterOptions.push({ text: "全部课程", value: 0 }); //全部课程选项
      //遍历每个学期
      if (data.length == 0) {
        this.$toast("暂无课程");
      }
      for (let i = 0; i < data.length; i++) {
        let semester = []; //semester存储该学期的课程
        this.semesterOptions.push({
          text: data[i].semester,
          value: i + 1,
        }); //全部课程选项

        let Courses = data[i].course;
        for (let j = 0; j < Courses.length; j++) {
          let temp = {
            id: Courses[j].id,
            name: Courses[j].name,
            time: this.getTime(
              Courses[j].days,
              Courses[j].stime,
              Courses[j].etime
            ),
            week:
              Courses[j].weeks[0] +
              "-" +
              Courses[j].weeks[Courses[j].weeks.length - 1] +
              "周",
            semester: data[i].semester,
            teachername:
              this.role == "老师"
                ? JSON.parse(localStorage.getItem("userInfo")).name
                : Courses[j].teachername, //如果是老师，则为老师自己的名字
          };
          semester.push(temp);
          this.allCourses[0].push(temp); //添加到全部课程中
        }
        this.allCourses.push(semester);
      }
      console.log(this.allCourses);
    },
    showCourseDetail(course) {
      this.courseDetail = course;
      this.showCourseInfo = true;
    },
    addCourse(course) {
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      let studentId = userInfo.id;
      let courseId = course.id;
      console.log("studentId:" + studentId);
      console.log("courseId:" + courseId);
      //通过接口   studentId和courseId  添加课程
      if (true) {
        this.$toast("添加课程成功!");
      }
    },
  },
};
</script>

<style>
</style>