<template>
  <div>
    <van-nav-bar
      :title="readonly == true ? '个人信息' : '个人信息编辑'"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
    >
      <template #right>
        <van-icon
          v-if="readonly == true"
          name="edit"
          size="18"
          @click="readonly = false"
        />
      </template>
    </van-nav-bar>
    <div>
      <van-cell-group>
        <van-field label="姓名" :value="name" :readonly="readonly" />
        <van-field label="身份" :value="role" :readonly="readonly" />
        <van-field
          :label="role == '老师' ? '工号' : '学号'"
          :value="id"
          :readonly="readonly"
        />
        <van-field v-if="role == '学生'" label="班级" :value="class_" />
        <van-field
          label="邮箱"
          :value="address == null ? '未绑定' : address"
          disabled
        />
        <van-field
          label="人脸状态"
          :value="checkFace == true ? '人脸已上传' : '人脸未上传'"
          disabled
        />
      </van-cell-group>
    </div>

    <van-button
      type="primary"
      round
      block
      v-if="readonly == false"
      @click="onSave"
      >保存修改</van-button
    >
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
    onSave() {
      console.log(this.name);
      console.log(this.role);
      console.log(this.id);
      console.log(this.address);
      console.log(this.class_);
      this.readonly = true;
    },
  },
};
</script>

<style>
</style>