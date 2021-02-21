<template>
  <van-nav-bar
    title="修改密码"
    left-text="返回"
    left-arrow
    @click-left="onClickLeft"
  />
</template>

<script>
export default {
  data() {
    return {
    };
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
  },
};
</script>

<style>
</style>