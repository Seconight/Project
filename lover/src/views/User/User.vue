<template>
  <div>
<<<<<<< HEAD
<<<<<<< Updated upstream
    <div v-if="$route.path == '/user'">
      <van-row>
        <van-col span="6">
          <img :src="avatarSrc" alt="" />
        </van-col>
        <van-col span="10" @click="openLoginModal">{{ name }}</van-col>
        <!-- <van-col span='10' @submit="uploadface"> -->
        <van-col span="8" @click="logout">
          <van-icon name="cross" />
        </van-col>
      </van-row>
      <van-grid :column-num="3" square>
        <van-grid-item icon="label-o" text="个人信息" to="/User/personalInfo" />
        <van-grid-item icon="envelop-o" text="邮箱绑定" to="/User/email" />
        <van-grid-item icon="closed-eye" text="修改密码" to="/User/password" />
        <van-grid-item icon="smile-o" text="人脸上传" to="/User/faceUpload" />
      </van-grid>

=======
=======
>>>>>>> parent of 0cbf737 (Merge branch 'main' of https://github.com/Seconight/Project into main)
    <van-row>
      <van-col span="6">
        <img :src="avatarSrc" alt="" />
      </van-col>
      <van-col span="10" @click="openLoginModal">{{ name }}</van-col>
      <!-- <van-col span='10' @submit="uploadface"> -->
      <van-col span="8" @click="logout">
        <van-icon name="cross" />
      </van-col>
    </van-row>
    <div v-if="role == '老师'">
      <van-cell-group title="个人信息">
        <van-cell title="身份" :value="role" />
        <van-cell title="工号" :value="id" />
        <van-cell title="邮箱" :value="address" />
      </van-cell-group>
    </div>
    <div v-if="role == '学生'">
      <van-cell-group title="个人信息">
        <van-cell title="身份" :value="role" />
        <van-cell title="学号" :value="id" />
        <van-cell title="班级" :value="class_" />
        <van-cell title="邮箱" :value="address" />
      </van-cell-group>
      <van-cell-group title="人脸信息">
        <van-cell
          title="人脸状态"
          :value="checkFace==true ?'人脸已上传':'人脸未上传'"
        />
      </van-cell-group>
      <div style="text-align: center; padding: 20px">
        <van-uploader
          :preview-options="preview_options"
          v-model="imgList"
          multiple
          accept="image/*"
          preview-size="110px"
<<<<<<< HEAD
          :max-count="3"
=======
          :max-count="1"
>>>>>>> parent of 0cbf737 (Merge branch 'main' of https://github.com/Seconight/Project into main)
          :after-read="onRead"
        />
      </div>
      <div style="text-align: center">
        <van-button round type="info" @click="uploadface">上传人脸</van-button>
      </div>
<<<<<<< HEAD
>>>>>>> Stashed changes
=======
>>>>>>> parent of 0cbf737 (Merge branch 'main' of https://github.com/Seconight/Project into main)
    </div>
    <div class="login_modal" v-if="showLoginModal">
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
      <router-link to="/register">注册</router-link>
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
      id: "",
      address: "",
      class_: "",
      checkFace: false,
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
            console.log(response.data.code);
            console.log(response.data.data);
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
        url: this.GLOBAL.port + "/user/checkFace/" + userID,
        headers: {},
      };

      axios(config)
        .then(function (response) {
          localStorage.setItem("checkFace", response.data.data);
        })
        .catch(function (error) {
          console.log(error);
        });
    },
    //组件个人信息显示
    showinfor() {
      let _userInfo = JSON.parse(localStorage.getItem("userInfo"));
      this.role = _userInfo.role;
      this.name = _userInfo.name;
      this.id = _userInfo.id;
      this.address = _userInfo.address;
      if (this.role == "老师") {
        this.avatarSrc = require("@/assets/teacher.jpg");
        this.id;
      }
      if (this.role == "学生") {
        this.avatarSrc = require("@/assets/student.jpg");
        this.class_ = _userInfo.class;
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
      this.$dialog;
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
              'Content-Type' : 'multipart/form-data'
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
    logout() {
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
          localStorage.removeItem("userInfo");
          localStorage.removeItem("checkFace");
          this.name = "点击登录";
          this.avatarSrc = require("@/assets/defualt.jpg");
          this.username = "";
          this.password = "";
          this.showLoginModal = false;
          this.role = "";
          this.id = "";
          this.address = "";
          this.class_ = "";
          this.checkFace = false;
          this.imgList = [];
          this.preview_options.closeable = true;
        })
        .catch(() => {
          // on cancel
        });
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
  z-index: 99;
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