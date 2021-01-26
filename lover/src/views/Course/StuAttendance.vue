<template>
  <div class="TeaAttendance">
    <div class="Top">
      <van-icon name="arrow-left" class @click="goback" />
      <div class="tital">签到详情</div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeNames: [],
      role: "",
      courses: [],
    };
  },
  methods: {
    goback() {
        this.$router.go(-1);
    },
  },
};
</script>

<style lang='less' scoped>
.TeaAttendance {
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  position: absolute;
  background: rgb(233, 229, 229);
}
.Top {
  position: relative;
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  -webkit-box-align: center;
  -webkit-align-items: center;
  align-items: center;
  -webkit-box-pack: center;
  -webkit-justify-content: center;
  justify-content: center;
  height: 56px;
  background-color: #fff;
  z-index: 99;
  .van-icon {
    position: absolute;
    top: 16px;
    left: 16px;
    width: 24px;
    height: 24px;
    cursor: pointer;
  }
  .div {
    font-weight: 600;
    font-size: 17px;
    text-transform: capitalize;
  }
}
</style>