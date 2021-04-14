<template>
  <div class="body">
    <van-nav-bar
      title="邮箱设置"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      safe-area-inset-top
    />
    <div class="emil">
      <van-steps :active="stepsActive">
        <van-step>选择验证方式</van-step>
        <van-step>验证身份</van-step>
        <van-step>绑定邮箱</van-step>
        <van-step>绑定成功</van-step>
      </van-steps>
      <div v-if="stepsActive == 0">
        <van-cell-group :border="false">
          <van-cell
            center
            title="通过密码验证"
            value="验证身份"
            label="使用账号密码"
            is-link
            @click="onChoicePassword"
          />
          <van-cell
            center
            title="通过邮箱验证"
            value="验证身份"
            label="向已绑定邮箱发送验证码"
            is-link
            @click="onChoiceEmail"
          />
        </van-cell-group>
      </div>
      <div v-if="stepsActive == 1">
        <div v-if="identityMethod == 'Password'">
          <van-field
            v-model="password"
            type="password"
            label="密码"
            placeholder="请输入密码"
          />
        </div>
        <div v-if="identityMethod == 'Email'">
          <van-cell-group :border="false">
            <van-field label="已绑定邮箱" :value="oldEmail" disabled />
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
        </div>
        <div style="margin: 10px auto; width: 80%">
          <van-button type="primary" block @click="identitySubmit"
            >下一步</van-button
          >
        </div>
      </div>
      <div v-if="stepsActive == 2">
        <van-form @submit="newEmailSubmit">
          <van-field
            v-model="newEmail"
            label="新邮箱"
            placeholder="请输入新邮箱地址"
            :rules="[
              {
                pattern: /^$|^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/,
                message: '请填写正确邮箱格式',
              },
            ]"
          />
          <van-field
            v-model="sms2"
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
                :disabled="!smsCountdown2"
                @click="sendsms2"
              >
                <span v-if="smsCountdown2">{{ smsText2 }}</span>
                <span v-if="!smsCountdown2"
                  >{{ smsText2 }} ({{ smsCount2 }})</span
                >
              </van-button>
            </template>
          </van-field>
          <div style="margin: 10px auto; width: 80%">
            <van-button type="primary" block @click="addAddress"
              >下一步</van-button
            >
          </div>
        </van-form>
      </div>
      <div v-if="stepsActive == 3">
        <van-row>
          <van-col span="4"></van-col>
          <van-col span="6">
            <van-icon name="checked" color="#2cc20e" size="80" />
          </van-col>
          <van-col span="0">
            <div style="font-size: 20px; font-weight: 700">邮箱绑定成功!</div>
          </van-col>
          <!-- <van-col span='10' @submit="uploadface"> -->
        </van-row>
        <div style="text-align: center">已绑定邮箱账号:{{ newEmail }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      stepsActive: 0,
      identityMethod: "",
      sms1: "",
      _sms1: "",
      sms2: "",
      _sms2: "",
      oldEmail: "",
      newEmail: "",
      password: "",
      _password: "",
      //控制旧邮箱验证码按钮
      smsCountdown1: true,
      smsCount1: "",
      smsTimer1: null,
      smsText1: "发送验证码",
      //控制新邮箱验证码按钮
      smsCountdown2: true,
      smsCount2: "",
      smsTimer2: null,
      smsText2: "发送验证码",
    };
  },
  created() {
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    this.oldEmail = userInfo.address;
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    onChoicePassword() {
      this.stepsActive = 1;
      this.identityMethod = "Password";
    },
    onChoiceEmail() {
      if (this.oldEmail == ""||this.oldEmail == null) {
        this.$dialog({ message: "第一次绑定邮箱请使用密码验证身份。" });
      } else {
        this.stepsActive = 1;
        this.identityMethod = "Email";
      }
    },
    identitySubmit() {
      if (this.identityMethod == "Password") {
        let userInfo = JSON.parse(localStorage.getItem("userInfo"));
        var _password;
        const _this = this;
        //调用接口获取密码  _password
        var axios = require("axios");
        var config = {
          method: "get",
          url: this.GLOBAL.port + "/user/getPassword?id=" + userInfo.id,
          headers: {},
        };
        axios(config)
          .then(function (response) {
            //console.log(response.data);
            if (response.data.code == 1) {
              _password = response.data.data;
              //password与_password比较
              if (_this.password == _password) {
                _this.stepsActive = 2;
              } else {
                _this.$toast("密码错误！");
              }
            } else {
              _this.$toast("网络错误！");
            }
          })
          .catch(function (error) {
            console.log(error);
          });
      } else {
        //sms1与_sms1比较
        if (this.sms1 == this._sms1 && this.sms1 != null) {
          this.stepsActive = 2;
        } else {
          this.$toast("验证码错误！");
        }
      }
    },
    newEmailSubmit() {
      if (this.sms2 == this._sms2) {
        var axios = require("axios");
        let userInfo = JSON.parse(localStorage.getItem("userInfo"));
        var config = {
          method: "post",
          url:
            this.GLOBAL.port +
            "/user/addAddress?id=" +
            userInfo.id +
            "&address=" +
            this.newEmail,
          headers: {},
        };
        const _this = this;
        axios(config)
          .then(function (response) {
            if (response.data.code == 1) {
              _this.stepsActive = 3;
              userInfo.address = _this.newEmail;
              //重新设置 localStorage
              localStorage.removeItem("userInfo");
              localStorage.setItem("userInfo", JSON.stringify(userInfo));
            } else {
              _this.$toast("网络错误！");
            }
          })
          .catch(function (error) {
            console.log(error);
          });
      } else {
        this.$toast("验证码错误！");
      }
    },
    emailTest(emil) {
      if (emil != "") {
        let reg = /^$|^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
        return reg.test(emil);
      } else return false;
    },
    sendsms1() {
      //判断邮箱是否满足格式
      if (!this.emailTest(this.oldEmail)) {
        this.$toast("请输入正确邮箱格式！");
        return;
      }
      //验证码发送接口,向oldEmail发送,用_sms1接收
      var axios = require("axios");
      const _this = this;
      var config = {
        method: "get",
        url: this.GLOBAL.port + "/getCheckCode?email=" + _this.oldEmail,
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
    sendsms2() {
      //验证码发送接口,向newEmail发送,用_sms2接收
      //判断邮箱是否满足格式
      if (!this.emailTest(this.newEmail)) {
        this.$toast("请输入正确邮箱格式！");
        return;
      }

      var axios = require("axios");
      const _this = this;
      var config = {
        method: "get",
        url: this.GLOBAL.port + "/getCheckCode?email=" + _this.newEmail,
        headers: {},
      };

      axios(config)
        .then(function (response) {
          //获取验证码
          _this._sms2 = JSON.stringify(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });

      this.$toast("发送成功");

      const TIME_COUNT = 60;
      if (!this.smsTimer2) {
        this.smsCount2 = TIME_COUNT;
        this.smsCountdown2 = false;
        this.smsText2 = "重新发送";
        this.smsTimer2 = setInterval(() => {
          if (this.smsCount2 > 0 && this.smsCount2 <= TIME_COUNT) {
            this.smsCount2--;
          } else {
            this.smsCountdown2 = true;

            clearInterval(this.smsTimer2);
            this.smsTimer2 = null;
          }
        }, 1000);
      }
    },
    addAddress() {},
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
.emil {
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