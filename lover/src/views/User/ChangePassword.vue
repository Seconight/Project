<template>
  <div class="body">
    <van-nav-bar
      title="修改密码"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      safe-area-inset-top
    />
    <div class="changPassword">
            <van-steps :active="stepsActive">
        <van-step>验证身份</van-step>
        <van-step>修改密码</van-step>
        <van-step>修改成功</van-step>
      </van-steps>
      <div v-if="stepsActive == 0">
        <van-cell-group>
          <van-field label="已绑定邮箱" :value="email" disabled />
          <van-field
            v-model="sms1"
            center
            clearable
            label="验证码"
            placeholder="请输入邮箱验证码"
          >
            <template #button>
              <!-- <van-button size="small" type="primary">发送验证码</van-button> -->
              <van-button
                size="small"
                type="primary"
                :disabled="!smsCountdown1"
                @click="sendsms1"
              >
                <span v-if="smsCountdown1">{{ smsText1 }}</span>
                <span v-if="!smsCountdown1"
                  >{{ smsText1 }} ({{ smsCount1 }})</span
                >
              </van-button>
            </template>
          </van-field>
        </van-cell-group>
        <div style="margin: 10px auto; width: 80%">
          <van-button type="primary" block @click="smsSubmit"
            >下一步</van-button
          >
        </div>
      </div>
      <div v-if="stepsActive == 1">
        <van-field
          v-model="password"
          type="password"
          label="密码"
          placeholder="请输入密码"
          clearable
        />
        <van-field
          v-model="password_"
          type="password"
          label="密码"
          placeholder="请再次输入密码"
          clearable
        />
        <div style="margin: 10px auto; width: 80%">
          <van-button type="primary" block @click="passwordSubmit"
            >下一步</van-button
          >
        </div>
      </div>
      <div v-if="stepsActive == 2">
        <van-row>
          <van-col span="4"></van-col>
          <van-col span="6">
            <van-icon name="checked" color="#2cc20e" size="80" />
          </van-col>
          <van-col span="0">
            <div style="font-size: 20px; font-weight: 700">修改密码成功!</div>
          </van-col>
          <!-- <van-col span='10' @submit="uploadface"> -->
        </van-row>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      stepsActive: 0,
      sms1: "",
      _sms1: "",
      email: "",
      password: "",
      password_: "",
      //控制旧邮箱验证码按钮
      smsCountdown1: true,
      smsCount1: "",
      smsTimer1: null,
      smsText1: "发送验证码",
      succeed: false,
    };
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    this.email = userInfo.address;
  },
  methods: {
    onClickLeft() {
      if (this.succeed == false) {
        this.$router.go(-1);
      } else {
        this.$router.push("/login");
      }
    },
    smsSubmit() {
      //sms1与_sms1比较
      if (this.sms1 != "") {
        if (this.sms1 == this._sms1) {
          this.stepsActive = 1;
        } else {
          this.$toast("验证码输入错误！");
        }
      } else {
        this.$toast("请输入验证码！");
      }
    },
    passwordSubmit() {
      if (this.password_ == this.password) {
        //修改密码
        let userInfo = JSON.parse(localStorage.getItem("userInfo"));
        var currentId = userInfo.id;
        //console.log("change user: "+currentId);
        var axios = require("axios");
        var config = {
          method: "post",
          url:
            this.GLOBAL.port +
            "/user/changePassword?id=" +
            currentId +
            "&oldPassword=" +
            userInfo.password +
            "&newPassword=" +
            this.password,
          headers: {},
        };
        let _this = this;
        axios(config)
          .then(function (response) {
            //console.log(JSON.stringify(response.data));
            _this.$toast("修改密码成功！请重新登录。");
            localStorage.removeItem("userInfo");
            localStorage.removeItem("checkFace");
            _this.succeed = true;
          })
          .catch(function (error) {
            console.log(error);
          });
        this.stepsActive = 2;
      } else {
        this.$toast("两次密码输入不一致，请再次输入");
      }
    },
    sendsms1() {
      //验证码发送接口,向oldEmail发送,用_sms1接收

      var axios = require("axios");
      const _this = this;
      var config = {
        method: "get",
        url: this.GLOBAL.port + "/getCheckCode?email=" + _this.email,
        headers: {},
      };

      axios(config)
        .then(function (response) {
          //获取验证码
          _this._sms1 = JSON.stringify(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });

      this.$toast("发送成功");

      const TIME_COUNT = 60;
      if (!this.smsTimer1) {
        this.smsCount1 = TIME_COUNT;
        this.smsCountdown1 = false;
        this.smsText1 = "重新发送";
        this.smsTimer1 = setInterval(() => {
          if (this.smsCount1 > 0 && this.smsCount1 <= TIME_COUNT) {
            this.smsCount1--;
          } else {
            this.smsCountdown1 = true;

            clearInterval(this.smsTimer1);
            this.smsTimer1 = null;
          }
        }, 1000);
      }
    },
  },
};
</script>

<style lang='less' scoped>

.van-row {
  height: 100px;
  padding: 10px;
  .van-col {
    height: 100px;
    line-height: 100px;
    font-size: 20px;
    text-align: center;
  }
}

.changPassword {
  position: relative;
  top: 3vh;
  left: 0;
  right: 0;
  margin: 0 5px;
  background: #fff;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
  border-radius: 20px;
  animation-duration: 0.8s;
}
</style>