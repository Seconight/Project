<template>
  <div class="body">
      <div class="usercenter-banner">
        <div class="_userinfo-avatar">
          <img :src="assert.avatarSrc" alt="" @click="onclickPersenInfor" />
        </div>
        <div class="_userinfo-name">{{ name }}</div>
        <div class="_userinfo-id" v-if="this.role == '老师'">
          职工号:{{ id }}
        </div>
        <div class="_userinfo-id" v-if="this.role == '学生'">学号:{{ id }}</div>
      </div>
      <van-grid column-num="3" square v-if="role != ''" class="menu">
        <van-grid-item icon="envelop-o" text="邮箱设置" to="/User/email">
          <template #icon>
            <van-icon :name="assert.gridImg.email" size="40px" />
          </template>
        </van-grid-item>
        <van-grid-item
          icon="closed-eye"
          text="修改密码"
          @click="onChangePassword"
        >
          <template #icon>
            <van-icon :name="assert.gridImg.changePassword" size="40px" />
          </template>
        </van-grid-item>
        <van-grid-item
          v-if="role == '学生'"
          icon="smile-o"
          text="人脸上传"
          to="/User/faceUpload"
        >
          <template #icon>
            <van-icon :name="assert.gridImg.faceUpload" size="40px" />
          </template>
        </van-grid-item>
      </van-grid>
      <transition
        name="van-slide-right"
      >
        <router-view @logout="logout"></router-view>
      </transition>
    
  </div>
</template>

<script>
//var axios = require('axios');
export default {
  data() {
    return {
      name: "",
      assert: {
        avatarSrc: require("@/assets/user/defualt.jpg"),
        gridImg: {
          email: require("@/assets/icon/youxiangshezhi.png"),
          changePassword: require("@/assets/icon/xiugaimima.png"),
          faceUpload: require("@/assets/icon/renlainshangchuan.png"),
        },
      },
      //identity: '1',
      role: "",
      id: "",
      imgList: [],
      preview_options: {
        closeable: true,
      },
    };
  },
  created() {
    //进入user页面判断是否登录
    let userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
      //存在已登录信息
      this.showinfor(); //更新组件信息显示
      this.$toast.success("登录成功");
    } else {
      this.$router.push("/login");
    }
  },

  methods: {
    onclickPersenInfor() {
      this.$router.push("/User/personalInfo");
    },
    onRead(file) {
      let content = file.file;
      this.GLOBAL.faceFile = content;
    },
    //组件个人信息显示
    showinfor() {
      let _userInfo = JSON.parse(localStorage.getItem("userInfo"));
      this.name = _userInfo.name;
      this.role = _userInfo.role;
      this.id = _userInfo.id;
      if (this.role == "老师") {
        this.assert.avatarSrc = require("@/assets/user/teacher.jpg");
      }
      if (this.role == "学生") {
        this.assert.avatarSrc = require("@/assets/user/student.jpg");
        //判断人脸是否注册
        this.axiosCheckFace(_userInfo.id);
      }
    },
    //上传人脸
    // uploadface() {
    //   this.$dialog
    //     .confirm({
    //       title: "确定上传人脸数据",
    //     })
    //     .then(() => {
    //       // on confirm
    //       //上传人脸数据,只能上传一张照片
    //       var axios = require("axios");
    //       var FormData = require("form-data");
    //       var data = new FormData();
    //       data.append("faceImage", this.GLOBAL.faceFile);
    //       //console.log("this is "+this.GLOBAL.faceFile);
    //       data.append("studentId", this.id);

    //       var config = {
    //         method: "post",
    //         url: this.GLOBAL.port + "/user/faceInfo",
    //         headers: {
    //           //...data.getHeaders(),
    //           //设置请求头
    //           "Content-Type": "multipart/form-data",
    //         },
    //         data: data,
    //       };

    //       axios(config)
    //         .then(function (response) {
    //           console.log(JSON.stringify(response.data));
    //         })
    //         .catch(function (error) {
    //           console.log(error);
    //         });

    //       this.$toast.success("上传成功");
    //     })
    //     .catch(() => {
    //       // on cancel
    //     });
    // },
    //登录

    //退出登录事件

    logout() {
      localStorage.removeItem("userInfo");
      localStorage.removeItem("checkFace");
      this.name = "";
      this.avatarSrc = require("@/assets/defualt.jpg");
      this.username = "";
      this.password = "";
      this.showLoginModal = false;
      this.role = "";
      this.imgList = [];
      this.preview_options.closeable = true;
      this.$router.push("/login");
    },
    onChangePassword() {
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      if (userInfo.address == null) {
        this.$toast("邮箱未绑定!,请先绑定邮箱！");
      } else {
        this.$router.push("/User/changePassword");
      }
    },
    axiosCheckFace(userID) {
      var axios = require("axios");
      var config = {
        method: "get",
        url: this.GLOBAL.port + "/user/checkFace?id=" + userID,
        headers: {},
      };
      axios(config)
        .then(function (response) {
          // console.log(response.data.data)
          localStorage.setItem("checkFace", response.data.data);
          // var current = localStorage.getItem("checkFace");
          // console.log(current);
        })
        .catch(function (error) {
          console.log(error);
        });
    },
  },
};
</script>
<style lang="less" scoped>

.usercenter-banner {
  position: relative;
  width: 100%;
  height: 15vh;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.5);
  border-radius: 0 0 20px 20px;
  animation-duration: 0.8s;
  background-image: linear-gradient(#a1c4fd 0%, #c2e9fb 100%);
  ._userinfo-avatar {
    position: relative;
    top: 2vh;
    left: 2vh;
    width: 80px;
    height: 80px;
    border-radius: 50%;
    overflow: hidden;
    img {
      left: auto;
      width: 100%;
      height: 100%;
    }
    // ._userinfo-name {
    //   padding: 10px;
    //   font-size: 1.125rem;
    //   font-weight: 600;
    //   color: #000;
    // }
  }
  ._userinfo-name {
    position: absolute;
    top: 4vh;
    left: 15vh;
    font-size: 1.5rem;
    font-weight: 600;
    color: #000;
  }
  ._userinfo-id {
    position: absolute;
    top: 8vh;
    left: 15vh;
    font-size: 0.8rem;
    font-weight: 400;
    color: #000;
  }
}
.menu {
  position: fixed;
  top: 20vh;
  left: 0;
  right: 0;
  margin: 0 26px;
  background: #fff;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
  border-radius: 20px;
  animation-duration: 0.8s;
}
</style>
