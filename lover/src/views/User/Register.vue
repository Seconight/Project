<template>
  <div>
    <van-nav-bar title="账号注册" left-text="返回" @click-left="onClickLeft" />
    <div>
      <div style="height: 150px"></div>
      <van-form @submit="onSubmit">
        <van-field label="身份">
          <template #input>
            <van-radio-group
              v-model="role"
              direction="horizontal"
              @change="changeRole"
            >
              <van-radio name="学生">学生</van-radio>
              <van-radio name="老师">老师</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
          v-model="name"
          type="name"
          label="姓名"
          placeholder="姓名"
          required
          :rules="[{ required: true, message: '请填写姓名' }]"
        />
        <van-field
          v-model="id"
          :label="idLabel"
          :placeholder="idLabel"
          required
          :rules="[{ required: true, message: '请填写' + idLabel }]"
        />
        <van-field
          v-model="password"
          type="password"
          label="密码"
          placeholder="密码"
          required
          :rules="[{ required: true, message: '请填写密码' }]"
        />
        <van-field
          v-model="confirmPassword"
          type="password"
          label="确认密码"
          placeholder="请再次输入密码"
          required
          :rules="[
            { required: true, message: '请填写确认密码' },
            { validator, message: '密码不一致' },
          ]"
        />
        <van-field
          v-if="role == '学生'"
          v-model="class_"
          label="班级"
          placeholder="班级"
          required
          :rules="[{ required: true, message: '请填写班级' }]"
        />
        <van-field
          v-model="address"
          label="邮箱"
          placeholder="邮箱"
          :rules="[
            {
              required: false,
              pattern: /^$|^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/,
              message: '请填写正确邮箱格式',
            },
          ]"
        />
        <div style="margin: 16px">
          <van-button round block type="info" native-type="submit"
            >提交</van-button
          >
        </div>
      </van-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: "",
      password: "",
      confirmPassword: "",
      role: "学生",
      id: "",
      idLabel: "学号",
      class_: "",
      address: "",
    };
  },

  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    onSubmit(values) {
      console.log("name", this.name);
      console.log("id", this.id);
      console.log("password", this.password);
      console.log("class", this.class_);
      console.log("Address", this.address);

      var axios = require("axios");
      var FormData = require("form-data");
      var data = new FormData();
      data.append("id", this.id);
      data.append("password", this.password);
      data.append("name", this.name);
      data.append("studentClass", this.class_);
      data.append("address",this.address);

      var config = {
        method: "put",
        url: this.GLOBAL.port + "/user/register",
        headers: {
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
    },
    changeRole() {
      this.idLabel = this.role == "学生" ? "学号" : "职工号";
      this.name = "";
      this.id = "";
      this.password = "";
      this.confirmPassword = "";
      this.class_ = "";
      this.address = "";
    },
    validator(val) {
      return this.password == this.confirmPassword ? true : false;
    },
  },
};
</script>

<style>
</style>