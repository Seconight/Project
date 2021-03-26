<template>
  <div>
    <!-- <van-nav-bar left-text="返回" @click-left="onClickLeft" style="opacity: .5;"/> -->
    <div class="login" v-if="$route.path == '/login'">
      <div class="login-banner">
        <img :src="background" alt contain />
      </div>
      <van-form @submit="onSubmit" class="login-box">
        <h3>登录</h3>
        <div class="_input-group">
          <label><van-icon name="manager" /></label>
          <van-field v-model="username" name="用户名" placeholder="用户名" />
        </div>
        <div class="_input-group">
          <label><van-icon name="lock" /></label>
          <van-field
            v-model="password"
            type="password"
            name="密码"
            placeholder="密码"
          />
        </div>

        <router-link to="/Login/register" style="font-size: 12px"
          >注册</router-link
        >
        <router-link
          to="/Login/retrievePassword"
          style="font-size: 12px; float: right"
          >忘记密码？</router-link
        >
        <van-button round block type="info" native-type="submit"
          >点击登录</van-button
        >
      </van-form>
    </div>
    <transition>
      <router-view></router-view>
    </transition>
  </div>
</template>

<script>
export default {
  name: "login",
  data() {
    return {
      background: require("@/assets/login/login_background.jpg"),
      username: "",
      password: "",
    };
  },
  methods: {
    onSubmit() {
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
              location.replace(document.referrer);
            } else {
              _this.$toast.fail("账号密码错误");
              _this.username = "";
              _this.password = "";
            }
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    },
    onChangePassword() {
      let userInfo = JSON.parse(localStorage.getItem("userInfo"));
      if (userInfo.address == null) {
        this.$toast("邮箱未绑定!,请先绑定邮箱！");
      } else {
        this.$router.push("/User/changePassword");
      }
    },
    onClickLeft() {
      this.$router.replace("/user");
    },
  },
};
</script>

<style lang="less" scoped>
.login {
  position: absolute;
  top: 0;
  width: 100vw;
  height: 100%;
  .login-banner {
    position: relative;
    width: 100%;
    height: 52vh;
    border-radius: 0 0 43px 43px;
    overflow: hidden;
    &::before {
      content: "";
      position: absolute;
      bottom: 0;
      display: block;
      width: 100%;
      height: 50%;
      background: linear-gradient(
        to bottom,
        rgba(0, 0, 0, 0),
        rgba(0, 0, 0, 0.6)
      );
    }
    img {
      width: 100%;
      height: 100%;
    }
  }
  .login-box {
    position: fixed;
    top: 18vh;
    left: 0;
    right: 0;
    margin: 0 26px;
    height: 290px;
    background: linear-gradient(#fffeff 0%, #d7fffe 100%);
    padding: 16px;
    box-sizing: border-box;
    box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
    border-radius: 20px;
    animation-duration: 0.8s;
    & > h3 {
      margin-top: 8px;
      padding-bottom: 10px;
      text-align: center;
      color: #717171;
    }
    ._input-group {
      width: 100%;
      height: 46px;
      display: grid;
      grid-template-columns: 50px 1fr;
      grid-template-rows: 1fr;
      background: #fafafc;
      border-radius: 16px;
      overflow: hidden;
      margin: 20px 0;
      label {
        justify-self: center;
        align-self: center;
        color: #2575fc;
        font-size: 1.125rem;
      }
    }
    .router-link {
      text-align: center;
      font-size: 1rem;
      &:active {
        color: crimson;
      }
    }
    .van-button {
      position: absolute;
      left: 0;
      right: 0;
      bottom: -30px;
      margin: 0 auto;
      width: 86%;
      height: 52px;
      border: none;
      background-image: linear-gradient(-20deg, #fc6076 0%, #ff9a44 100%);
      color: #fff;
      border-radius: 46px;
      letter-spacing: 4px;
      box-shadow: 0 6px 10px rgba(0, 0, 0, 0.2);
      outline: none;
      font-size: 0.875rem;
      transition: all 0.2s;
      &:active {
        background: linear-gradient(to right, #2ce2fa, #1ff5e4);
      }
    }
  }
}
</style>