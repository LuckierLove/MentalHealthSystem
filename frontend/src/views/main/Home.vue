<template>
  <div class="home-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用大学生心理健康系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <p>您好，{{ userInfo }}！</p>
        <p>当前角色：{{ roles }}</p>
        <p class="tips">本系统旨在为大学生提供心理健康支持和咨询服务。</p>
      </div>
    </el-card>

    <!-- 功能快捷入口 -->
    <el-row :gutter="20" class="feature-cards">
      <!-- 管理员功能 -->
      <template v-if="hasRole('管理员')">
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/admin/users')">
            <el-icon class="feature-icon" :size="40" color="#409EFF">
              <User />
            </el-icon>
            <h3>用户管理</h3>
            <p>管理系统用户信息</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/admin/roles')">
            <el-icon class="feature-icon" :size="40" color="#67C23A">
              <Setting />
            </el-icon>
            <h3>角色管理</h3>
            <p>管理系统角色配置</p>
          </el-card>
        </el-col>
      </template>

      <!-- 咨询师功能 -->
      <template v-if="hasRole('咨询师')">
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/counselor/appointments')">
            <el-icon class="feature-icon" :size="40" color="#E6A23C">
              <Calendar />
            </el-icon>
            <h3>预约管理</h3>
            <p>查看和管理咨询预约</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/counselor/records')">
            <el-icon class="feature-icon" :size="40" color="#F56C6C">
              <Document />
            </el-icon>
            <h3>咨询记录</h3>
            <p>查看历史咨询记录</p>
          </el-card>
        </el-col>
      </template>

      <!-- 学生功能 -->
      <template v-if="hasRole('学生')">
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/student/appointments')">
            <el-icon class="feature-icon" :size="40" color="#409EFF">
              <Calendar />
            </el-icon>
            <h3>我的预约</h3>
            <p>查看我的咨询预约</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/student/tests')">
            <el-icon class="feature-icon" :size="40" color="#67C23A">
              <Memo />
            </el-icon>
            <h3>心理测试</h3>
            <p>进行心理健康测评</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/student/treehole')">
            <el-icon class="feature-icon" :size="40" color="#E6A23C">
              <ChatDotRound />
            </el-icon>
            <h3>心情树洞</h3>
            <p>分享和倾诉心情</p>
          </el-card>
        </el-col>
      </template>

      <!-- 公共功能 -->
      <el-col :span="6">
        <el-card shadow="hover" class="feature-card" @click="goTo('/counselors')">
          <el-icon class="feature-icon" :size="40" color="#909399">
            <Avatar />
          </el-icon>
          <h3>咨询师列表</h3>
          <p>查看专业咨询师</p>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统信息 -->
    <el-card class="info-card" v-if="permissions.length > 0">
      <template #header>
        <div class="card-header">
          <span>权限信息</span>
        </div>
      </template>
      <div class="permission-list">
        <el-tag v-for="perm in permissions" :key="perm" type="info" class="permission-tag">
          {{ perm }}
        </el-tag>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  User,
  Setting,
  Calendar,
  Document,
  Memo,
  ChatDotRound,
  Avatar
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 用户信息
const userInfo = computed(() => userStore.userId || '用户')
const roles = computed(() => userStore.roles.join('、') || '暂无角色')
const permissions = computed(() => userStore.permissions)

/**
 * 检查是否有某个角色
 */
function hasRole(role) {
  return userStore.hasRole(role)
}

/**
 * 跳转到指定页面
 */
function goTo(path) {
  router.push(path)
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  margin-bottom: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.welcome-content {
  font-size: 16px;
  line-height: 2;
}

.welcome-content .tips {
  color: #909399;
  font-size: 14px;
  margin-top: 20px;
}

.feature-cards {
  margin-bottom: 20px;
}

.feature-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  margin: 20px 0;
}

.feature-card h3 {
  margin: 10px 0;
  font-size: 16px;
  color: #303133;
}

.feature-card p {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

.info-card {
  margin-top: 20px;
}

.permission-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.permission-tag {
  margin: 0;
}
</style>
