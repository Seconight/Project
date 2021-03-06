<template>
  <div>
    <div v-if="$route.path == '/course'">
      <van-nav-bar title="课程" fixed placeholder>
        <template #right>
          <van-popover
            v-model="showAddCoursePopover"
            trigger="click"
            :actions="
              role == '老师'
                ? creatCoursePopoverActions
                : addCoursePopoverActions
            "
            @select="onPopoverSelect"
            theme="dark"
            placement="bottom-end"
          >
            <template #reference>
              <van-icon name="add-o" size="20" color="black" />
            </template>
          </van-popover>
        </template>
      </van-nav-bar>
      <form action="/">
        <van-search
          v-model="searchValue"
          show-action
          left-icon=""
          placeholder="请输入课程名搜索关键词或课程号"
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
        <van-dropdown-item
          v-model="semesterItem"
          :options="semesterOptions"
          @change="semesterChange"
        />
      </van-dropdown-menu>
      <div>
        <van-collapse v-model="activeCourse">
          <van-collapse-item
            v-for="(course, index) in allCourses[semesterItem]"
            :key="course.id"
          >
            <template #title style="width: 200px">
              <div class="courseItem">{{ course.name }}</div>
            </template>
            <van-cell-group>
              <van-cell title="课程号" :value="course.id" />
              <van-cell title="任课老师" :value="course.teachername" />
              <van-cell title="上课时间" :value="course.time" />
              <van-cell title="周次" :value="course.week" />
              <van-cell title="学期" :value="course.semester" />
            </van-cell-group>
            <div style="text-align: center">
              <van-button
                plain
                type="info"
                size="small"
                icon="contact"
                @click="goToStudentList(index)"
                >学生名单</van-button
              >
              <van-button
                plain
                type="primary"
                size="small"
                icon="eye-o"
                style="margin-left: 10px"
                @click="goToAttendance(index)"
                >签到记录</van-button
              >
              <van-button
                v-if="role == '老师'"
                type="primary"
                size="small"
                icon="scan"
                style="margin-left: 10px"
                @click="photoSign(index)"
                >拍照签到</van-button
              >
            </div>
          </van-collapse-item>
        </van-collapse>
      </div>
      <div style="height: 50px"></div>
    </div>
    <transition>
      <router-view
        :course="allCourses[semesterItem][courseIndex]"
        @refresh="refresh"
      ></router-view>
    </transition>
    <van-popup v-model="showPhotoSign" round>
      <div class="photoSign">
        <van-uploader
          :preview-options="preview_options"
          v-model="imgList"
          multiple
          accept="image/*"
          style="padding: 18px"
          :after-read="onRead"
          :max-count="3"
        />
        <div style="position: fixed; bottom: 0px; left: 75px; padding: 20px">
          <van-button type="danger" @click="upLodaeSign" icon="upgrade"
            >上传签到</van-button
          >
        </div>
      </div>
    </van-popup>
    <van-overlay :show="photoSignUploading">
      <div class="wrapper" @click.stop>
        <van-loading color="#0094ff" size="80px" vertical
          >签到识别中...</van-loading
        >
      </div>
    </van-overlay>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showAddCoursePopover: false,
      creatCoursePopoverActions: [{ text: "新建课程" }],
      addCoursePopoverActions: [{ text: "添加课程" }],
      searchValue: "",
      activeCourse: [],
      role: "",
      allCourses: [[]],
      courseIndex: 0,
      imgList: [],
      showPhotoSign: false,
      preview_options: {
        closeable: true,
      },
      semesterItem: 0,
      semesterOptions: [{ text: "全部课程", value: 0 }],
      coursesStorage: [],
      photoSignUploading: false,
    };
  },
  created() {
    //判断是否登录
    let userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
      //存在已登录信息
      //获取登录身份
      let _userInfo = JSON.parse(userInfo);
      this.role = _userInfo.role;
      let _this = this;
      //通过_userInfo.id获得Courses
      //假数据
      if (this.role == "学生") {
        var axios = require("axios");

        var config = {
          method: "get",
          //这里用户信息就直接在url里了
          url: this.GLOBAL.port + "/course/info?id=" + _userInfo.id,
          headers: {},
        };

        axios(config)
          .then(function (response) {
            if (response.data.code == 0) {
              _this.$toast("获取课程信息失败");
              return;
            }
            _this.coursesStorage = response.data.data;
            _this.loadCourse(response.data.data);
          })
          .catch(function (error) {
            console.log(error);
          });
      } else if (this.role == "老师") {
        var axios = require("axios");
        var config = {
          method: "get",
          url: this.GLOBAL.port + "/teacher/getCourseInfo?id=" + _userInfo.id,
          headers: {},
        };
        axios(config)
          .then(function (response) {
            if (response.data.code == 0) {
              _this.$toast("获取课程信息失败");
              return;
            }
            _this.coursesStorage = response.data.data;
            _this.loadCourse(response.data.data);
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    }
  },
  methods: {
    refresh(val) {
      if (val) {//刷新页面
        location.reload();
      }
    },
    onRead(file) {
      this.GLOBAL.signFiles.push(file.file);
    },
    goToAttendance(index) {
      this.courseIndex = index;
      if (this.role == "老师") {
        this.$router.push("/Course/teaAttendance");
      } else if (this.role == "学生") {
        this.$router.push("/Course/stuAttendance");
      }
    },
    goToStudentList(index) {
      //暂时老师和学生共用一个学生列表界面
      this.courseIndex = index;
      if (this.role == "老师") {
        this.$router.push("/Course/stuStudentList");
      } else if (this.role == "学生") {
        this.$router.push("/Course/stuStudentList");
      }
    },
    photoSign(index) {
      //console.log(JSON.stringify(this.allCourses));
      this.GLOBAL.signFiles = [];
      this.courseIndex = index;
      console.log("this index is " + index);
      this.showPhotoSign = true;
    },
    upLodaeSign() {
      if (this.imgList.length == 0) {
        this.$toast("请添加图片");
      } else {
        this.photoSignUploading = true;
        this.showPhotoSign = false;
        let _this = this;
        //接口
        console.log(
          this.allCourses[this.semesterItem][this.courseIndex].id +
            " add attendance"
        );

        var axios = require("axios");
        var FormData = require("form-data");
        var data = new FormData();
        for (var i = 0; i < this.GLOBAL.signFiles.length; i++) {
          data.append("img", this.GLOBAL.signFiles[i]);
        }
        data.append(
          "id",
          this.allCourses[this.semesterItem][this.courseIndex].id
        );

        var config = {
          method: "post",
          url: this.GLOBAL.port + "/teacher/attendance",
          headers: {
            "Content-Type": "multipart/form-data",
          },
          data: data,
        };

        axios(config)
          .then(function (response) {
            console.log(JSON.stringify(response.data));
            _this.photoSignUploading = false;
            
            _this.$toast.success("上传成功，请在签到记录查看结果。");
            
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
    onPopoverSelect() {
      if (this.role == "老师") {
        this.$router.push("/Course/creatCourse");
      } else {
        this.$router.push("/Course/addCourse");
      }
    },
    onClear() {
      this.loadCourse(this.coursesStorage);
    },
    onSearch() {
      //调用接口
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
    semesterChange() {
      this.activeCourse = []; //保持所有课程缩放状态
    },
  },
};
</script>
<style lang='less' scoped>
.courseItem {
  padding: 10px;
  // background: rgb(117, 213, 236);
  background: linear-gradient(to right, #63d5f1, #5d87d4);
  border-radius: 10px;
  font-size: medium;
}
.photoSign {
  text-align: center;
  width: 300px;
  height: 200px;
}
.wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}
</style>