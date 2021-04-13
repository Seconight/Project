<template>
  <div id="myChart" :style="{ width: 'screenWidth', height: '400px' }"></div>
</template>
<script>
import * as echarts from "echarts";
//Vue.prototype.$echarts = echarts;
export default {
  props: ["chartData"],
  data() {
    return {
      xAxisData: [],
      acStuData: [],
      screenWidth: document.body.clientWidth, // 屏幕尺寸
    };
  },
  mounted() {
    for (let i = 0; i < this.chartData.records.length; i++) {
      this.xAxisData.push(i + 1);
      this.acStuData.push(this.chartData.records[this.chartData.records.length-i-1].acStudentNum);
    }
    this.drawLine();
  },
  methods: {
    drawLine() {
      let _this = this;
      // 基于准备好的dom，初始化echarts实例
      let myChart = echarts.init(document.getElementById("myChart"));
      // 绘制图表
      myChart.setOption({
        backgroundColor: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: "#FFFEFF" },
          { offset: 1, color: "#D7FFFE" },
        ]),
        title: {
          text: "出勤统计",
          subtext:
            this.chartData.course.name + "(" + this.chartData.course.id + ")",
          x: "center",
        },

        legend: {
          // orient 设置布局方式，默认水平布局，可选值：'horizontal'（水平） ¦ 'vertical'（垂直）
          orient: "vertical",
          // x 设置水平安放位置，默认全图居中，可选值：'center' ¦ 'left' ¦ 'right' ¦ {number}（x坐标，单位px）
          x: "left",
          // y 设置垂直安放位置，默认全图顶端，可选值：'top' ¦ 'bottom' ¦ 'center' ¦ {number}（y坐标，单位px）
          y: "top",
        },

        //  图表距边框的距离,可选值：'百分比'¦ {number}（单位px）
        grid: {
          top: "25%", // 等价于 y: '16%'
          left: "3%",
          right: "3%",
          bottom: "3%",
          containLabel: true,
        },

        // 提示框

        tooltip: {
          trigger: "axis",
          formatter: function (params, ticket, callback) {
            var htmlStr = "<div style='text-align:center;font-size: 18px;'>";
            console.log(_this.chartData.records);
            var param = params[0];
            var xName = param.name; //x轴的名称
            var seriesName = param.seriesName; //图例名称
            var value = param.value; //y轴值
            var color = param.color; //图例颜色
            htmlStr += "第" + xName + "次考勤" + "</div>"; //x轴的名称
            let index = parseInt(xName)-1;
            htmlStr +=
              '<div style="font-size: 12px;text-align:center;">' +
              _this.chartData.records[_this.chartData.records.length-1-index].time +
              "</div>";
            // 具体显示的数据【字段名称：seriesName，值：value】
            // 这里判断一下name，如果是我们需要特殊处理的，就处理

            // 正常显示的数据，走默认
            htmlStr +=
              '<span style="margin-right:5px;display:inline-block;width:10px;height:10px;border-radius:5px;background-color:' +
              color +
              ';"></span>';
            htmlStr += seriesName + "：" + value + "人";

            htmlStr += "</div>";

            return htmlStr;
          },
        },
        //工具框，可以选择
        toolbox: {
          feature: {
            saveAsImage: {}, //下载工具
          },
        },
        xAxis: {
          type: "category",
          axisLine: {
            lineStyle: {
              // 设置x轴颜色
              color: "#6E7FF3",
            },
          },
          // // 设置X轴数据旋转倾斜
          // axisLabel: {
          //   rotate: 30, // 旋转角度
          //   interval: 0, //设置X轴数据间隔几个显示一个，为0表示都显示
          // },
          // boundaryGap值为false的时候，折线第一个点在y轴上
          boundaryGap: false,
          data: this.xAxisData,
        },
        yAxis: {
          name: "人数",
          type: "value",
          min: 0, // 设置y轴刻度的最小值
          max:
            this.chartData.records[0].abStudentNum +
            this.chartData.records[0].acStudentNum, // 设置y轴刻度的最大值
          splitNumber: 5, // 设置y轴刻度间隔个数
          minInterval: 1,
          axisLine: {
            lineStyle: {
              // 设置y轴颜色
              color: "#6E7FF3",
            },
          },
        },
        series: [
          {
            name: "出勤人数",
            data: this.acStuData,
            type: "line",
            // 设置折线上圆点大小
            symbolSize: 8,
            itemStyle: {
              label: {
                show: true,
              },
              borderColor: "red", // 拐点边框颜色
              lineStyle: {
                width: 5, // 设置线宽
                type: "solid", //'dotted'虚线 'solid'实线
              },
            },
            smooth: 0.5,
          },
        ],
        color: ["#3dff3d"],
      });
    },
  },
};
</script>
<style scoped>
.a {
  color: #3dff3d;
}
</style>