<template>
  <div class="treehole-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>心情树洞</span>
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
            <el-button type="primary" size="small" @click="openCreate">发帖</el-button>
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
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button size="small" type="primary" plain @click="openDetail(row)">查看</el-button>
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑帖子' : '发布帖子'" width="760px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div class="rich-editor">
            <div class="toolbar">
              <el-button-group>
                <el-button size="small" text @mousedown.prevent @click="applyCommand('bold')"><b>B</b></el-button>
                <el-button size="small" text @mousedown.prevent @click="applyCommand('italic')"><i>I</i></el-button>
                <el-button size="small" text @mousedown.prevent @click="applyCommand('underline')"><u>U</u></el-button>
                <el-button size="small" text @mousedown.prevent @click="applyCommand('insertUnorderedList')">• 列表</el-button>
                <el-button size="small" text @mousedown.prevent @click="applyCommand('insertOrderedList')">1. 列表</el-button>
                <el-button size="small" text @mousedown.prevent @click="applyCommand('removeFormat')">清除格式</el-button>
              </el-button-group>
            </div>
            <div
              ref="editorRef"
              class="editor-area"
              contenteditable="true"
              @input="onEditorInput"
              @blur="onEditorInput"
            ></div>
            <div class="editor-hint">支持粘贴富文本，保存为 HTML 内容。</div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPost">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="帖子详情" size="60%" :with-header="true">
      <template #title>
        <div class="detail-title">
          <div class="title-text">{{ detailPost?.title }}</div>
          <div class="title-meta">发布时间：{{ formatTime(detailPost?.post_time || detailPost?.postTime) }}</div>
        </div>
      </template>
      <div class="detail-content" v-html="detailPost?.content"></div>
      <div class="reply-section">
        <div class="reply-header">回复</div>
        <el-empty v-if="!replies.length" description="暂无回复" />
        <el-timeline v-else>
          <el-timeline-item
            v-for="reply in replies"
            :key="reply.id"
            :timestamp="formatTime(reply.reply_time || reply.replyTime)"
            placement="top"
          >
            <div class="reply-card" v-html="reply.content"></div>
          </el-timeline-item>
        </el-timeline>
        <div class="reply-editor">
          <div class="editor-label">写回复</div>
          <div class="rich-editor">
            <div class="toolbar">
              <el-button-group>
                <el-button size="small" text @mousedown.prevent @click="applyReplyCommand('bold')"><b>B</b></el-button>
                <el-button size="small" text @mousedown.prevent @click="applyReplyCommand('italic')"><i>I</i></el-button>
                <el-button size="small" text @mousedown.prevent @click="applyReplyCommand('underline')"><u>U</u></el-button>
                <el-button size="small" text @mousedown.prevent @click="applyReplyCommand('insertUnorderedList')">• 列表</el-button>
                <el-button size="small" text @mousedown.prevent @click="applyReplyCommand('insertOrderedList')">1. 列表</el-button>
                <el-button size="small" text @mousedown.prevent @click="applyReplyCommand('removeFormat')">清除格式</el-button>
              </el-button-group>
            </div>
            <div
              ref="replyEditorRef"
              class="editor-area"
              contenteditable="true"
              @input="onReplyInput"
              @blur="onReplyInput"
            ></div>
            <div class="editor-hint">回复内容保存为 HTML。</div>
          </div>
          <div class="reply-actions">
            <el-button size="small" @click="clearReply">清空</el-button>
            <el-button size="small" type="primary" @click="submitReply">提交回复</el-button>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, computed } from 'vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { useUserStore } from '@/stores/user'
import { getPostList, createPost, getPost, listReplies, createReply } from '@/api/treehole'

const posts = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const editorRef = ref(null)
const replyEditorRef = ref(null)
const detailPost = ref(null)
const replies = ref([])
const replyContent = ref('')
const userStore = useUserStore()
const isAdmin = computed(() => userStore.hasRole('管理员'))

const query = reactive({
  page: 1,
  pageSize: 10,
  keyword: '',
  pass: 1
})

const form = reactive({
  title: '',
  content: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const formatTime = (val) => (val ? dayjs(val).format('YYYY-MM-DD HH:mm') : '-')
const contentSummary = (html) => {
  const text = html ? html.replace(/<[^>]+>/g, '') : ''
  if (!text) return '-'
  return text.length > 80 ? `${text.slice(0, 80)}...` : text
}

const setEditorContent = (html = '') => {
  if (editorRef.value) {
    editorRef.value.innerHTML = html
  }
  form.content = html
}

const onEditorInput = () => {
  form.content = editorRef.value?.innerHTML?.trim() || ''
}

const applyCommand = (command) => {
  document.execCommand(command, false, null)
  onEditorInput()
}

const setReplyContent = (html = '') => {
  if (replyEditorRef.value) {
    replyEditorRef.value.innerHTML = html
  }
  replyContent.value = html
}

const onReplyInput = () => {
  replyContent.value = replyEditorRef.value?.innerHTML?.trim() || ''
}

const applyReplyCommand = (command) => {
  document.execCommand(command, false, null)
  onReplyInput()
}

const fetchPosts = async () => {
  loading.value = true
  try {
    const passParam = isAdmin.value ? query.pass : 1
    const res = await getPostList(query.page, query.pageSize, query.keyword, passParam)
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

const resetForm = () => {
  form.title = ''
  form.content = ''
  isEdit.value = false
  setEditorContent('')
}

const openCreate = () => {
  resetForm()
  dialogVisible.value = true
}

const openDetail = async (row) => {
  detailPost.value = row
  detailVisible.value = true
  fetchReplies(row.id)
}

const fetchReplies = async (postId) => {
  try {
    const res = await listReplies(postId)
    replies.value = res?.data || []
  } catch (e) {
    console.error(e)
  }
}

const submitPost = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    if (!form.content) {
      ElMessage.warning('请输入内容')
      return
    }
    await createPost({ title: form.title, content: form.content })
    ElMessage.success('发布成功，等待审核或展示')
    dialogVisible.value = false
    fetchPosts()
  })
}

const submitReply = async () => {
  if (!detailPost.value?.id) {
    ElMessage.warning('请先打开要回复的帖子')
    return
  }
  if (!replyContent.value) {
    ElMessage.warning('请输入回复内容')
    return
  }
  await createReply({ post_id: detailPost.value.id, content: replyContent.value })
  ElMessage.success('回复成功')
  setReplyContent('')
  fetchReplies(detailPost.value.id)
}

const clearReply = () => {
  setReplyContent('')
}

onMounted(() => {
  fetchPosts()
})
</script>

<style scoped>
.treehole-page {
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

.rich-editor {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  background: #fafafa;
}

.toolbar {
  padding: 6px 8px;
  border-bottom: 1px solid #e4e7ed;
  background: #f5f7fa;
}

.editor-area {
  min-height: 220px;
  padding: 10px;
  outline: none;
  background: #fff;
  border-radius: 0 0 6px 6px;
  word-break: break-word;
}

.editor-hint {
  padding: 8px 10px;
  font-size: 12px;
  color: #909399;
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

.reply-section {
  margin-top: 16px;
}

.reply-header {
  font-weight: 600;
  margin-bottom: 8px;
}

.reply-card {
  padding: 10px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  word-break: break-word;
}

.reply-editor {
  margin-top: 12px;
}

.editor-label {
  margin-bottom: 6px;
  font-weight: 600;
}

.reply-actions {
  margin-top: 8px;
  display: flex;
  gap: 8px;
}
</style>
