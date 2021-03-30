<template>
  <div>
    <div class="usercenter-banner">
      <!-- <ImgLoader class="user-bg" :src="profile.background" v-if="loadflag" /> -->
      <van-cell title="路由跳转" is-link to="index" />
      <div class="_userinfo" @click="onlogout">
        <div class="_userinfo-avatar">
          <img :src="assert.avatarSrc" alt="" />
        </div>
        <div class="_userinfo-name">{{ name }}</div>
      </div>
      <van-grid :column-num="3" square v-if="role != ''" class="menu">
        <van-grid-item text="个人信息" to="/User/personalInfo">
          <template #icon>
            <van-icon :name="assert.gridImg[0]" size="40px" />
          </template>
        </van-grid-item>
        <van-grid-item icon="envelop-o" text="邮箱设置" to="/User/email">
          <template #icon>
            <van-icon :name="assert.gridImg[1]" size="40px" />
          </template>
        </van-grid-item>
        <van-grid-item
          icon="closed-eye"
          text="修改密码"
          @click="onChangePassword"
        >
          <template #icon>
            <van-icon :name="assert.gridImg[2]" size="40px" />
          </template>
        </van-grid-item>
        <van-grid-item
          v-if="role == '学生'"
          icon="smile-o"
          text="人脸上传"
          to="/User/faceUpload"
        >
          <template #icon>
            <van-icon :name="assert.gridImg[3]" size="40px" />
          </template>
        </van-grid-item>
        <van-grid-item icon="label-o" text="测试" to="/test" />
      </van-grid>
    </div>
    <transition enter-active-class="pt-page-scaleUp">
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
        gridImg: [
          require("@/assets/icon/gerenxinxi.png"),
          require("@/assets/icon/youxiangshezhi.png"),
          require("@/assets/icon/xiugaimima.png"),
          require("@/assets/icon/renlainshangchuan.png"),
        ],
      },

      //identity: '1',
      role: "",
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
    onRead(file) {
      let content = file.file;
      this.GLOBAL.faceFile = content;
    },
    //组件个人信息显示
    showinfor() {
      let _userInfo = JSON.parse(localStorage.getItem("userInfo"));
      this.name = _userInfo.name;
      this.role = _userInfo.role;
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
    onlogout() {
      this.$dialog
        .confirm({
          title: "确定要退出登录吗？",
        })
        .then(() => {
          // on confirm
          if (!localStorage.getItem("userInfo")) {
            this.$toast.fail("请先登录");
            return;
          }
          this.logout();
        })
        .catch(() => {
          // on cancel
        });
    },
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
  height: 30vh;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.5);
  border-radius: 0 0 36px 36px;
  animation-duration: 0.8s;
background-image: linear-gradient(to bottom,#5D9FFF 0%, #B8DCFF 48%, #6BBBFF 100%);

  .user-bg,
  & > img {
    position: absolute;
    width: 100%;
    height: 100%;
    border-radius: 0 0 36px 36px;
    overflow: hidden;
    z-index: 1;
  }
  //   .df-btn {
  //     position: absolute;
  //     left: 0;
  //     right: 0;
  //     margin: auto;
  //     bottom: -22px;
  //     display: block;
  //     width: 140px;
  //     height: 38px;
  //     border-radius: 44px;
  //     background: crimson;
  //     color: #fff;
  //     border: none;
  //     font-size: 0.875rem;
  //     z-index: 3;
  //   }
  ._userinfo {
    position: absolute;
    left: 0;
    right: 0;
    top: 60px;
    margin: auto;
    bottom: 100px;
    width: 100px;
    text-align: center;
    z-index: 3;
    
    ._userinfo-avatar {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      overflow: hidden;
      img {
        left: auto;
        width: 100%;
        height: 100%;
      }
    }
    ._userinfo-name {
      padding: 10px;
      font-size: 1.125rem;
      font-weight: 600;
      color: #000;
    }
  }
}
.grid {
  position: absolute;
  width: 100%;
  top: 40vh;
}
.menu {
  position: fixed;
  top: 35vh;
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
