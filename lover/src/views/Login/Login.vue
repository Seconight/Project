<template>
  <div style="position: fixed; background: #ecffff; height: 100%; width: 100%">
    <!-- <van-nav-bar left-text="返回" @click-left="onClickLeft" style="opacity: .5;"/> -->
    <div v-if="$route.path == '/login'">
      <div class="login">
        <div style="height: 40px"></div>
        <van-image width="100" height="100" :src="logo" />
        <div style="height: 80px"></div>
        <van-form @submit="onSubmit" style="width: 100%;">
          <van-field class="login_box">
            <template #input>
              <input type="text" v-model="username" required maxlength="20" />
              <label>用户名</label>
            </template>
          </van-field>
          <van-field class="login_box">
            <template #input>
              <input
                type="password"
                v-model="password"
                required
                maxlength="20"
              />
              <label>密码</label>
              <div @click.self="onChangePassword">忘记密码？</div>
            </template>
          </van-field>
          <van-button
            style="height: 35px; top: 5px"
            block
            type="info"
            native-type="submit"

            >点击登录</van-button
          >
        </van-form>
        <router-link
          to="/Login/register"
          style="top: 10px; font-size: 12px; color: blue"
        >
          新用户注册
        </router-link>
      </div>
    </div>

    <!-- <div style="text-align: center">
        <van-image width="200" height="200" :src="logo" />
      </div> -->
    <!-- <van-form @submit="onSubmit" class="login-box">
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
      </van-form> -->
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
      logo: require("@/assets/login/logo.png"),
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
              _this.$toast.success("登录成功");
              location.replace(document.referrer);
            } else {
              _this.$toast(JSON.stringify(response.data.data));
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
      this.$router.push("/Login/retrievePassword");
    },
  },
};
</script>

<style lang="less" scoped>
.login-box {
  position: fixed;
  top: 28vh;
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
.login {
  /* 弹性布局 让子元素称为弹性项目 */
  display: flex;
  /* 让弹性项目垂直排列 原理是改变弹性盒子的主轴方向 父元素就是弹性盒子 现在改变后的主轴方向是向下了 */
  flex-direction: column;
  /* 让弹性项目在交叉轴方向水平居中 现在主轴的方向是向下 交叉轴的方向是与主轴垂直 交叉轴的方向是向右 */
  align-items: center;
  padding: 20px;
}
.login h2 {
  color: #000;
  margin-bottom: 30px;
}
.login .login_box {
  /* 相对定位 */
  background-color: #ecffff;
  position: relative;
  width: 100%;
}
.login .login_box input {
  /* 清除input框自带的边框和轮廓 */
  outline: none;
  border: none;
  width: 100%;
  padding: 10px 0;
  margin-bottom: 30px;
  color: #000;
  font-size: 16px;
  border-bottom: 1px solid #000;
  /* 背景颜色为透明色 */
  background-color: transparent;
}
.login .login_box label {
  position: absolute;
  top: 0;
  left: 0;
  padding: 10px 0;
  color: #000;
  /* 这个属性的默认值是auto 默认是这个元素可以被点击 但是如果我们写了none 就是这个元素不能被点击 , 就好像它可见但是不能用 可望而不可即 */
  /* 这个就是两者的区别 */
  pointer-events: none;
  /* 加个过渡 */
  transition: all 0.5s;
}
.login .login_box div {
  position: absolute;
  top: 0;
  right: 0;
  padding: 10px 0;
  font-size: 12px;
  color: blue;
  /* 这个属性的默认值是auto 默认是这个元素可以被点击 但是如果我们写了none 就是这个元素不能被点击 , 就好像它可见但是不能用 可望而不可即 */
  /* 这个就是两者的区别 */

  /* 加个过渡 */
  transition: all 0.5s;
}
/* :focus 选择器是当input获得焦点是触发的样式 + 是相邻兄弟选择器 去找与input相邻的兄弟label */
/* :valid 选择器是判断input框的内容是否合法,如果合法会执行下面的属性代码,不合法就不会执行,我们刚开始写布局的时候给input框写了required 我们删掉看对比 当没有required的话input框的值就会被认为一直合法,所以一直都是下方的样式 ,但是密码不会,密码框内的值为空,那么这句话局不合法,required不能为空 当我们给密码框写点东西的时候才会执行以下代码*/
.login .login_box input:focus + label,
.login .login_box input:valid + label {
  top: -25px;
  color: #03e9f4;
  font-size: 16px;
}

.login a {
  overflow: hidden;
  position: relative;
  padding: 10px 20px;
  color: #03e9f4;
  /* 取消a表现原有的下划线 */
  text-decoration: none;
  /* 同样加个过渡 */
  transition: all 0.5s;
}
</style>