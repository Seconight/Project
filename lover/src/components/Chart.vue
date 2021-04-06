<template>
  <div id="myChart" :style="{ width: 'screenWidth', height: '400px' }"></div>
</template>
<script>
import * as echarts from 'echarts';
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
    console.log(this.chartData);
    for (let i = 0; i < this.chartData.records.length; i++) {
      this.xAxisData.push(i + 1);
      this.acStuData.push(this.chartData.records[i].acStudentNum);
    }
    this.drawLine();
  },
  methods: {
    drawLine() {
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
            smooth: 0.3,
          },
        ],
        color: ["#3dff3d"],
      });
    },
  },
};
</script>
<style scoped>
.a{
    color:#3dff3d
}
</style>