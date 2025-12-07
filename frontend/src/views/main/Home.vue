<template>
  <div class="home-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用大学生心理健康系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <p>您好，{{ nickname }}！</p>
        <p>当前角色：{{ roles }}</p>
        <p class="tips">本系统旨在为大学生提供心理健康支持和咨询服务。</p>
      </div>
    </el-card>

    <!-- 统计数据（管理员/学生可见） -->
    <el-row v-if="hasRole('管理员') || hasRole('学生')" :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">总预约数</div>
          <div class="stat-value">{{ totalAppointments }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">今日预约数</div>
          <div class="stat-value">{{ todayAppointments }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">咨询师数量</div>
          <div class="stat-value">{{ counselorCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-title">用户数量</div>
          <div class="stat-value">{{ userCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能快捷入口 -->
    <el-row :gutter="20" class="feature-cards">
      <!-- 管理员功能 -->
      <template v-if="hasRole('管理员')">
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/management/users')">
            <el-icon class="feature-icon" :size="40" color="#409EFF">
              <User />
            </el-icon>
            <h3>用户管理</h3>
            <p>管理系统用户信息</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/management/roles')">
            <el-icon class="feature-icon" :size="40" color="#67C23A">
              <Setting />
            </el-icon>
            <h3>角色管理</h3>
            <p>管理系统角色配置</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/management/counselors')">
            <el-icon class="feature-icon" :size="40" color="#E6A23C">
              <User />
            </el-icon>
            <h3>咨询师管理</h3>
            <p>管理系统咨询师</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/profile')">
            <el-icon class="feature-icon" :size="40" color="#909399">
              <Avatar />
            </el-icon>
            <h3>个人中心</h3>
            <p>查看与编辑个人信息</p>
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
          <el-card shadow="hover" class="feature-card" @click="goTo('/counselor/dialogs')">
            <el-icon class="feature-icon" :size="40" color="#F56C6C">
              <Document />
            </el-icon>
            <h3>咨询记录</h3>
            <p>查看历史咨询记录</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/counselor/treehole')">
            <el-icon class="feature-icon" :size="40" color="#409EFF">
              <ChatDotRound />
            </el-icon>
            <h3>心情树洞</h3>
            <p>分享和倾诉心情</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/counselor/tests')">
            <el-icon class="feature-icon" :size="40" color="#67C23A">
              <Memo />
            </el-icon>
            <h3>问卷管理</h3>
            <p>创建和管理测试问卷</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/counselor/warnings')">
            <el-icon class="feature-icon" :size="40" color="#F56C6C">
              <Bell />
            </el-icon>
            <h3>心理预警</h3>
            <p>关注超出阈值学生</p>
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
          <el-card shadow="hover" class="feature-card" @click="goTo('/student/counselors')">
            <el-icon class="feature-icon" :size="40" color="#5c7cfa">
              <User />
            </el-icon>
            <h3>咨询师推荐</h3>
            <p>浏览咨询师信息并收藏</p>
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
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/student/favourites')">
            <el-icon class="feature-icon" :size="40" color="#faad14">
              <StarFilled />
            </el-icon>
            <h3>我的收藏</h3>
            <p>管理已收藏的咨询师</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="feature-card" @click="goTo('/profile')">
            <el-icon class="feature-icon" :size="40" color="#909399">
              <Avatar />
            </el-icon>
            <h3>个人中心</h3>
            <p>查看与编辑个人信息</p>
          </el-card>
        </el-col>
      </template>
    </el-row>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  User,
  Setting,
  Calendar,
  Document,
  Memo,
  ChatDotRound,
  Avatar,
  Bell,
  StarFilled
} from '@element-plus/icons-vue'
import {
  getTotalAppointments,
  getTodayAppointments,
  getCounselorCount,
  getUserCount
} from '@/api/stats'

const router = useRouter()
const userStore = useUserStore()

// 统计数据
const totalAppointments = ref(0)
const todayAppointments = ref(0)
const counselorCount = ref(0)
const userCount = ref(0)

// 用户信息
const nickname = computed(() => userStore.nickname || '用户')
const roles = computed(() => userStore.roles.join('、') || '暂无角色')
const permissions = computed(() => userStore.permissions)

/**
 * 检查是否有某个角色
 */
function hasRole(role) {
  return userStore.hasRole(role)
}

// 获取统计数据
async function fetchCounts() {
  try {
    const [totalRes, todayRes, counselorRes, userRes] = await Promise.all([
      getTotalAppointments(),
      getTodayAppointments(),
      getCounselorCount(),
      getUserCount()
    ])
    if (totalRes?.code === 200) totalAppointments.value = totalRes.data ?? 0
    if (todayRes?.code === 200) todayAppointments.value = todayRes.data ?? 0
    if (counselorRes?.code === 200) counselorCount.value = counselorRes.data ?? 0
    if (userRes?.code === 200) userCount.value = userRes.data ?? 0
  } catch (err) {
    // 统计失败时不阻塞页面
    console.error('获取统计数据失败', err)
  }
}

/**
 * 跳转到指定页面
 */
function goTo(path) {
  router.push(path)
}

onMounted(() => {
  if (hasRole('管理员') || hasRole('学生')) {
    fetchCounts()
  }
})
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

.stats-cards {
  margin: 20px 0;
}

.stat-card {
  text-align: center;
  padding: 10px 0;
}

.stat-title {
  color: #606266;
  font-size: 14px;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
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
