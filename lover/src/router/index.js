import Vue from 'vue'
import VueRouter from 'vue-router'

import User from '../views/User/User.vue'
import Course from '../views/Course/Course.vue'
import StuAttendance from '../views/Course/StuAttendance.vue'
import TeaAttendance from '../views/Course/TeaAttendance.vue'
Vue.use(VueRouter)


const routes = [
  {
    path: '/',
    //路由重定向
    redirect: '/user',
    meta:{
      ifShowTabbar: true
    }
  },
  {
    path: '/user',
    name: 'User',
    component: User,
    meta:{
      ifShowTabbar: true
    }
  },
  {
    path: '/course',
    name: 'Course',
    component: Course,
    children:[
      {
        path: 'stuAttendance',
        name: 'StuAttendance',
        component: StuAttendance,
      },
      {
        path: 'teaAttendance',
        name: 'TeaAttendance',
        component: TeaAttendance,
      },
    ],
    meta:{
      ifShowTabbar: true
    }
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
