<template>
  <div class="counselor-page">
    <div class="page-header">
      <div>
        <h2>咨询师推荐</h2>
        <p class="sub">挑选适合你的咨询师，收藏后可以在收藏夹里管理。</p>
      </div>
      <div class="actions">
        <el-input
          v-model="keyword"
          placeholder="搜索咨询师姓名"
          clearable
          :prefix-icon="Search"
          class="search-input"
          @keyup.enter="fetchCounselors"
        />
        <el-button type="primary" @click="fetchCounselors">搜索</el-button>
      </div>
    </div>

    <el-row :gutter="20" class="card-grid">
      <el-col v-for="c in counselors" :key="c.id" :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="counselor-card">
          <div class="card-top">
            <div class="avatar">{{ c.name?.slice(0, 1) || '咨' }}</div>
            <div>
              <div class="name-line">
                <span class="name">{{ c.name }}</span>
                <el-tag size="small" type="success">{{ c.specialty || '擅长倾听' }}</el-tag>
              </div>
              <div class="rating-row">
                <el-rate v-model="c.displayRating" disabled allow-half />
                <span class="rating-text">{{ c.displayRating?.toFixed(1) || '暂无评分' }}</span>
              </div>
            </div>
          </div>
          <p class="intro">{{ c.introduction || '这位咨询师很专业，欢迎预约或收藏。' }}</p>
          <div class="card-actions">
            <el-button type="primary" plain @click="toggleFavourite(c)">
              <el-icon class="action-icon">
                <StarFilled v-if="isFavourited(c.id)" />
                <Star v-else />
              </el-icon>
              {{ isFavourited(c.id) ? '已收藏' : '收藏' }}
            </el-button>
            <el-button @click="openDetail(c)">查看详情</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="pagination" v-if="total > pageSize">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="page"
        @current-change="handlePageChange"
      />
    </div>

    <el-drawer v-model="showDrawer" title="咨询师详情" size="40%">
      <div v-if="activeCounselor" class="drawer-body">
        <div class="drawer-header">
          <div class="avatar big">{{ activeCounselor.name?.slice(0, 1) }}</div>
          <div>
            <div class="name">{{ activeCounselor.name }}</div>
            <div class="tag-line">
              <el-tag type="success">{{ activeCounselor.specialty || '擅长倾听' }}</el-tag>
            </div>
            <div class="rating-row">
              <el-rate v-model="activeCounselor.displayRating" disabled allow-half />
              <span class="rating-text">{{ activeCounselor.displayRating?.toFixed(1) || '暂无评分' }}</span>
            </div>
          </div>
        </div>
        <p class="detail-intro">{{ activeCounselor.introduction || '这位咨询师致力于为同学们提供支持和陪伴。' }}</p>
        <el-button type="primary" @click="toggleFavourite(activeCounselor)">
          <el-icon class="action-icon">
            <StarFilled v-if="isFavourited(activeCounselor.id)" />
            <Star v-else />
          </el-icon>
          {{ isFavourited(activeCounselor.id) ? '取消收藏' : '收藏' }}
        </el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getCounselorList } from '@/api/counselor'
import { createFavourite, deleteFavourite, getFavouriteList } from '@/api/favourite'
import { useUserStore } from '@/stores/user'
import { Star, StarFilled, Search } from '@element-plus/icons-vue'

const userStore = useUserStore()

const counselors = ref([])
const keyword = ref('')
const page = ref(1)
const pageSize = 9
const total = ref(0)
const favouriteMap = reactive(new Map())
const showDrawer = ref(false)
const activeCounselor = ref(null)

const normalizeRating = (num) => {
  const val = Number(num)
  if (Number.isNaN(val)) return 0
  return Math.round(val * 2) / 2
}

function mapFavourites(list) {
  favouriteMap.clear()
  list.forEach((fav) => {
    const counselorId = fav.counselor_id || fav.counselorId
    if (counselorId) {
      favouriteMap.set(counselorId, fav.id)
    }
  })
}

async function fetchFavourites() {
  if (!userStore.userId) return
  try {
    const res = await getFavouriteList(1, 200, userStore.userId)
    if (res.code === 200 && res.data?.records) {
      mapFavourites(res.data.records)
    }
  } catch (err) {
    console.error('加载收藏失败', err)
  }
}

async function fetchCounselors() {
  try {
    const res = await getCounselorList(page.value, pageSize, keyword.value)
    if (res.code === 200) {
      const list = res.data?.records || []
      counselors.value = list.map((c) => ({
        ...c,
        displayRating: normalizeRating(c.rating)
      }))
      total.value = res.data?.total || 0
    }
  } catch (err) {
    ElMessage.error('获取咨询师列表失败')
  }
}

async function ensureUserReady() {
  if (!userStore.userId) {
    await userStore.fetchUserInfo()
  }
}

async function handlePageChange(val) {
  page.value = val
  await fetchCounselors()
}

function isFavourited(counselorId) {
  return favouriteMap.has(counselorId)
}

async function toggleFavourite(counselor) {
  if (!counselor?.id) return
  try {
    await ensureUserReady()
    const favId = favouriteMap.get(counselor.id)
    if (favId) {
      await deleteFavourite(favId)
      favouriteMap.delete(counselor.id)
      ElMessage.success('已取消收藏')
    } else {
      await createFavourite(counselor.id)
      await fetchFavourites()
      ElMessage.success('已收藏')
    }
  } catch (err) {
    ElMessage.error('操作失败，请稍后重试')
  }
}

function openDetail(c) {
  activeCounselor.value = c
  showDrawer.value = true
}

onMounted(async () => {
  await ensureUserReady()
  await Promise.all([fetchCounselors(), fetchFavourites()])
})
</script>

<style scoped>
.counselor-page {
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 22px;
  color: #1f2d3d;
}

.sub {
  margin: 4px 0 0;
  color: #606266;
  font-size: 13px;
}

.actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 260px;
}

.card-grid {
  margin-top: 10px;
}

.counselor-card {
  margin-bottom: 20px;
  border: none;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.05);
}

.card-top {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 12px;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  background: linear-gradient(135deg, #5c7cfa, #74c0fc);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
}

.avatar.big {
  width: 64px;
  height: 64px;
  border-radius: 16px;
}

.name-line {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 6px;
}

.name {
  font-size: 18px;
  font-weight: 600;
  color: #1f2d3d;
}

.rating-row {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #909399;
}

.rating-text {
  font-size: 12px;
}

.intro {
  min-height: 48px;
  color: #606266;
  line-height: 1.5;
}

.card-actions {
  margin-top: 12px;
  display: flex;
  gap: 10px;
}

.action-icon {
  margin-right: 4px;
}

.pagination {
  text-align: center;
  margin-top: 10px;
}

.drawer-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.drawer-header {
  display: flex;
  gap: 12px;
  align-items: center;
}

.tag-line {
  margin: 6px 0;
}

.detail-intro {
  color: #606266;
  line-height: 1.6;
}
</style>
