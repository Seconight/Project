<template>
  <div>
    <div v-if="$route.path == '/course'">
      <van-nav-bar title="课程" fixed placeholder>
        <template #right>
          <van-popover
            v-model="showAddCoursePopover"
            trigger="click"
            :actions="addCoursePopoverActions"
            @select="addCourse"
            theme="dark"
            placement="bottom-end"
          >
            <template #reference v-if="role == '老师'">
              <van-icon name="add-o" size="20" color="black" />
            </template>
          </van-popover>
        </template>
      </van-nav-bar>
      <div>
        <van-collapse v-model="activeNames">
          <van-collapse-item
            v-for="(course, index) in courses"
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
                type="primary"
                size="small"
                icon="eye-o"
                @click="goToAttendance(index)"
                >查看签到记录</van-button
              >
              <van-button
                v-if="role == '老师'"
                type="primary"
                size="small"
                icon="scan"
                style="width: 108.4px; margin-left: 50px"
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
      <router-view :course="courses[courseIndex]"></router-view>
    </transition>
    <van-popup v-model="showPhotoSign">
      <div class="photoSign">
        <van-uploader
          :preview-options="preview_options"
          v-model="imgList"
          multiple
          accept="image/*"
          style="padding: 18px"
          :max-count="3"
        />
        <div
          style="
            position: fixed;
            bottom: 0px;
            left: 75px;
            padding: 20px;
          "
        >
          <van-button type="danger" @click="upLodaeSign" icon="upgrade">上传签到</van-button>
        </div>
      </div>
    </van-popup>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showAddCoursePopover: false,
      addCoursePopoverActions: [{ text: "新建课程" }],
      activeNames: [],
      role: "",
      courses: [],
      courseIndex: 0,
      imgList: [],
      showPhotoSign: false,
      preview_options: {
        closeable: true,
      },
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
      //通过_userInfo.id获得Courses
      //假数据
      if (this.role == "学生") {
        let Courses = [
          {
            id: "0000000001",
            name: "C语言",
            stime: 1,
            etime: 2,
            days: [1, 2],
            week: [1, 2, 3, 4, 5, 6, 7, 8],
            semester: "2020-2021-1",
            teachername: "张1 ",
          },
          {
            id: "0000000002",
            name: "高等数学",
            stime: 3,
            etime: 5,
            days: [1, 4],
            week: [1, 2, 3, 4, 5, 6, 7, 8],
            semester: "2020-2021-1",
            teachername: "张2 ",
          },
          {
            id: "0000000003",
            name: "A语言",
            stime: 7,
            etime: 8,
            days: [1, 2, 3],
            week: [1, 2, 3, 4, 5, 6, 7, 8],
            semester: "2020-2021-1",
            teachername: "张3 ",
          },
          {
            id: "0000000004",
            name: "B语言",
            stime: 9,
            etime: 10,
            days: [2, 4],
            week: [1, 2, 3, 4, 5, 6, 7, 8],
            semester: "2020-2021-1",
            teachername: "张4 ",
          },
          {
            id: "0000000005",
            name: "C语言",
            stime: 6,
            etime: 8,
            days: [5],
            week: [1, 2, 3, 4, 5, 6, 7, 8],
            semester: "2020-2021-1",
            teachername: "张5 ",
          },
          {
            id: "0000000006",
            name: "D语言",
            stime: 1,
            etime: 2,
            days: [4, 5],
            week: [10, 11, 12, 13, 14, 15, 16, 17],
            semester: "2020-2021-1",
            teachername: "张6 ",
          },
        ];
        this.loadCourse(Courses);
      } else if (this.role == "老师") {
        let Courses = [
          {
            id: "0000000001",
            name: "C语言",
            stime: 1,
            etime: 2,
            days: [1, 2],
            week: [1, 2, 3, 4, 5, 6, 7, 8],
            semester: "2020-2021-1",
          },
          {
            id: "0000000002",
            name: "高等数学",
            stime: 3,
            etime: 5,
            days: [1, 4],
            week: [1, 2, 3, 4, 5, 6, 7, 8],
            semester: "2020-2021-1",
          },
          {
            id: "0000000003",
            name: "A语言",
            stime: 7,
            etime: 8,
            days: [1, 2, 3],
            week: [1, 2, 3, 4, 5, 6, 7, 8],
            semester: "2020-2021-1",
          },
        ];
        this.loadCourse(Courses);
      }
    }
  },
  methods: {
    goToAttendance(index) {
      this.courseIndex = index;
      if (this.role == "老师") {
        this.$router.push("/Course/teaAttendance");
      } else if (this.role == "学生") {
        this.$router.push("/Course/stuAttendance");
      }
    },
    photoSign(index) {
      this.courseIndex = index;
      this.showPhotoSign = true;
    },
    upLodaeSign() {
      if (this.imgList.length == 0) {
        this.$toast("请添加图片");
      }
      else{
        this.$toast.success("上传成功");
        this.showPhotoSign=false;
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
    loadCourse(Courses) {
      for (let i = 0; i < Courses.length; i++) {
        let temp = {
          id: Courses[i].id,
          name: Courses[i].name,
          time: this.getTime(
            Courses[i].days,
            Courses[i].stime,
            Courses[i].etime
          ),
          week:
            Courses[i].week[0] +
            "-" +
            Courses[i].week[Courses[i].week.length - 1] +
            "周",
          semester: Courses[i].semester,
          teachername:
            this.role == "老师"
              ? JSON.parse(localStorage.getItem("userInfo")).name
              : Courses[i].teachername, //如果是老师，则为老师自己的名字
        };
        this.courses.push(temp);
      }
    },
    addCourse(action) {
      this.$router.push("/Course/addCourse");
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
}
.photoSign {
  text-align: center;
  width: 300px;
  height: 200px;
}
</style>