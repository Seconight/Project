<template>
  <div class="aaa">
    <van-nav-bar
      title="人脸上传"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
    />
    <van-notice-bar left-icon="volume-o" text="最多上传三张人脸照片" />
    <div style="text-align: center; padding: 25px">
      <van-uploader
        :preview-options="preview_options"
        v-model="imgList"
        multiple
        accept="image/*"
        preview-size="100px"
        :max-count="3"
        :after-read="onRead"
      />
    </div>
    <div style="text-align: center">
      <van-button round type="info" @click="uploadface">保存修改</van-button>
    </div>
    <van-overlay :show="faceUploading" @click="faceUploading = false">
      <div class="wrapper" @click.stop>
        <van-loading color="#0094ff" size="80px" vertical>人脸上传中...</van-loading>
      </div>
    </van-overlay>
  </div>
</template>

<script>
export default {
  data() {
    return {
      imgList: [],
      preview_options: {
        closeable: true,
      },
      faceUploading: false,
    };
  },
  created() {
    //进入清空之前存入的照片
    this.GLOBAL.faceFile = [];
    var axios = require("axios");

    var config = {
      method: "get",
      url: "http://localhost:8081/user/getFaces?id=0121810880207",
      headers: {},
    };

    axios(config)
      .then(function (response) {
        console.log(JSON.stringify(response.data));
      })
      .catch(function (error) {
        console.log(error);
      });
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    onRead(file) {
      //读入图片到数组
      // let content = file.file;
      // this.GLOBAL.faceFile = content;
      this.GLOBAL.faceFile.push(file.file);
    },
    //上传人脸
    uploadface() {
      this.$dialog;
      this.$dialog
        .confirm({
          title: "确定上传人脸数据",
        })
        .then(() => {
          // on confirm
          //上传人脸数据,上传三张照片
          //uploading=true;
          this.faceUploading = true;
          let userInfo = localStorage.getItem("userInfo");
          let _userInfo = JSON.parse(userInfo);
          var axios = require("axios");
          var FormData = require("form-data");
          var data = new FormData();
          data.append("studentId", _userInfo.id);
          for (var i = 0; i < this.GLOBAL.faceFile.length; i++) {
            data.append("faceImage", this.GLOBAL.faceFile[i]);
          }
          let _this=this;
          var config = {
            method: "post",
            url: this.GLOBAL.port + "/user/faceInfo",
            headers: {
              //...data.getHeaders(),
              //设置请求头
              "Content-Type": "multipart/form-data",
            },
            data: data,
          };
          axios(config)
            .then(function (response) {
              console.log(JSON.stringify(response.data));
              _this.faceUploading=false;
              _this.$toast("人脸上传成功！");
            })
            .catch(function (error) {
              console.log(error);
            });
        })
        .catch(() => {
          // on cancel
        });
    },
  },
};
</script>

<style lang='less' scoped>
.aaa {
  background-color: #fff;
  width: 100%;
  height: 100vh;
}
html,
body {
  height: 100%;
}
.wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}
</style>