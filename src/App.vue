<template>
  <div id="app">
    <router-view />
    <Tabbar v-if="$route.meta.ifShowTabbar" />
  </div>
</template>

<script>
import Tabbar from "@/components/Tabbar.vue";

export default {
  data() {
    return {};
  },
  components: {
    Tabbar,
  },
};
</script>

<style>
@import "colorui/main.css";
@import "colorui/icon.css";
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  background-color: #f7f8fa;
  min-height: 100%;
}
html,body{
  height: 100%;
}
</style>
