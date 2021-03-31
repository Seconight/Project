<template>
  <div id="app" :style="backgroundImg">
    <transition
      enter-active-class="pt-page-flipInRight"
      leave-active-class="pt-page-flipInRight"
    >
      <keep-alive>
        <router-view />
      </keep-alive>
    </transition>
    <Tabbar v-if="$route.meta.ifShowTabbar" />
  </div>
</template>

<script>
import Tabbar from "@/components/Tabbar.vue";

export default {
  data() {
    return {
      backgroundImg: {
        // backgroundImage: "url(" + require("@/assets/background.png") + ")",
        // backgroundSize: "100% 100%",
      },
    };
  },
  components: {
    Tabbar,
  },
};
</script>

<style>
@import "colorui/main.css";
@import "colorui/icon.css";
/* #f7f8fa */
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  min-height: 100%;
}
html,
body {
  width: 100vw;
  height: 100vh;
  margin: 0;
  padding: 0;
  font-family: "montserrat";
  background: #ecffff;
}
.body {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #ecffff;
}
/* html,
body {
  width: 100vw;
  height: 100vh;
  margin: 0;
  padding: 0;
  font-family: "montserrat";
  background-image: linear-gradient(
    125deg,
    #fed6e3,
    #a8edea,
    #d7fffe,
    #e9defa,
    #fbfcdb
  );
  background-size: 400%;
  animation: bganimation 5s infinite;
}
@keyframes bganimation {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
} */
@import "assets/css/animations.css";
</style>
