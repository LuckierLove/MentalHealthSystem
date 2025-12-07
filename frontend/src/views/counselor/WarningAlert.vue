<template>
  <div class="warning-alert">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>心理预警</span>
          <div class="actions">
            <el-button type="primary" size="small" @click="handleRefresh">刷新</el-button>
          </div>
        </div>
      </template>
      <el-alert
        v-if="warnings.length === 0 && !loading"
        title="暂无心理预警"
        type="success"
        description="目前所有问卷作答未超过阈值，可继续关注学生心理健康。"
        :closable="false"
        style="margin-bottom: 16px"
      />
      <el-table
        v-else
        :data="warnings"
        v-loading="loading"
        border
        stripe
        style="margin-bottom: 16px"
      >
        <el-table-column type="index" width="60" label="#" />
        <el-table-column prop="userName" label="学生姓名" min-width="120" />
        <el-table-column prop="userId" label="学生ID" min-width="150" />
        <el-table-column prop="testTitle" label="问卷" min-width="200" />
        <el-table-column label="得分" width="120">
          <template #default="{ row }">
            <span class="score" :class="{ 'score-warning': row.score > row.threshold }">
              {{ row.score }} 分
            </span>
          </template>
        </el-table-column>
        <el-table-column label="阈值" width="120">
          <template #default="{ row }">
            {{ row.threshold }} 分
          </template>
        </el-table-column>
        <el-table-column label="超出" width="100">
          <template #default="{ row }">
            <span class="exceed">{{ row.overScore }} 分</span>
          </template>
        </el-table-column>
        <el-table-column prop="answerTime" label="作答时间" width="180">
          <template #default="{ row }">{{ formatTime(row.answer_time || row.answerTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="success" size="small" plain @click="openDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination" v-if="warnings.length">
        <el-pagination
          background
          layout="prev, pager, next, jumper"
          :total="total"
          :page-size="query.pageSize"
          :current-page="query.page"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <el-drawer v-model="detailVisible" title="预警详情" size="60%">
      <div v-if="detailData">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="学生姓名">{{ detailData.userName }}</el-descriptions-item>
          <el-descriptions-item label="学生ID">{{ detailData.userId }}</el-descriptions-item>
          <el-descriptions-item label="问卷">{{ detailData.testTitle }}</el-descriptions-item>
          <el-descriptions-item label="得分">
            <span class="score" :class="{ 'score-warning': detailData.score > detailData.threshold }">
              {{ detailData.score }} 分
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="阈值">{{ detailData.threshold }} 分</el-descriptions-item>
          <el-descriptions-item label="超出" :span="2">
            <span class="exceed">{{ detailData.overScore }} 分</span>
          </el-descriptions-item>
          <el-descriptions-item label="作答时间" :span="2">
            {{ formatTime(detailData.answer_time || detailData.answerTime) }}
          </el-descriptions-item>
        </el-descriptions>
        <div class="alert-box">
          <p>⚠️ 该学生已超过心理问卷设定的预警阈值，请优先联系、跟进</p>
          <p>建议适当安排心理疏导或预约面谈，并留存沟通记录。</p>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { useUserStore } from '@/stores/user'
import { getTestList, getAnswerList } from '@/api/mentaltest'
import { getUser } from '@/api/adminUser'

const userStore = useUserStore()
const warnings = ref([])
const total = ref(0)
const loading = ref(false)
const detailVisible = ref(false)
const detailData = ref(null)
const query = reactive({ page: 1, pageSize: 10 })
const testsMap = ref({})
const userCache = reactive({})

const formatTime = (val) => (val ? dayjs(val).format('YYYY-MM-DD HH:mm') : '-')

const loadUserNames = async (ids) => {
  const tasks = ids
    .filter(id => id && !userCache[id])
    .map(async (id) => {
      try {
        const res = await getUser(id)
        const u = res?.data || {}
        userCache[id] = u.real_name || u.realName || u.name || u.nickname || id
      } catch (e) {
        console.error(e)
        userCache[id] = id
      }
    })
  await Promise.all(tasks)
}

const transformWarnings = (answers) => {
  const mapped = []
  answers.forEach((answer) => {
    const testId = answer.test_id || answer.testId
    const testInfo = testsMap.value[testId]
    if (!testInfo) return
    const threshold = testInfo.threshold || 0
    if (!threshold) return
    const score = Number(answer.score) || 0
    if (typeof score !== 'number') return
    if (score <= threshold) return
    const overScore = score - threshold
    mapped.push({
      ...answer,
      score,
      testTitle: testInfo.title,
      threshold,
      overScore,
      userId: answer.user_id || answer.userId
    })
  })
  return mapped
}

const fetchWarnings = async () => {
  loading.value = true
  try {
    const testsRes = await getTestList(1, 1000, '', userStore.userId)
    const testRecords = testsRes?.data?.records || []
    const map = {}
    testRecords.forEach((test) => {
      map[test.id] = {
        title: test.title,
        threshold: test.threshold || 0
      }
    })
    testsMap.value = map

    const res = await getAnswerList(query.page, query.pageSize)
    const records = res?.data?.records || []
    const mappedWarnings = transformWarnings(records)
    const userIds = [...new Set(mappedWarnings.map(item => item.userId))]
    await loadUserNames(userIds)
    warnings.value = mappedWarnings.map(item => ({
      ...item,
      userName: item.real_name || item.realName || userCache[item.userId] || item.userId
    }))
    total.value = res?.data?.total || 0
  } catch (e) {
    console.error(e)
    ElMessage.error('加载心理预警数据失败')
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  query.page = page
  fetchWarnings()
}

const handleRefresh = () => {
  query.page = 1
  fetchWarnings()
}

const openDetail = (row) => {
  detailData.value = row
  detailVisible.value = true
}

onMounted(() => {
  fetchWarnings()
})
</script>

<style scoped>
.warning-alert {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.actions {
  display: flex;
  gap: 8px;
}

.score {
  font-weight: 600;
  color: #409eff;
}

.score-warning {
  color: #f56c6c;
}

.exceed {
  font-weight: 600;
  color: #f56c6c;
}

.alert-box {
  margin-top: 20px;
  padding: 12px;
  background-color: #fff0f0;
  border-left: 4px solid #f56c6c;
  color: #f56c6c;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}
</style>
