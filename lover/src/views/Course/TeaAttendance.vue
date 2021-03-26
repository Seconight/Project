<template>
  <div>
    <van-nav-bar
      left-text="返回"
      right-text="课程信息"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
    >
      <template #title>
        <span> 签到记录</span>
      </template>
    </van-nav-bar>
    <van-cell-group class="recordsList">
      <van-cell
        center
        v-for="(record, index) in records"
        :key="record.id"
        is-link
        @click="goToDetail(record)"
      >
        <template #title style="display: flex">
          <div
            style="float: left; padding: 10px; font-size: 20px; color: #b3b3b3"
          >
            {{ index + 1 }}
          </div>
          <div style="margin-left: 5px; float: left; font-size: 18px">
            <div>{{ record.time }}</div>

            <div style="font-size: 10px; color: #b3b3b3">
              签到{{ record.acStudentNum }}人 缺勤{{ record.abStudentNum }}人
            </div>
          </div>
        </template>
        <template #default>
          <van-circle
            v-model="currentRate[index]"
            :speed="100"
            :color="gradientColor[index]"
            :rate="data[index].rate"
            :text="data[index].text"
            :clockwise="false"
            size="60px"
            :stroke-width="100"
          />
        </template>
      </van-cell>
    </van-cell-group>
    <van-action-sheet v-model="showCourseInfo" title="课程信息" position="top">
      <div class="content">
        <van-cell-group>
          <van-cell title="课程号" :value="course.id" />
          <van-cell title="任课老师" :value="course.teachername" />
          <van-cell title="上课时间" :value="course.time" />
          <van-cell title="周次" :value="course.week" />
          <van-cell title="学期" :value="course.semester" />
        </van-cell-group>
      </div>
    </van-action-sheet>
    <transition>
      <router-view></router-view>
    </transition>
  </div>
</template>

<script>
import TabHead from "@/components/TabHead.vue";

export default {
  props: ["course"],
  data() {
    return {
      currentRate: [],
      gradientColor: [],

      showCourseInfo: false,
      records: [],
      data: [],
    };
  },
  components: {
    TabHead,
  },
  created() {
    let courseID = this.course.id;
    //根据courseID和获得课程所有签到记录
    var axios = require("axios");

    var config = {
      method: "get",
      url: this.GLOBAL.port + "/teacher/checkAttendanceInfo?id=" + courseID,
      headers: {},
    };
    let _this = this;
    axios(config)
      .then(function (response) {
        console.log(response);
        if (response.data.code == 1) {
          let data = response.data.data;
          if (data.length == 0) {
            _this.$toast("没有签到记录。");
          } else {
            _this.loadRecord(data);
          }
        } else {
          _this.$toast("获取签到记录失败！");
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
      console.log(time.substring(index + 1));
      return fir + " " + sec;
    },
    loadRecord(result) {
      for (let i = 0; i < result.length; i++) {
        let temp = {
          attendanceId: result[i].attendanceId,
          time: this.timeChange(result[i].time),
          acStudentNum: result[i].acStudentNum,
          acStudent: result[i].acStudent,
          abStudentNum: result[i].abStudentNum,
          abStudent: result[i].abStudent,
        };
        this.records.push(temp);
      }
      for (let index = 0; index < this.records.length; index++) {
        let rate = 0;
        if (
          this.records[index].acStudentNum + this.records[index].abStudentNum !=
          0
        )
          rate =
            (100 * this.records[index].acStudentNum) /
            (this.records[index].acStudentNum +
              this.records[index].abStudentNum);
        let text = rate.toFixed(0) + "%";
        this.gradientColor.push(this.switchcolor(rate));
        // linear-gradient(to top, #96fbc4 0%, #f9f586 100%); //中等  75-90
        //background-image: linear-gradient(-20deg, #  0%, #fc6076 100%);//红 50-75
        this.data.push({ text: text, rate: rate });
        this.currentRate.push(0);
      }
      console.log(this.data);
    },
    switchcolor(rate) {
      if (rate < 50) {
        return {
          "0%": "#ff9a44",
          "100%": "#fc6076",
        };
      } else if (rate < 75) {
        return {
          "0%": "#96fbc4",
          "100%": "#f9f586",
        };
      } else {
        return {
          "0%": "#00FF00",
          "100%": "#38f9d7",
        };
      }
    },
    goToDetail(record) {
      localStorage.setItem("attendanceDetail", JSON.stringify(record));
      console.log(record)
      this.$router.push("/Course/attendanceDetail");
    },
  },
};
</script>

<style lang='less' scoped>
.collapseTitle {
  padding: 10px;
  // background: rgb(117, 213, 236);
  background: linear-gradient(to right, #63d5f1, #5d87d4);
  border-radius: 10px;
}
.recordsList {

  top: 1vh;
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