<template>
  <div class="treehole-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>树洞帖子管理</span>
          <div class="actions">
            <el-input
              v-model="query.keyword"
              size="small"
              placeholder="搜索标题关键词"
              clearable
              style="width: 220px"
              @clear="handleSearch"
              @keyup.enter.native="handleSearch"
            />
            <el-select v-model="query.pass" size="small" style="width: 140px" placeholder="审核状态" clearable>
              <el-option label="全部" :value="undefined" />
              <el-option label="待审核" :value="0" />
              <el-option label="已通过" :value="1" />
            </el-select>
            <el-button type="primary" size="small" @click="handleSearch">查询</el-button>
          </div>
        </div>
      </template>

      <el-table :data="posts" v-loading="loading" border stripe>
        <el-table-column type="index" width="60" label="#" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="content" label="内容摘要" min-width="300">
          <template #default="{ row }">{{ contentSummary(row.content) }}</template>
        </el-table-column>
        <el-table-column prop="post_time" label="发布时间" width="180">
          <template #default="{ row }">{{ formatTime(row.post_time || row.postTime) }}</template>
        </el-table-column>
        <el-table-column prop="pass" label="审核状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.pass === 1 ? 'success' : 'warning'">
              {{ row.pass === 1 ? '已通过' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" type="primary" plain @click="openDetail(row)">查看</el-button>
            <el-button size="small" type="success" plain :disabled="row.pass === 1" @click="approve(row)">通过</el-button>
            <el-button size="small" type="danger" plain @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
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

    <el-drawer v-model="detailVisible" title="帖子详情" size="60%" :with-header="true">
      <template #title>
        <div class="detail-title">
          <div class="title-text">{{ detailPost?.title }}</div>
          <div class="title-meta">发布时间：{{ formatTime(detailPost?.post_time || detailPost?.postTime) }}</div>
        </div>
      </template>
      <div class="detail-content" v-html="detailPost?.content"></div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { getPostList, updatePost, deletePost } from '@/api/treehole'

const posts = ref([])
const total = ref(0)
const loading = ref(false)
const detailVisible = ref(false)
const detailPost = ref(null)

const query = reactive({
  page: 1,
  pageSize: 10,
  keyword: '',
  pass: undefined
})

const formatTime = (val) => (val ? dayjs(val).format('YYYY-MM-DD HH:mm') : '-')
const contentSummary = (html) => {
  const text = html ? html.replace(/<[^>]+>/g, '') : ''
  if (!text) return '-'
  return text.length > 80 ? `${text.slice(0, 80)}...` : text
}

const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await getPostList(query.page, query.pageSize, query.keyword, query.pass)
    posts.value = res?.data?.records || res?.data?.list || []
    total.value = res?.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  query.page = page
  fetchPosts()
}

const handleSearch = () => {
  query.page = 1
  fetchPosts()
}

const openDetail = (row) => {
  detailPost.value = row
  detailVisible.value = true
}

const approve = async (row) => {
  await updatePost(row.id, { pass: 1, title: row.title, content: row.content })
  ElMessage.success('已通过审核')
  fetchPosts()
}

const remove = (row) => {
  ElMessageBox.confirm('确认删除该帖子？', '提示', { type: 'warning' })
    .then(async () => {
      await deletePost(row.id)
      ElMessage.success('已删除')
      fetchPosts()
    })
    .catch(() => {})
}

onMounted(() => {
  fetchPosts()
})
</script>

<style scoped>
.treehole-manage {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.detail-title {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.title-text {
  font-size: 18px;
  font-weight: 600;
}

.title-meta {
  color: #909399;
  font-size: 12px;
}

.detail-content {
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  background: #fafafa;
  min-height: 160px;
  word-break: break-word;
}
</style>
