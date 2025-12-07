/**
 * 路由配置
 */
import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/LoginView.vue'),
    meta: { 
      title: '登录',
      requiresAuth: false 
    }
  },
  {
    path: '/',
    name: 'Main',
    component: () => import('@/views/main/MainLayout.vue'),
    meta: { 
      title: '主页',
      requiresAuth: true 
    },
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/main/Home.vue'),
        meta: { 
          title: '首页',
          requiresAuth: true 
        }
      }
      ,
      {
        path: '/management/users',
        name: 'UsersManagement',
        component: () => import('@/views/management/Users.vue'),
        meta: { title: '用户管理', requiresAuth: true }
      },
      {
        path: '/management/roles',
        name: 'RolesManagement',
        component: () => import('@/views/management/Roles.vue'),
        meta: { title: '角色管理', requiresAuth: true }
      },
      {
        path: '/management/counselors',
        name: 'CounselorsManagement',
        component: () => import('@/views/management/Counselors.vue'),
        meta: { title: '咨询师管理', requiresAuth: true }
      },
      {
        path: '/management/treehole',
        name: 'TreeholeManagement',
        component: () => import('@/views/management/TreeholeManage.vue'),
        meta: { title: '树洞管理', requiresAuth: true }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('@/views/main/Profile.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      },
      {
        path: '/student/appointments',
        name: 'StudentAppointments',
        component: () => import('@/views/student/Appointments.vue'),
        meta: { title: '我的预约', requiresAuth: true }
      },
      {
        path: '/student/treehole',
        name: 'StudentTreehole',
        component: () => import('@/views/main/Treehole.vue'),
        meta: { title: '心情树洞', requiresAuth: true }
      },
      {
        path: '/student/tests',
        name: 'StudentTests',
        component: () => import('@/views/student/Tests.vue'),
        meta: { title: '心理测试', requiresAuth: true }
      },
      {
        path: '/counselor/appointments',
        name: 'CounselorAppointments',
        component: () => import('@/views/counselor/AppointmentManagement.vue'),
        meta: { title: '预约管理', requiresAuth: true }
      },
      {
        path: '/counselor/treehole',
        name: 'CounselorTreehole',
        component: () => import('@/views/main/Treehole.vue'),
        meta: { title: '心情树洞', requiresAuth: true }
      },
      {
        path: '/counselor/dialogs',
        name: 'CounselorDialogRecords',
        component: () => import('@/views/main/DialogRecords.vue'),
        meta: { title: '咨询记录', requiresAuth: true }
      },
      {
        path: '/counselor/tests',
        name: 'CounselorTestManage',
        component: () => import('@/views/counselor/TestManage.vue'),
        meta: { title: '问卷管理', requiresAuth: true }
      }
      // 这里可以根据角色添加更多子路由
      // 管理员路由、咨询师路由、学生路由等
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 设置页面标题
  document.title = to.meta.title || '大学生心理健康系统'
  
  // 需要认证的路由
  if (to.meta.requiresAuth) {
    if (!userStore.token) {
      // 未登录，跳转到登录页
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    } else {
      // 已登录，检查是否已获取用户信息
      if (!userStore.userId) {
        // 获取用户信息后再进入页面
        userStore.fetchUserInfo()
          .then(() => {
            next()
          })
          .catch(() => {
            // 获取用户信息失败，清除 token 并跳转到登录页
            userStore.logout()
            next('/login')
          })
      } else {
        next()
      }
    }
  } else {
    // 不需要认证的路由直接放行
    if (to.path === '/login' && userStore.token) {
      // 已登录用户访问登录页，跳转到首页
      next('/')
    } else {
      next()
    }
  }
})

export default router
