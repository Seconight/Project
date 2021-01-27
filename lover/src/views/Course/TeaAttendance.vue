<template>
  <div class="TeaAttendance">
    <van-nav-bar
      title="签到记录"
      left-text="返回"
      right-text="课程信息"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
    />
    <van-collapse v-model="activeNames">
      <van-collapse-item v-for="(record, index) in records" :key="record.id">
        <template #title>
          <div class="collapseTitle">
            <div>签到时间:{{ records[index].time }}</div>
            <span>签到人数:{{ records[index].acStudentNum }}人</span>
            <span style="padding: 50px"
              >缺勤人数:{{ records[index].abStudentNum }}人</span
            >
          </div>
        </template>
        <van-tabs
          v-model="recordActive[index]"
          type="card"
          color="#63d5f1"
          animated
          swipeable
          sticky
        >
          <van-tab title="实到学生" style="text-align: center; font-size: 16px">
            <TabHead v-if="records[index].acStudentNum != 0" />
            <van-row
              v-for="acStudent in records[index].acStudent"
              :key="acStudent.studentNo"
            >
              <van-col span="8">{{ acStudent.studentNo }}</van-col>
              <van-col span="8">{{ acStudent.studentName }}</van-col>
              <van-col span="8">{{ acStudent.studentClass }}</van-col>
            </van-row>
          </van-tab>
          <van-tab title="缺勤学生" style="text-align: center; font-size: 16px">
            <TabHead v-if="records[index].abStudentNum != 0" />
            <van-row
              v-for="(abStudent,abindex) in records[index].abStudent"
              :key="abStudent.studentNo"
              @click="supply(index,abindex,abStudent, records[index].attendanceId)"
            >
              <van-col span="8">{{ abStudent.studentNo }}</van-col>
              <van-col span="8">{{ abStudent.studentName }}</van-col>
              <van-col span="8">{{ abStudent.studentClass }}</van-col>
            </van-row>
          </van-tab>
        </van-tabs>
      </van-collapse-item>
    </van-collapse>
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
  </div>
</template>

<script>
import TabHead from "@/components/TabHead.vue";

export default {
  props: ["course"],
  data() {
    return {
      activeNames: [],
      showCourseInfo: false,
      records: [],
      recordActive: [],
    };
  },
  components: {
    TabHead,
  },
  created() {
    let courseID = this.course.id;
    //根据courseID和获得课程所有签到记录
    let data = [
      {
        attendanceId: "0000000001",
        time: "1-1",
        acStudentNum: 3,
        acStudent: [
          {
            studentNo: "0121810880214",
            studentName: "王宇",
            studentClass: "计算机1801",
          },
          {
            studentNo: "0121810880204",
            studentName: "田家兴",
            studentClass: "计算机1801",
          },
          {
            studentNo: "0121810880120",
            studentName: "徐凯",
            studentClass: "计算机1801",
          },
        ],
        abStudentNum: 0,
        abStudent: [],
      },
      {
        attendanceId: "0000000002",
        time: "2-3",
        acStudentNum: 1,
        acStudent: [
          {
            studentNo: "0121810880204",
            studentName: "田家兴",
            studentClass: "计算机1801",
          },
        ],
        abStudentNum: 2,
        abStudent: [
          {
            studentNo: "0121810880214",
            studentName: "王宇",
            studentClass: "计算机1801",
          },
          {
            studentNo: "0121810880120",
            studentName: "徐凯",
            studentClass: "计算机1801",
          },
        ],
      },
    ];
    for (let i = 0; i < data.length; i++) {
      this.recordActive.push(0);//添加与签到记录个数相同的导航栏的选择项
    }
    this.loadRecord(data);
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
    },
    supply(index,abindex, abStudent, attendanId) {
      let _this=this;
      this.$dialog
        .confirm({
          title: "补签",
          message:
            "姓名:" +
            abStudent.studentName +
            "      班级:" +
            abStudent.studentClass,
        })
        .then(() => {
          // on confirm
          //abStudent.studentNo和attendanId进行补签接口
          let supStudent=_this.records[index].abStudent.splice(abindex,1);//在缺席学生删除该学生
          _this.records[index].abStudentNum--;
          _this.records[index].acStudent.push(supStudent[0]);
          _this.records[index].acStudentNum++;
          console.log(_this.records);
          _this.$toast("补签成功");
        })
        .catch(() => {
          // on cancel
        });
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
</style>