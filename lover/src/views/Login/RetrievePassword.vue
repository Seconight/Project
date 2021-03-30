<template>
  <div>
    <van-nav-bar
      title="找回密码"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      safe-area-inset-top
    />
    <van-steps :active="stepsActive">
      <van-step>输入账号</van-step>
      <van-step>邮箱验证</van-step>
      <van-step>找回密码</van-step>
    </van-steps>
    <div v-if="stepsActive == 0">
      <van-field v-model="id" label="账号" placeholder="请输入账号" clearable />
      <div style="margin: 10px auto; width: 80%">
        <van-button type="primary" block @click="idSubmit">下一步</van-button>
      </div>
    </div>
    <div v-if="stepsActive == 1">
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
        <van-button type="primary" block @click="smsSubmit">下一步</van-button>
      </div>
    </div>

    <div v-if="stepsActive == 2">
      <van-row>
        <van-col span="4"></van-col>
        <van-col span="6">
          <van-icon name="checked" color="#2cc20e" size="80" />
        </van-col>
        <van-col span="0">
          <div style="font-size: 20px; font-weight: 700">找回成功！</div>
        </van-col>
        <!-- <van-col span='10' @submit="uploadface"> -->
      </van-row>
      <div style="text-align: center">账号:{{ id }}</div>
      <div style="text-align: center">密码:{{ password }}</div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      stepsActive: 0,
      id: "",
      sms1: "",
      _sms1: "",
      email: "",
      password: "",
      //控制邮箱验证码按钮
      smsCountdown1: true,
      smsCount1: "",
      smsTimer1: null,
      smsText1: "发送验证码",
    };
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    idSubmit() {
      //通过id获取绑定的邮箱email
      var axios = require("axios");
      const _this = this;
      var config = {
        method: "get",
        url: this.GLOBAL.port+"/user/getEmail?id="+_this.id,
        headers: {},
      };

      axios(config)
        .then(function (response) {
          _this.email = response.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });

      if(this.email == null){
        this.$toast("请联系工作人员！");
      }
      else{
        this.stepsActive = 1;
      }
    },
    smsSubmit() {
      //sms1与_sms1比较
      if (this.sms1 == this._sms1) {
        this.stepsActive = 2;
        const _this = this;
        var axios = require("axios");

        var config = {
          method: "get",
          url: this.GLOBAL.port + "/user/getPassword?id=" + _this.id,
          headers: {},
        };

        axios(config)
          .then(function (response) {
            _this.password = response.data.data;
          })
          .catch(function (error) {
            console.log(error);
          });

      }
      else{
        this.$toast("验证码错误！");
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
</style>