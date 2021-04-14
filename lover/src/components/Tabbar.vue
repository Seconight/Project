<template>
  <van-tabbar v-model="active" fixed placeholder >
    <van-tabbar-item to="/course" icon="apps-o">课程</van-tabbar-item>
    <van-tabbar-item to="/user" icon="user-o">我的</van-tabbar-item>
  </van-tabbar>
</template>

<script>
export default {
  data() {
    return {
    };
  },
  computed: {
    active: {
      get() {
        switch (this.$route.path) {
          case "/home":
            return 0;
          case "/user":
            return 1;
          default:
            break;
        }
      },
      set() {},
    },
  },
};
</script>