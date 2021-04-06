import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './vantui'
import 'vant/lib/index.css'
import  global from './global/global.js'
import 'swiper/swiper-bundle.css'

Vue.config.productionTip = false
Vue.prototype.GLOBAL = global

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
