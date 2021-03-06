<template>
  <div class="aaa">
    <van-nav-bar
      title="人脸上传"
      left-text="返回"
      left-arrow
      :right-text="edit?'取消':'编辑'"
      @click-left="onClickLeft"
      @click-right="onClickRight"
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
        :disabled="!edit"
        :deletable="edit"
      />
    </div>
    <div v-if="edit" style="text-align: center">
      <van-button round type="info" @click="uploadface">保存修改</van-button>
    </div>
    <van-overlay :show="faceUploading">
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
      edit: false,
    };
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
      url: this.GLOBAL.port+"/user/getFaces?id="+_userInfo.id,
      headers: {},
    };
    console.log(config.url);
    axios(config)
      .then(function (response) {
        //console.log(JSON.stringify(response.data));
        //console.log(response.data);
        for(let i=0;i<response.data.length;i++){
          _this.imgList.push({url:("data:image/jpg;base64,"+response.data[i]), isImage:true});
        }
      })
      .catch(function (error) {
        console.log(error);
      });
  },
  methods: {
    onClickRight(){
      this.edit=!this.edit;
      this.GLOBAL.faceFile = [];
      this.imgList= [];
    },
    onClickLeft() {
      console.log(this.imgList)
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
