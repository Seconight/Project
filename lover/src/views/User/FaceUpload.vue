<template>
  <div class="body">
    <van-nav-bar
      title="人脸上传"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      safe-area-inset-top
    />
    <div class="faceupload">
      <div v-if="faceDownloading == true">
        <van-loading style="margin-top: 50%" size="50px" color="#0094ff"
          >加载中...</van-loading
        >
      </div>
      <div v-if="faceDownloading != true">
        <van-notice-bar left-icon="volume-o" text="最多上传三张人脸照片" />
        <div class="uploader">
          <van-uploader
            :preview-options="preview_options"
            v-model="imgList"
            multiple
            accept="image/*"
            :preview-size="uploader_preview_size"
            :max-count="3"
            :after-read="onRead"
            deletable
            style="margin-top: 10%"
          />
        </div>
      </div>
      <div class="static">
        <van-button
          round
          block
          type="info"
          native-type="submit"
          @click="uploadface"
          color="linear-gradient(to top, #f77062 0%, #fe5196 100%)"
          >上传</van-button
        >
        <van-image width="300" height="155" :src="backgroundImg" />
      </div>
    </div>

    <van-overlay :show="faceUploading">
      <div class="wrapper" @click.stop>
        <van-loading
          color="#0094ff"
          size="80px"
          vertical
          style="margin-top: 50%"
          >人脸上传中...</van-loading
        >
      </div>
    </van-overlay>
  </div>
</template>

<script>
export default {
  data() {
    return {
      backgroundImg: require("@/assets/user/faceUpload_background.png"),
      imgList: [],
      preview_options: {
        closeable: true,
      },
      faceUploading: false,
      faceDownloading: true,
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
    //进入清空之前存入的照片
    this.GLOBAL.faceFile = [];
    var axios = require("axios");
    let userInfo = localStorage.getItem("userInfo");
    let _userInfo = JSON.parse(userInfo);
    let _this = this;
    var config = {
      method: "get",
      url: this.GLOBAL.port + "/user/getFaces?id=" + _userInfo.id,
      headers: {},
    };
    console.log(config.url);
    axios(config)
      .then(function (response) {
        //console.log(JSON.stringify(response.data));
        //console.log(response.data);
        for (let i = 0; i < response.data.length; i++) {
          _this.imgList.push({
            url: "data:image/jpg;base64," + response.data[i],
            isImage: true,
          });
        }
        // _this.imgList.push({ url: "https://img01.yzcdn.cn/vant/leaf.jpg" });
        // _this.imgList.push({ url: "https://img01.yzcdn.cn/vant/leaf.jpg" });
        _this.faceDownloading = false;
      })
      .catch(function (error) {
        console.log(error);
      });
  },
  methods: {
    onClickLeft() {
      console.log(this.imgList);
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
          let _this = this;
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
              _this.faceUploading = false;
              _this.$dialog
                .alert({
                  message: "人脸上传成功！",
                  theme: "round-button",
                })
                .then(() => {
                  // on close
                });
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
.faceupload {
  position: relative;
  text-align: center;
  top: 3vh;
  left: 0;
  right: 0;
  height: 80%;
  margin: 0 5px;
  background: #fff;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
  border-radius: 20px;
  animation-duration: 0.8s;
  .uploader {
    margin-top: 10px;
    margin-left: 10px;
  }
  .static {
    position: absolute;
    width: 100%;
    bottom: 0;
    .van-button {
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
