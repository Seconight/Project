<template>
  <div class="body">
    <van-nav-bar
      title="签到记录"
      left-text="返回"
      right-text="课程信息"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
      safe-area-inset-top
    />
    <div class="recordsList">
      <van-cell-group>
        <van-empty
          v-if="records.length == 0"
          :image="noRecordSrc"
          description="暂无签到记录"
        />
        <div v-if="records.length != 0">
          <van-cell
            v-for="(record, index) in records"
            :key="record.time"
            :title="records[index].time"
          >
            <template #default>
              <div v-if="records[index].success">
                <span style="color: #2cc20e">已签到</span>
                <van-icon
                  name="passed"
                  style="padding: 5px; line-height: inherit"
                  color="#2cc20e"
                />
              </div>
              <div v-if="!records[index].success">
                <span style="color: #ee0a24">未签到</span>
                <van-icon
                  name="close"
                  style="padding: 5px; line-height: inherit"
                  color="#ee0a24"
                />
              </div>
            </template>
          </van-cell>
        </div>
      </van-cell-group>
    </div>

    <van-action-sheet v-model="showCourseInfo" title="课程信息" position="top">
      <div class="content">
        <van-cell-group>
          <van-cell title="课程号" :value="course.id" />
          <van-cell title="课程名" :value="course.name" />
          <van-cell title="任课老师" :value="course.teachername" />
          <van-cell title="上课时间" :value="course.time" />
          <van-cell title="周次" :value="course.week" />
          <van-cell title="学期" :value="course.semester" />
        </van-cell-group>
      </div>
    </van-action-sheet>
  </div>
</template>

<script>
export default {
  props: ["course"],
  data() {
    return {
      noRecordSrc: require("@/assets/course/noRecord.png"),
      showCourseInfo: false,
      records: [],
    };
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    let courseID = this.course.id;
    //根据courseID和userInfo.id获得学生改课程所有签到记录
    var axios = require("axios");
    var config = {
      method: "get",
      url:
        this.GLOBAL.port +
        "/course/attendanceInfo?courseId=" +
        courseID +
        "&studentId=" +
        userInfo.id,
      headers: {},
    };
    let _this = this;
    axios(config)
      .then(function (response) {
        _this.loadRecord(response.data.data);
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
    timeChange(time) {
      //把1-1转化为第一周周一
      let index = time.indexOf("-");
      let fir = "第" + parseInt(time.substring(0, index)) + "周";
      let sec = this.dayChange(parseInt(time.substring(index + 1)));
      return fir + " " + sec;
    },
    loadRecord(result) {
      if (result.length == 0) {
        this.$toast("该课程无签到记录");
      }
      for (let i = 0; i < result.length; i++) {
        let temp = {
          time: this.timeChange(result[i].time),
          success: result[i].success,
        };
        this.records.push(temp);
      }
    },
  },
};
</script>

<style lang='less' scoped>
.recordsList {
  position: relative;
  top: 3vh;
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
</style>