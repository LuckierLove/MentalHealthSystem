<template>
  <div class="fav-page">
    <div class="page-header">
      <div>
        <h2>我的收藏</h2>
        <p class="sub">已收藏的咨询师会汇总在这里，方便快速查看与预约。</p>
      </div>
      <el-button type="primary" @click="goExplore">去发现咨询师</el-button>
    </div>

    <el-skeleton v-if="loading" animated :rows="4" style="margin-bottom: 12px;" />

    <el-empty v-else-if="favourites.length === 0" description="还没有收藏任何咨询师" />

    <el-row v-else :gutter="20" class="card-grid">
      <el-col v-for="fav in favourites" :key="fav.id" :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="fav-card">
          <div class="card-top">
            <div class="avatar">{{ counselorFor(fav)?.name?.slice(0, 1) || '咨' }}</div>
            <div>
              <div class="name-line">
                <span class="name">{{ counselorFor(fav)?.name || '咨询师' }}</span>
                <el-tag size="small" type="success">{{ counselorFor(fav)?.specialty || '擅长倾听' }}</el-tag>
              </div>
              <div class="rating-row">
                <el-rate :model-value="counselorFor(fav)?.displayRating" disabled allow-half />
                <span class="rating-text">{{ (counselorFor(fav)?.displayRating || 0).toFixed(1) }}</span>
              </div>
            </div>
          </div>
          <p class="intro">{{ counselorFor(fav)?.introduction || '这位咨询师很专业，欢迎预约或咨询。' }}</p>
          <div class="card-actions">
            <el-button type="primary" plain @click="openDetail(fav)">查看详情</el-button>
            <el-button type="danger" plain @click="removeFavourite(fav)">取消收藏</el-button>
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
              <span class="rating-text">{{ (activeCounselor.displayRating || 0).toFixed(1) }}</span>
            </div>
          </div>
        </div>
        <p class="detail-intro">{{ activeCounselor.introduction || '这位咨询师致力于为同学们提供支持和陪伴。' }}</p>
        <el-button type="danger" @click="removeFavourite(activeFav)">取消收藏</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getFavouriteList, deleteFavourite } from '@/api/favourite'
import { getCounselor } from '@/api/counselor'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const favourites = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = 9
const loading = ref(false)
const counselorMap = reactive(new Map())
const showDrawer = ref(false)
const activeCounselor = ref(null)
const activeFav = ref(null)

const normalizeRating = (num) => {
  const val = Number(num)
  if (Number.isNaN(val)) return 0
  return Math.round(val * 2) / 2
}

function counselorFor(fav) {
  if (!fav) return null
  const counselorId = fav.counselor_id || fav.counselorId
  return counselorMap.get(counselorId) || null
}

async function ensureUserReady() {
  if (!userStore.userId) {
    await userStore.fetchUserInfo()
  }
}

async function fetchCounselorDetails(list) {
  const missingIds = list
    .map((f) => f.counselor_id || f.counselorId)
    .filter((id) => id && !counselorMap.has(id))
  if (missingIds.length === 0) return

  await Promise.all(
    missingIds.map(async (id) => {
      try {
        const res = await getCounselor(id)
        if (res.code === 200 && res.data) {
          counselorMap.set(id, {
            ...res.data,
            displayRating: normalizeRating(res.data.rating)
          })
        }
      } catch (err) {
        console.error('获取咨询师失败', err)
      }
    })
  )
}

async function fetchFavourites() {
  loading.value = true
  try {
    const res = await getFavouriteList(page.value, pageSize, userStore.userId)
    if (res.code === 200 && res.data) {
      favourites.value = res.data.records || []
      total.value = res.data.total || 0
      await fetchCounselorDetails(favourites.value)
    }
  } catch (err) {
    ElMessage.error('获取收藏列表失败')
  } finally {
    loading.value = false
  }
}

async function handlePageChange(val) {
  page.value = val
  await fetchFavourites()
}

async function removeFavourite(fav) {
  if (!fav?.id) return
  try {
    await deleteFavourite(fav.id)
    ElMessage.success('已取消收藏')
    await fetchFavourites()
    if (activeFav.value?.id === fav.id) {
      showDrawer.value = false
    }
  } catch (err) {
    ElMessage.error('取消收藏失败')
  }
}

function openDetail(fav) {
  activeFav.value = fav
  activeCounselor.value = counselorFor(fav)
  showDrawer.value = true
}

function goExplore() {
  router.push('/student/counselors')
}

onMounted(async () => {
  await ensureUserReady()
  await fetchFavourites()
})
</script>

<style scoped>
.fav-page {
  max-width: 1100px;
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

.card-grid {
  margin-top: 10px;
}

.fav-card {
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
  background: linear-gradient(135deg, #34c38f, #5ad8a6);
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
