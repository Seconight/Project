<template>
  <div class="body">
    <!-- <div class="usercenter-banner">
      <div class="_userinfo-avatar">
        <img :src="assert.avatarSrc" alt="" @click="onclickPersenInfor" />
      </div>
      <div class="_userinfo-name">{{ name }}</div>
      <div class="_userinfo-id" v-if="this.role == '老师'">职工号:{{ id }}</div>
      <div class="_userinfo-id" v-if="this.role == '学生'">学号:{{ id }}</div>
    </div> -->
    <van-row>
      <van-col span="6">
        <img :src="assert.avatarSrc" alt="" @click="onclickPersenInfor" />
      </van-col>
      <van-col span="14" class="userInfo">
        <div class="name">{{ name }}</div>
        <div class="id" v-if="this.role == '老师'">职工号:{{ id }}</div>
        <div class="id" v-if="this.role == '学生'">学号:{{ id }}</div>
      </van-col>
      <van-col span="4"> </van-col>
    </van-row>
    <van-grid column-num="3" square :border="false" class="menu">
      <van-grid-item icon="envelop-o" text="邮箱设置" to="/User/email">
        <template #icon>
          <van-icon :name="assert.gridImg.email" size="40px" />
        </template>
      </van-grid-item>
      <van-grid-item
        icon="closed-eye"
        text="修改密码"
        @click="onChangePassword"
      >
        <template #icon>
          <van-icon :name="assert.gridImg.changePassword" size="40px" />
        </template>
      </van-grid-item>
      <van-grid-item
        v-if="role == '学生'"
        icon="smile-o"
        text="人脸上传"
        to="/User/faceUpload"
      >
        <template #icon>
          <van-icon :name="assert.gridImg.faceUpload" size="40px" />
        </template>
      </van-grid-item>
    </van-grid>
    <div class="courseToday">
      <van-cell-group>
        <template #title>
          <span>今日课程</span>
          <span style="float: right">{{ time }}</span>
        </template>
        <van-empty
          v-if="courseToday.length == 0"
          :image="assert.noCourseTodaySrc"
          image-size="30%"
          description="今日无课"
        />
        <van-cell
          v-for="(course, index) in courseToday"
          :key="course.id"
          :title="course.name"
          center
          clickable
          :label="course.stime + '-' + course.etime + '节'"
          size="large"
          is-link
          @click="onClickCourseToday(index)"
        >
          <template #default>
            <div style="color: blue">点击查看</div>
          </template>
        </van-cell>
      </van-cell-group>
    </div>
    <transition name="van-slide-right">
      <router-view @logout="logout"></router-view>
    </transition>
  </div>
</template>

<script>
//var axios = require('axios');
export default {
  data() {
    return {
      name: "",
      assert: {
        noCourseTodaySrc: require("@/assets/user/noCourseToday.png"),
        avatarSrc: require("@/assets/user/defualt.jpg"),
        gridImg: {
          email: require("@/assets/icon/youxiangshezhi.png"),
          changePassword: require("@/assets/icon/xiugaimima.png"),
          faceUpload: require("@/assets/icon/renlainshangchuan.png"),
        },
      },
      //identity: '1',
      role: "",
      id: "",
      imgList: [],
      time: "",
      courseToday: [],
    };
  },
  created() {
    let date = new Date();
    let month = date.getMonth();
    let year = date.getFullYear();
    let semester = "";
    if (month <= 8) {
      semester = year - 1 + "-" + year + "-" + "2";
    } else {
      semester = year + "-" + (year + 1) + "-" + "1";
    }
    localStorage.setItem("semester", semester);
    //进入user页面判断是否登录
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (userInfo) {
      //存在已登录信息
      this.showinfor(); //更新组件信息显示
    } else {
      this.$router.push("/login");
    }
  },

  methods: {
    onclickPersenInfor() {
      this.$router.push("/User/personalInfo");
    },
    onRead(file) {
      let content = file.file;
      this.GLOBAL.faceFile = content;
    },
    //组件个人信息显示
    showinfor() {
      let _userInfo = JSON.parse(localStorage.getItem("userInfo"));
      this.name = _userInfo.name;
      this.role = _userInfo.role;
      this.id = _userInfo.id;
      let date = new Date();
      this.time =
        "      " +
        date.getFullYear() +
        "年" +
        (date.getMonth() + 1) +
        "月" +
        date.getDate() +
        "日" +
        "  " +
        this.dayChange(date.getDay());
      localStorage.setItem("date", this.time);
      if (this.role == "老师") {
        this.assert.avatarSrc = require("@/assets/user/teacher.jpg");
      }
      if (this.role == "学生") {
        this.assert.avatarSrc = require("@/assets/user/student.jpg");
        //判断人脸是否注册
        this.axiosCheckFace(_userInfo.id);
      }
      let _this = this;
      if (_userInfo.role == "老师") {
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
            } else {
              _this.loadCourseToday(response.data.data);
            }
          })
          .catch(function (error) {
            console.log(error);
          });
      }
      if (_userInfo.role == "学生") {
        var axios = require("axios");
        var config = {
          method: "get",
          url: this.GLOBAL.port + "/course/info?id=" + _userInfo.id,
          headers: {},
        };
        axios(config)
          .then(function (response) {
            if (response.data.code == 0) {
              _this.$toast("获取课程信息失败");
              return;
            } else {
              _this.loadCourseToday(response.data.data);
            }
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    },
    loadCourseToday(data) {
      let date = new Date();
      let day = date.getDay();
      if (day == 0) {
        //星期天为0，匹配数据库模式
        day = 7;
      }
      let semester = localStorage.getItem("semester");
      // day=2;
      //console.log("day:" + day + "  month:" + month + " year:" + year);
      for (let i = 0; i < data.length; i++) {
        //判断是否是当前学期
        if (data[i].semester == semester) {
          for (let j = 0; j < data[i].course.length; j++) {
            //今天可能上的课程（没判断周次，只判断星期）
            if (data[i].course[j].days.indexOf(day) != -1) {
              this.courseToday.push(data[i].course[j]);
            }
          }
        }
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
        case 0:
          return "星期天";
      }
    },
    onClickCourseToday(index) {
      localStorage.setItem(
        "courseToday",
        JSON.stringify(this.courseToday[index])
      );
      this.$router.push("/course");
    },
    //上传人脸
    // uploadface() {
    //   this.$dialog
    //     .confirm({
    //       title: "确定上传人脸数据",
    //     })
    //     .then(() => {
    //       // on confirm
    //       //上传人脸数据,只能上传一张照片
    //       var axios = require("axios");
    //       var FormData = require("form-data");
    //       var data = new FormData();
    //       data.append("faceImage", this.GLOBAL.faceFile);
    //       //console.log("this is "+this.GLOBAL.faceFile);
    //       data.append("studentId", this.id);

    //       var config = {
    //         method: "post",
    //         url: this.GLOBAL.port + "/user/faceInfo",
    //         headers: {
    //           //...data.getHeaders(),
    //           //设置请求头
    //           "Content-Type": "multipart/form-data",
    //         },
    //         data: data,
    //       };

    //       axios(config)
    //         .then(function (response) {
    //           console.log(JSON.stringify(response.data));
    //         })
    //         .catch(function (error) {
    //           console.log(error);
    //         });

    //       this.$toast.success("上传成功");
    //     })
    //     .catch(() => {
    //       // on cancel
    //     });
    // },
    //登录

    //退出登录事件
    logout() {
      localStorage.removeItem("userInfo");
      localStorage.removeItem("checkFace");
      localStorage.removeItem("semester");
      this.name = "";
      this.avatarSrc = require("@/assets/defualt.jpg");
      this.username = "";
      this.password = "";
      this.showLoginModal = false;
      this.role = "";
      this.imgList = [];
      this.preview_options.closeable = true;
      this.$router.push("/login");
    },
    onChangePassword() {
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      if (userInfo.address == null) {
        this.$toast("邮箱未绑定!,请先绑定邮箱！");
      } else {
        this.$router.push("/User/changePassword");
      }
    },
    axiosCheckFace(userID) {
      var axios = require("axios");
      var config = {
        method: "get",
        url: this.GLOBAL.port + "/user/checkFace?id=" + userID,
        headers: {},
      };
      axios(config)
        .then(function (response) {
          // console.log(response.data.data)
          localStorage.setItem("checkFace", response.data.data);
          // var current = localStorage.getItem("checkFace");
          // console.log(current);
        })
        .catch(function (error) {
          console.log(error);
        });
    },
  },
};
</script>
<style lang="less" scoped>
.van-row {
  height: 100px;
  padding: 10px;
  background-image: linear-gradient(#a1c4fd 0%, #c2e9fb 100%);
  color: #000;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.5);
  border-radius: 0 0 20px 20px;
  animation-duration: 0.8s;
  .van-col {
    height: 80px;
    line-height: 80px;
    img {
      padding:5px;
      height: 70px;
      width: 70px;
      border-radius: 50%;
    }
    &:last-child {
      text-align: right;
    }
  }
  .userInfo {
    margin-top: 15px;
    height: 50px;
    line-height: 25px;
    .name {
      font-size: 20px;
      font-weight: 600;
    }
    .id {
      font-size: 18px;
    }
  }
}
// .usercenter-banner {
//   position: relative;
//   width: 100%;
//   height: 15vh;
//   box-sizing: border-box;
//   box-shadow: 0 0 24px rgba(0, 0, 0, 0.5);
//   border-radius: 0 0 20px 20px;
//   animation-duration: 0.8s;
//   background-image: linear-gradient(#a1c4fd 0%, #c2e9fb 100%);
//   ._userinfo-avatar {
//     position: relative;
//     top: 2vh;
//     left: 2vh;
//     width: 80px;
//     height: 80px;
//     border-radius: 50%;
//     overflow: hidden;
//     img {
//       left: auto;
//       width: 100%;
//       height: 100%;
//     }
//     // ._userinfo-name {
//     //   padding: 10px;
//     //   font-size: 1.125rem;
//     //   font-weight: 600;
//     //   color: #000;
//     // }
//   }
//   ._userinfo-name {
//     position: absolute;
//     top: 4vh;
//     left: 15vh;
//     font-size: 1.5rem;
//     font-weight: 600;
//     color: #000;
//   }
//   ._userinfo-id {
//     position: absolute;
//     top: 8vh;
//     left: 15vh;
//     font-size: 0.8rem;
//     font-weight: 400;
//     color: #000;
//   }
// }
.menu {
  position: relative;
  top: 5vh;
  left: 0;
  right: 0;
  margin: 0 5px;
  background: #fff;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
  border-radius: 20px;
  animation-duration: 0.8s;
}
.courseToday {
  position: relative;
  top: 10vh;
  left: 0;
  right: 0;
  margin: 0 5px;
  background: #fff;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  animation-duration: 0.8s;
  white-space: pre;
}
</style>
