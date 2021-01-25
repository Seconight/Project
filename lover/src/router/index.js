import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

import User from '../views/User/User.vue'
Vue.use(VueRouter)


const routes = [
  {
    path: '/',
    //路由重定向
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
  },
  {
    path: '/user',
    name: 'User',
    component: User,
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//路由拦截
//
// router.beforeEach((to,from,next)=>{
//   let token=localStorage.getItem('token')
//   if(to.path='/')
//   {
//     if(token){
//       next();
//     }else{
//     //alert("请先登录")
//       Vue.prototype.$toast("请先登录");
//       //定时器
//         setTimeout(()=>{
//           next("/user");
//         },1000)
//     }
//     return;
//   }
//   //对所有路由适配
//   next()
// })

export default router
