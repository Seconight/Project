<template>
  <div class="body">
    <van-nav-bar
      title="拍照签到"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      safe-area-inset-top
    />
    <div class="photoSign">
      <van-cell-group>
        <van-cell title="课程名" :value="course.name" />
        <van-cell title="签到时间" :value="date" />
      </van-cell-group>
      <div style="text-align: center">
        <van-uploader
          :preview-options="preview_options"
          v-model="imgList"
          multiple
          accept="image/*"
          :after-read="onRead"
          :max-count="3"
          :preview-size="uploader_preview_size"
          deletable
          style="margin-top: 10%"
        />
        <div class="static">
          <van-button
            round
            block
            type="info"
            native-type="submit"
            @click="upLodaeSign"
            color="linear-gradient(to top, #f77062 0%, #fe5196 100%)"
            >上传签到</van-button
          >
          <van-image width="320" height="231" :src="backgroundImg" />
        </div>
      </div>
    </div>

    <van-overlay :show="photoSignUploading"> 
      <div class="wrapper" @click.stop>
        <van-loading
          color="#0094ff"
          size="80px"
          vertical
          style="margin-top: 50%"
          >识别中...</van-loading
        >
      </div>
    </van-overlay>
  </div>
</template>

<script>
export default {
  props: ["course"],
  data() {
    return {
      backgroundImg: require("@/assets/course/photoSignIn_background.png"),
      date: "",
      imgList: [],
      preview_options: {
        closeable: true,
      },
      photoSignUploading: false,
      uploader_preview_size: "25vh",
    };
  },
  watch: {
    imgList(newVal, oldVal) {
      if (newVal.length == 0) {
        this.uploader_preview_size = "25vh";
      } else if (newVal.length == 1) {
        this.uploader_preview_size = "18vh";
      } else if (newVal.length == 2) {
        this.uploader_preview_size = "13vh";
      }
    },
  },
  created() {
    this.date = localStorage.getItem("date");
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    onRead(file) {
      this.GLOBAL.signFiles.push(file.file);
    },
    upLodaeSign() {
      if (this.imgList.length == 0) {
        this.$toast("请添加图片");
      } else {
        this.photoSignUploading = true;
        let _this = this;
        //_this.photoSignUploading = false;
        //接口
        console.log(this.course.id + " add attendance");

        var axios = require("axios");
        var FormData = require("form-data");
        var data = new FormData();
        for (var i = 0; i < this.GLOBAL.signFiles.length; i++) {
          data.append("img", this.GLOBAL.signFiles[i]);
        }
        data.append("id", this.course.id);

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
            _this.$dialog
              .confirm({
                title: "上传成功",
                message: "点击确认查看签到结果。",
              })
              .then(() => {
                _this.$router.replace("/course");
                _this.$router.push("/Course/teaAttendance");
                _this.$router.push("/course/teaAttendance/attendanceDetail");
              })
              .catch(() => {
                // on cancel
              });
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    },
  },
};
</script>

<style lang='less' scoped>
/* .photoSign {
  width: 300px;
  height: 200px;
} */
.photoSign {
  position: relative;
  top: 2vh;
  left: 0;
  right: 0;
  height: 90%;
  margin: 0 5px;
  background: #fff;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
  border-radius: 20px;
  animation-duration: 0.8s;
  .van-uploader {
    left: 5px;
  }
  .static {
    position: absolute;
    width: 100%;
    bottom: 0;
    .van-button {
      margin-bottom: 0;
      left: 0;
      right: 0;
      margin: 0 auto;
      width: 60%;
      height: 40px;
      border: none;
      margin-bottom: 20%;
    }
    .van-image {
      margin-bottom: 0;
    }
  }
}
</style>