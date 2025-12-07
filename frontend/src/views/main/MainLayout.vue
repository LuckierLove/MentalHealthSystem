<template>
  <div class="main-layout">
    <el-container>
      <!-- 侧边导航栏 -->
      <el-aside width="200px" class="aside-menu">
        <div class="logo-container">
          <h3>心理健康系统</h3>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <!-- 公共菜单 -->
          <el-menu-item index="/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>

          <!-- 管理员菜单 -->
          <template v-if="hasRole('管理员')">
            <el-sub-menu index="admin">
              <template #title>
                <el-icon><Setting /></el-icon>
                <span>系统管理</span>
              </template>
              <el-menu-item index="/admin/users">用户管理</el-menu-item>
              <el-menu-item index="/admin/roles">角色管理</el-menu-item>
              <el-menu-item index="/admin/permissions">权限管理</el-menu-item>
            </el-sub-menu>
          </template>

          <!-- 咨询师菜单 -->
          <template v-if="hasRole('咨询师')">
            <el-sub-menu index="counselor">
              <template #title>
                <el-icon><ChatDotRound /></el-icon>
                <span>咨询管理</span>
              </template>
              <el-menu-item index="/counselor/appointments">预约管理</el-menu-item>
              <el-menu-item index="/counselor/records">咨询记录</el-menu-item>
            </el-sub-menu>
          </template>

          <!-- 学生菜单 -->
          <template v-if="hasRole('学生')">
            <el-sub-menu index="student">
              <template #title>
                <el-icon><User /></el-icon>
                <span>我的服务</span>
              </template>
              <el-menu-item index="/student/appointments">我的预约</el-menu-item>
              <el-menu-item index="/student/tests">心理测试</el-menu-item>
              <el-menu-item index="/student/treehole">心情树洞</el-menu-item>
            </el-sub-menu>
          </template>

          <!-- 咨询服务（所有角色可见） -->
          <el-menu-item index="/counselors">
            <el-icon><User /></el-icon>
            <span>咨询师列表</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-container>
        <!-- 顶部栏 -->
        <el-header class="header">
          <div class="header-left">
            <span class="welcome-text">欢迎，{{ userStore.nickname }}</span>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand">
              <span class="user-dropdown">
                <el-icon><Avatar /></el-icon>
                <span class="user-name">{{ userInfo }}</span>
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="settings">设置</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 内容区 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  House,
  Setting,
  ChatDotRound,
  User,
  Avatar,
  ArrowDown
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 用户信息显示
const userInfo = computed(() => {
  const roles = userStore.roles.join('、')
  return roles || '用户'
})

/**
 * 检查是否有某个角色
 */
function hasRole(role) {
  return userStore.hasRole(role)
}

/**
 * 处理下拉菜单命令
 */
function handleCommand(command) {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      handleLogout()
      break
  }
}

/**
 * 处理退出登录
 */
function handleLogout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  }).catch(() => {
    // 取消退出
  })
}

// 组件挂载时确保已获取用户信息
onMounted(() => {
  if (!userStore.nickname) {
    userStore.fetchUserInfo().catch(() => {
      ElMessage.error('获取用户信息失败')
      router.push('/login')
    })
  }
})
</script>

<style scoped>
.main-layout {
  height: 100vh;
  overflow: hidden;
}

.el-container {
  height: 100%;
}

.aside-menu {
  background-color: #304156;
  overflow-x: hidden;
}

.logo-container {
  height: 60px;
  line-height: 60px;
  text-align: center;
  background-color: #2b3a4b;
  color: #fff;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
}

.logo-container h3 {
  margin: 0;
  font-size: 16px;
}

.el-menu-vertical {
  border-right: none;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left .welcome-text {
  font-size: 14px;
  color: #606266;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #606266;
}

.user-dropdown:hover {
  color: #409EFF;
}

.user-name {
  margin: 0 8px;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>
