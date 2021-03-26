<template>
  <swiper ref="mySwiper" :options="swiperOptions">
    <swiper-slide v-for="(signInPhoto, index) in signInPhotos" :key="index" style="text-align:center">
      <van-image
        width="100"
        height="100"
        :src="signInPhoto"
        contain
      />
    </swiper-slide>
    <div class="swiper-pagination" slot="pagination"></div>
  </swiper>
</template>

<script>
export default {
  name: "carrousel",
  data() {
    return {
      signInPhotos: [
        require("@/assets/user/background.jpg"),
        require("@/assets/user/background.jpg"),
        require("@/assets/user/background.jpg"),
      ],
      swiperOptions: {
        pagination: {
          el: ".swiper-pagination",
        },
        // Some Swiper option/callback...
      },
    };
  },
  computed: {
    swiper() {
      return this.$refs.mySwiper.$swiper;
    },
  },
  mounted() {
    console.log("Current Swiper instance object", this.swiper);
    this.swiper.slideTo(3, 1000, false);
  },
};
</script>