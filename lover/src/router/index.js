import Vue from 'vue'
import VueRouter from 'vue-router'

import User from '../views/User/User.vue'
import Course from '../views/Course/Course.vue'
import StuAttendance from '../views/Course/StuAttendance.vue'
import TeaAttendance from '../views/Course/TeaAttendance.vue'
import AddCourse from '../views/Course/AddCourse.vue'
import CreatCourse from '../views/Course/CreatCourse.vue'
import StuStudentList from '../views/Course/StuStudentList.vue'
import PhotoSignIn from '../views/Course/PhotoSignIn.vue'
import AttendanceDetail from '../views/Course/AttendanceDetail.vue'

import Email from '../views/User/Email.vue'
import FaceUpload from '../views/User/FaceUpload.vue'
import ChangePassword from '../views/User/ChangePassword.vue'

import PersonalInfo from '../views/User/PersonalInfo.vue'
import Login from '../views/Login/Login.vue'
import RetrievePassword from '../views/Login/RetrievePassword.vue'
import Register from '../views/Login/Register.vue'
import Test from '../views/Test.vue'
Vue.use(VueRouter)


const routes = [
  {
    path: '/',
    //路由重定向
    redirect: '/user',
    meta: {
      ifShowTabbar: true
    }
  },
  {
    path: '/test',
    name: 'Test',
    component: Test,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    children: [
      {
        path: 'retrievePassword',
        name: 'RetrievePassword',
        component: RetrievePassword,
      },
      {
        path: 'register',
        name: 'Register',
        component: Register,
      },
    ],
  },
  {
    path: '/user',
    name: 'User',
    component: User,
    children: [
      {
        path: 'email',
        name: 'Email',
        component: Email,
      },
      {
        path: 'faceUpload',
        name: 'FaceUpload',
        component: FaceUpload,
      },

      {
        path: 'changePassword',
        name: 'ChangePassword',
        component: ChangePassword,
      },
      {
        path: 'personalInfo',
        name: 'PersonalInfo',
        component: PersonalInfo,
      },
    ],
    meta: {
      ifShowTabbar: true
    }
  },

  {
    path: '/course',
    name: 'Course',
    component: Course,
    children: [
      {
        path: 'stuAttendance',
        name: 'StuAttendance',
        component: StuAttendance,
      },
      {
        path: 'teaAttendance',
        name: 'TeaAttendance',
        component: TeaAttendance,
        children: []
      },
      {
        path: 'addCourse',
        name: 'AddCourse',
        component: AddCourse,
      },
      {
        path: 'creatCourse',
        name: 'CreatCourse',
        component: CreatCourse,
      },
      {
        path: 'stuStudentList',
        name: 'StuStudentList',
        component: StuStudentList,
      },
      {
        path: 'photoSignIn',
        name: 'PhotoSignIn',
        component: PhotoSignIn,
      },
      {
        path: 'attendanceDetail',
        name: 'AttendanceDetail',
        component: AttendanceDetail,
      }
    ],
    meta: {
      ifShowTabbar: true
    }
  },
]


const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})


//路由拦截

// 解决Vue-Router升级导致的Uncaught(in promise) navigation guard问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

router.beforeEach((to, from, next) => {
  let token = localStorage.getItem('userInfo');
  if (to.path == '/course') {
    if (token) {
      next();
    } else {
      //alert("请先登录")
      Vue.prototype.$toast("请先登录");
      //定时器
      next("/user");
    }
    return;
  }
  //对所有路由适配
  next()
})

export default router
