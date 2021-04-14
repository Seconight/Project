<template>
  <div class="body">
    <van-nav-bar
      title="个人信息"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      safe-area-inset-top
    >
    </van-nav-bar>
    <div class="personInfo">
      <van-cell-group title="基础信息">
        <van-cell title="姓名" :value="name" :readonly="readonly" />
        <van-cell title="身份" :value="role" :readonly="readonly" />
        <van-cell
          :title="role == '老师' ? '工号' : '学号'"
          :value="id"
          :readonly="readonly"
        />
        <van-cell v-if="role == '学生'" title="班级" :value="class_" />
      </van-cell-group>
      <van-cell-group title="账号绑定">
        <van-cell title="邮箱" v-if="address != ''" :value="address" />
        <van-cell title="邮箱" v-if="address == ''" is-link to="/User/email">
          <template #default>
            <div style="color: blue">邮箱未绑定</div>
          </template>
        </van-cell>
      </van-cell-group>
      <van-cell-group title="人脸信息" v-if="role == '学生'">
        <van-cell
          title="人脸状态"
          value="人脸已上传"
          v-if="checkFace == 'true'"
        />
        <van-cell
          title="人脸状态"
          v-if="checkFace != 'true'"
          is-link
          to="/User/faceUpload"
        >
          <template #default>
            <div style="color: blue">人脸未上传</div>
          </template>
        </van-cell>
      </van-cell-group>
      <van-cell-group title=" ">
        <van-button block style="color: red" @click="onlogout"
          >退出登录</van-button
        >
      </van-cell-group>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      readonly: true,
      name: "",
      role: "",
      id: "",
      address: "",
      class_: "",
      checkFace: "",
    };
  },
  created() {
    //进入user页面判断是否登录
    let userInfo = JSON.parse(localStorage.getItem("userInfo"));
    this.checkFace = localStorage.getItem("checkFace");
    this.name = userInfo.name;
    this.role = userInfo.role;
    this.id = userInfo.id;
    this.address = userInfo.address;
    this.class_ = userInfo.class;
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    onlogout() {
      this.$dialog
        .confirm({
          title: "确定要退出登录吗？",
        })
        .then(() => {
          // on confirm
          this.logout();
        })
        .catch(() => {
          // on cancel
        });
    },
    logout() {
      localStorage.removeItem("userInfo");
      localStorage.removeItem("checkFace");
      this.$router.push("/login");
    },
  },
};
</script>

<style>
</style>