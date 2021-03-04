<template>
  <div>
    <div v-if="$route.path == '/user'">
      <van-row>
        <van-col span="6">
          <img :src="avatarSrc" alt="" />
        </van-col>
        <van-col span="10" @click="openLoginModal">{{ name }}</van-col>
        <!-- <van-col span='10' @submit="uploadface"> -->
        <van-col span="8" @click="onlogout">
          <van-icon name="cross" />
        </van-col>
      </van-row>
      <van-grid :column-num="3" square v-if="role != ''">
        <van-grid-item icon="label-o" text="个人信息" to="/User/personalInfo" />
        <van-grid-item icon="envelop-o" text="邮箱设置" to="/User/email" />
        <van-grid-item
          icon="closed-eye"
          text="修改密码"
          @click="onChangePassword"
        />
        <van-grid-item
          v-if="role == '学生'"
          icon="smile-o"
          text="人脸上传"
          to="/User/faceUpload"
        />
      </van-grid>
    </div>
    <transition>
      <router-view @logout="logout"></router-view>
    </transition>
    <div class="login_modal" v-if="showLoginModal && $route.path == '/user'">
      <section @click="closeLoginModal"></section>
      <van-form @submit="onSubmit">
        <!-- <div class="radio">
          <van-radio-group
            v-model="identity"
            direction="horizontal"
            icon-size="30px"
          >
            <van-radio name="1">老师</van-radio>
            <van-radio name="2">学生</van-radio>
          </van-radio-group>
        </div> -->
        <van-field
          v-model="username"
          name="用户名"
          label="用户名"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
          v-model="password"
          type="password"
          name="密码"
          label="密码"
          placeholder="密码"
          :rules="[{ required: true, message: '请填写密码' }]"
        />
        <router-link
          to="/User/register"
          @click="onRegister"
          style="font-size: 12px"
          >注册</router-link
        >
        <router-link
          to="/User/retrievePassword"
          @click="onRegister"
          style="font-size: 12px; float: right"
          >忘记密码？</router-link
        >
        <div style="margin: 16px">
          <van-button round block type="info" native-type="submit"
            >登录</van-button
          >
        </div>
      </van-form>
    </div>
  </div>
</template>

<script>
//var axios = require('axios');
export default {
  data() {
    return {
      name: "点击登录",
      avatarSrc: require("@/assets/defualt.jpg"),
      //identity: '1',
      username: "",
      password: "",
      showLoginModal: false,
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
    }
  },

  methods: {
    onRead(file) {
      let content = file.file;
      this.GLOBAL.faceFile = content;
    },

    onSubmit(values) {
      this.username = values["用户名"];
      this.password = values["密码"];
      //登录逻辑 从接口判断登录是否成功
      //假数据
      //if (this.username == "123" && this.password == "123") {
      if (this.username != "" && this.password != "") {
        var axios = require("axios");
        var data = JSON.stringify({ id: this.username, pwd: this.password });

        var config = {
          method: "post",
          url: this.GLOBAL.port + "/user/login",
          headers: {
            "Content-Type": "application/json",
          },
          data: data,
        };
        let _this = this;
        axios(config)
          .then(function (response) {
            console.log(response);
            if (response.data.code == 1) {
              //添加用户信息到localStorage
              localStorage.setItem(
                "userInfo",
                JSON.stringify(response.data.data)
              );
              //获取人脸是否注册
              _this.axiosCheckFace(response.data.data.id);
              //console.log("this is "+_this.checkFace);
              _this.$toast.success("登录成功");
              _this.showinfor(); //更新组件信息显示
              _this.closeLoginModal();
            } else {
              //账户密码错误
              _this.$toast.fail("账号密码错误");
              _this.username = "";
              _this.password = "";
            }
          })
          .catch(function (error) {
            console.log(error);
          });
        //登录成功
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
    //组件个人信息显示
    showinfor() {
      let _userInfo = JSON.parse(localStorage.getItem("userInfo"));
      this.name = _userInfo.name;
      this.role = _userInfo.role;
      if (this.role == "老师") {
        this.avatarSrc = require("@/assets/teacher.jpg");
      }
      if (this.role == "学生") {
        this.avatarSrc = require("@/assets/student.jpg");
        this.checkFace = localStorage.getItem("checkFace");
      }
    },
    //打开登录模态框
    openLoginModal() {
      if (!localStorage.getItem("userInfo")) {
        this.showLoginModal = true;
      }
    },
    //关闭登录模块框
    closeLoginModal() {
      this.showLoginModal = false;
    },
    //上传人脸
    uploadface() {
      this.$dialog
        .confirm({
          title: "确定上传人脸数据",
        })
        .then(() => {
          // on confirm
          //上传人脸数据,只能上传一张照片
          var axios = require("axios");
          var FormData = require("form-data");
          var data = new FormData();
          data.append("faceImage", this.GLOBAL.faceFile);
          //console.log("this is "+this.GLOBAL.faceFile);
          data.append("studentId", this.id);

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
            })
            .catch(function (error) {
              console.log(error);
            });

          this.$toast.success("上传成功");
        })
        .catch(() => {
          // on cancel
        });
    },
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
    logout(){
          localStorage.removeItem("userInfo");
          localStorage.removeItem("checkFace");
          this.name = "点击登录";
          this.avatarSrc = require("@/assets/defualt.jpg");
          this.username = "";
          this.password = "";
          this.showLoginModal = false;
          this.role = "";
          this.imgList = [];
          this.preview_options.closeable = true;
    },
    onRegister() {
      this.showLoginModal = false;
    },
    onChangePassword() {
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      if (userInfo.address == null) {
        this.$toast("邮箱未绑定!,请先绑定邮箱！");
      } else {
        this.$router.push("/User/changePassword");
      }
    },
  },
};
</script>

<style lang='less' scoped>
.van-row {
  height: 100px;
  padding: 10px;
  background: linear-gradient(to right, #ff0000, #000000);
  color: #fff;
  .van-col {
    height: 100px;
    line-height: 100px;
    font-size: 20px;
    text-align: center;
    img {
      width: 100px;
      border-radius: 50%;
    }
    &:last-child {
      text-align: right;
    }
  }
}
.login_modal {
  width: 100%;
  height: 100vh;
  position: absolute;
  left: 0;
  top: 0;
  section {
    width: 100%;
    height: 100vh;
    background: rgba(0, 0, 0, 0.5);
  }
  .van-form {
    width: 90%;
    position: absolute;
    top: 30vh;
    left: 5%;
    padding: 10px;
    box-sizing: border-box;
    background: #fff;
  }
}
</style>