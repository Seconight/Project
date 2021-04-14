<template>
  <div class="body">
    <van-nav-bar
      left-text="返回"
      right-text="课程信息"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
      title="签到记录"
      safe-area-inset-top
    >
    </van-nav-bar>
    <div class="chartCell">
      <van-cell title="统计图" clickable @click="onclickChartCell" is-link />
    </div>
    <div
      class="recordsList"
      v-if="$route.path == '/Course/teaAttendance'"
      :key="menuKey"
    >
      <div v-if="records.length != 0">
        <div style="margin-left: 76%; position: relative; font-size: 12px">
          出勤率
        </div>
        <van-cell-group :border="false">
          <van-cell
            center
            v-for="(record, index) in records"
            :key="record.id"
            is-link
            @click="goToDetail(index)"
          >
            <template #title style="display: flex">
              <div style="float: left; font-size: 25px; color: #b3b3b3">
                {{ index + 1 }}
              </div>
              <div style="margin-left: 5px; float: left; font-size: 18px">
                <div>{{ record.time }}</div>

                <div style="font-size: 10px; color: #b3b3b3">
                  签到{{ record.acStudentNum }}人 缺勤{{
                    record.abStudentNum
                  }}人
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
      </div>
      <van-empty
        v-if="records.length == 0"
        :image="noRecordSrc"
        description="暂无签到记录"
      />
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
    <van-popup
      v-model="showChartPopup"
      :style="{ width: '100%', height: '400px' }"
    >
      <Chart :chartData="chartData" :key="chartTimerKey"></Chart>
    </van-popup>
    <transition name="van-slide-right">
      <router-view :record="records[attendanceDetailIndex]"></router-view>
    </transition>
  </div>
</template>

<script>
import Chart from "@/components/Chart.vue";
export default {
  watch: {
    $route(to) {
      if (to.name == "TeaAttendance") {
        this.records = [];
        this.data = [];
        this.gradientColor = [];
        this.currentRate = [];
        this.attendanceDetailIndex = 0;
        this.axiosRecords();
      }
    },
  },
  props: ["course"],
  data() {
    return {
      chartTimerKey: "",
      menuKey: 0,
      noRecordSrc: require("@/assets/course/noRecord.png"),
      currentRate: [],
      gradientColor: [],
      showCourseInfo: false,
      records: [],
      data: [],
      attendanceDetailIndex: 0,
      showChartPopup: false,
      chartData: {},
    };
  },
  components: {
    Chart,
  },
  created() {
    this.axiosRecords();
  },
  methods: {
    onclickChartCell() {
      if (this.records.length != 0) {
        this.chartData = {
          records: this.records,
          course: this.course,
        };
        this.chartTimerKey = new Date().getTime();
        this.showChartPopup = true;
      }else{
        this.$toast("无签到记录,无法生成统计图");
      }
    },
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
        case 0:
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
    goToDetail(index) {
      this.attendanceDetailIndex = index;
      this.$router.push("/course/teaAttendance/attendanceDetail");
    },
    axiosRecords() {
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
  },
};
</script>

<style lang='less' scoped>
.chartCell {
  position: relative;
  top: 1vh;
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
.collapseTitle {
  padding: 10px;
  // background: rgb(117, 213, 236);
  background: linear-gradient(to right, #63d5f1, #5d87d4);
  border-radius: 10px;
}
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