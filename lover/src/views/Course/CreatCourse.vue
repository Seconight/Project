<template>
  <div>
    <van-nav-bar title="创建课程" left-text="返回" @click-left="onClickLeft" />
    <van-form @submit="onSubmit">
      <van-field
        v-model="className"
        label="课程名"
        placeholder="请输入课程名"
        required
      />
      <van-field
        readonly
        clickable
        label="上课工作日"
        :value="dayText"
        placeholder="点击选择工作日"
        @click="showCourseDayPicker = true"
      />
      <van-popup v-model="showCourseDayPicker" round position="bottom">
        <van-checkbox-group v-model="day">
          <van-cell-group>
            <van-cell
              v-for="(item, index) in dayList"
              clickable
              :key="item"
              :title="item"
              @click="toggle(index)"
            >
              <template #right-icon>
                <van-checkbox :name="index" ref="checkboxes" />
              </template>
            </van-cell>
          </van-cell-group>
        </van-checkbox-group>
      </van-popup>
      <van-field
        readonly
        clickable
        name="timePicker"
        label="上课时间"
        :value="timeText"
        placeholder="点击选择上课时间段"
        @click="showCourseTimePicker = true"
      />
      <van-popup v-model="showCourseTimePicker" position="bottom">
        <van-picker
          title="选择上课时间段"
          show-toolbar
          :columns="timeColumns"
          @confirm="onTimePickerConfirm"
          @cancel="showCourseTimePicker = false"
          @change="onTimePickerChange"
        />
      </van-popup>
      <van-field
        readonly
        clickable
        name="weekPicker"
        label="上课周"
        :value="weekText"
        placeholder="点击选择课程周段"
        @click="showCourseWeekPicker = true"
      />
      <van-popup v-model="showCourseWeekPicker" position="bottom">
        <van-picker
          title="选择课程周段"
          show-toolbar
          :columns="weekColumns"
          @confirm="onWeekPickerConfirm"
          @cancel="showCourseWeekPicker = false"
          @change="onWeekPickerChange"
        />
      </van-popup>
      <van-field
        readonly
        clickable
        name="semesterPicker"
        label="学期"
        :value="semesterText"
        placeholder="点击选择学期"
        @click="showCourseSemesterPicker = true"
      />
      <van-popup v-model="showCourseSemesterPicker" position="bottom">
        <van-picker
          title="选择学期"
          show-toolbar
          :columns="semesterColumns"
          @confirm="onSemesterPickerConfirm"
          @cancel="showCourseSemesterPicker = false"
        />
      </van-popup>
      <div style="text-align: center; padding: 20px">
        <van-uploader accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" 
        v-model="excelfile" 
        :after-read="onRead">
          <van-button icon="plus" type="primary">导入学生名单</van-button>
        </van-uploader>
      </div>

      <div style="margin: 16px">
        <van-button round block type="info" native-type="submit"
          >提交</van-button
        >
      </div>
    </van-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      className: "",
      showCourseDayPicker: false,
      showCourseTimePicker: false,
      showCourseWeekPicker: false,
      showCourseSemesterPicker: false,
      day: [],
      dayText: "",
      dayList: [
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六",
        "星期天",
      ],
      timeColumns: [
        {
          values: [
            "第一节课",
            "第二节课",
            "第三节课",
            "第四节课",
            "第五节课",
            "第六节课",
            "第七节课",
            "第八节课",
            "第九节课",
            "第十节课",
            "第十一节课",
            "第十二节课",
            "第十三节课",
          ],
        },
        {
          values: [
            "第一节课",
            "第二节课",
            "第三节课",
            "第四节课",
            "第五节课",
            "第六节课",
            "第七节课",
            "第八节课",
            "第九节课",
            "第十节课",
            "第十一节课",
            "第十二节课",
            "第十三节课",
          ],
        },
      ],
      timeText: "",
      weekColumns: [
        {
          values: [
            "第一周",
            "第二周",
            "第三周",
            "第四周",
            "第五周",
            "第六周",
            "第七周",
            "第八周",
            "第九周",
            "第十周",
            "第十一周",
            "第十二周",
            "第十三周",
            "第十四周",
            "第十五周",
            "第十六周",
            "第十七周",
            "第十八周",
          ],
        },
        {
          values: [
            "第一周",
            "第二周",
            "第三周",
            "第四周",
            "第五周",
            "第六周",
            "第七周",
            "第八周",
            "第九周",
            "第十周",
            "第十一周",
            "第十二周",
            "第十三周",
            "第十四周",
            "第十五周",
            "第十六周",
            "第十七周",
            "第十八周",
          ],
        },
      ],
      weekText: "",
      semesterColumns: [
        "2020-2021-1",
        "2020-2021-2",
        "2021-2022-1",
        "2021-2022-2",
      ],
      semesterText: "",
      stime: -1,
      etime: -1,
      week: [],
      excelfile: [],
    };
  },
  watch: {
    day: function (day) {
      //修改dayText
      this.dayText = "";
      if (day.length == 0) {
        this.dayText = "";
      } else {
        let newday = [];
        for (let i = 0; i < day.length; i++) {
          newday.push(day[i]);
        }
        newday.sort();
        for (let i = 0; i < newday.length; i++) {
          this.dayText += this.dayList[newday[i]];
          if (i != newday.length - 1) this.dayText += ",";
        }
      }
    },
  },
  methods: {
    onRead(file){
      this.GLOBAL.studentFile = file.file;
    },
    onTimePickerConfirm(value, index) {
      this.showCourseTimePicker = false;
      this.timeText = value[0] + "-" + value[1];
      this.stime = this.timeColumns[0].values.indexOf(value[0]) + 1;
      this.etime = this.timeColumns[0].values.indexOf(value[1]) + 1;
    },
    onTimePickerChange(picker, value, index) {
      let temp = this.timeColumns[0].values.indexOf(value[0]);
      this.timeColumns[1].values = [
        "第一节课",
        "第二节课",
        "第三节课",
        "第四节课",
        "第五节课",
        "第六节课",
        "第七节课",
        "第八节课",
        "第九节课",
        "第十节课",
        "第十一节课",
        "第十二节课",
        "第十三节课",
      ];
      for (let i = 0; i < temp; i++) {
        this.timeColumns[1].values.shift();
      }
    },
    onWeekPickerConfirm(value, index) {
      this.showCourseWeekPicker = false;
      this.weekText = value[0] + "-" + value[1];
      this.week = [];
      for (
        let i = this.weekColumns[0].values.indexOf(value[0]);
        i <= this.weekColumns[0].values.indexOf(value[1]);
        i++
      ) {
        this.week.push(i + 1);
      }
    },
    onWeekPickerChange(picker, value, index) {
      let temp = this.weekColumns[0].values.indexOf(value[0]);
      this.weekColumns[1].values = [
        "第一周",
        "第二周",
        "第三周",
        "第四周",
        "第五周",
        "第六周",
        "第七周",
        "第八周",
        "第九周",
        "第十周",
        "第十一周",
        "第十二周",
        "第十三周",
        "第十四周",
        "第十五周",
        "第十六周",
        "第十七周",
        "第十八周",
      ];
      for (let i = 0; i < temp; i++) {
        this.weekColumns[1].values.shift();
      }
    },
    onSemesterPickerConfirm(picker, value, index) {
      this.semesterText = this.semesterColumns[value];
      this.showCourseSemesterPicker = false;
    },
    toggle(index) {
      this.$refs.checkboxes[index].toggle();
    },
    onClickLeft() {
      this.$router.go(-1);
    },
    onSubmit() {
      //day需要加1
      if (this.className == "") {
        this.$toast.fail("请输入课程名");
        return;
      }
      let newday = [];
      for (let i = 0; i < this.day.length; i++) {
        newday.push(this.day[i] + 1);
      }
      console.log(this.className);
      console.log(this.stime);
      console.log(this.etime);
      console.log(newday);
      console.log(this.week);
      console.log(this.semesterText);
      console.log(JSON.parse(localStorage.getItem("userInfo")).id);
      console.log(this.excelfile);

      var axios = require("axios");
      var FormData = require("form-data");
      var data = new FormData();
      data.append("name", this.className);
      data.append("sTime", this.stime);
      data.append("eTime", this.etime);
      data.append("day", newday);
      data.append("week", this.week);
      data.append("semester", this.semesterText);
      data.append("teacherId", JSON.parse(localStorage.getItem("userInfo")).id);
      //此处文件在根目录，其他情况可能要加路径
      data.append("students", this.GLOBAL.studentFile);

      var config = {
        method: "put",
        url: this.GLOBAL.port+"/teacher/createCourse",
        headers: {
          'Content-Type': 'multipart/form-data',
          
        },
        data: data,
      };

      axios(config)
        .then(function (response) {
          console.log(JSON.stringify(response.data));
        })
        .catch(function (error) {
          console.log(error);
        });

      this.$toast.success("新建课程成功 课程号:");
      this.$router.go(-1);
    },
  },
};
</script>

<style>
</style>