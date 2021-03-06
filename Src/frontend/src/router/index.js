
// 这块就是配置我们的路由信息了，一个是首页 home，一个是关于页面 about
// 其中 name: 'Home' 是我们的别名， path: '/',是我们的路由路径，component是我们路由对应的组件。

import Vue from "vue";
import VueRouter from 'vue-router'
import Home from "../views/Home";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
    }
  },
  {
    path: '/user',
    name: 'User',
    component: () => import('../views/User'),
    meta: {
      requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
    }
  },
  {
    path: '/user/specific/:recruitmentId',
    name: 'Specific',
    component: () => import('../views/user/Specific'),
    meta: {
      requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
    }
  },
  {
    path: '/manager',
    name: 'Manager',
    component: () => import('../views/Manager'),
    meta: {
      requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
    }
  },
  {
    path: '/manager/concrete/:recruitmentId',
    name: 'Concrete',
    component: () => import('../views/manager/Concrete'),
    meta: {
      requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
    }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/Admin'),
    meta: {
      requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  const isLogin = !!(window.sessionStorage.getItem("token"));
  if (to.meta.requireAuth && !isLogin) {
    next("/login");
  } else {
    next();
  }
});

export default router
