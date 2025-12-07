<template>
  <div class="dialog-records">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>咨询记录</span>
          <el-button type="primary" @click="openCreate">新增记录</el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="content" label="内容摘要" min-width="260" show-overflow-tooltip>
          <template #default="{ row }">{{ contentSummary(row.content) }}</template>
        </el-table-column>
        <el-table-column prop="dialogTime" label="时间" width="200">
          <template #default="{ row }">{{ formatTime(row.dialogTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="260">
          <template #default="{ row }">
            <el-button size="small" type="primary" plain @click="openDetail(row)">详情</el-button>
            <el-button size="small" type="success" plain @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" plain @click="handleDelete(row.id)">删除</el-button>
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑记录' : '新增记录'" width="720px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="时间" prop="dialogTime">
          <el-date-picker
            v-model="form.dialogTime"
            type="datetime"
            placeholder="选择时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
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
            <div class="editor-hint">可直接粘贴富文本，保存为 HTML 字符串。</div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="咨询详情" width="720px">
      <el-descriptions :column="1" border v-if="detailRecord">
        <el-descriptions-item label="时间">{{ formatTime(detailRecord.dialogTime) }}</el-descriptions-item>
      </el-descriptions>
      <div class="detail-content" v-html="detailRecord?.content || '暂无内容'"></div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { useUserStore } from '@/stores/user'
import { getDialogList, createDialog, updateDialog, deleteDialog } from '@/api/dialog'

const userStore = useUserStore()
const records = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const detailRecord = ref(null)
const formRef = ref(null)
const editorRef = ref(null)

const query = reactive({
  page: 1,
  pageSize: 10
})

const form = reactive({
  dialogTime: '',
  content: ''
})

const rules = {
  dialogTime: [{ required: true, message: '请选择时间', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const formatTime = (val) => (val ? dayjs(val).format('YYYY-MM-DD HH:mm') : '-')
const contentSummary = (html) => {
  const text = html ? html.replace(/<[^>]+>/g, '') : ''
  if (!text) return '-'
  return text.length > 60 ? `${text.slice(0, 60)}...` : text
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

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getDialogList(query.page, query.pageSize, userStore.userId)
    records.value = res?.data?.records || []
    total.value = res?.data?.total || 0
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  form.dialogTime = ''
  form.content = ''
  editingId.value = null
  isEdit.value = false
  setEditorContent('')
}

const openCreate = () => {
  resetForm()
  dialogVisible.value = true
}

const openEdit = (row) => {
  form.dialogTime = row.dialogTime
  editingId.value = row.id
  isEdit.value = true
  dialogVisible.value = true
  nextTick(() => setEditorContent(row.content || ''))
}

const openDetail = (row) => {
  detailRecord.value = row
  detailVisible.value = true
}

const submit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    if (!form.content) {
      ElMessage.warning('请输入内容')
      return
    }
    const payload = {
      counselorId: userStore.userId,
      dialogTime: form.dialogTime,
      content: form.content
    }
    const action = isEdit.value
      ? updateDialog(editingId.value, payload)
      : createDialog(payload)
    await action
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchList()
  })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除该记录？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteDialog(id)
      ElMessage.success('删除成功')
      fetchList()
    })
    .catch(() => {})
}

const handlePageChange = (page) => {
  query.page = page
  fetchList()
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>

.dialog-records {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
  min-height: 200px;
  padding: 10px;
  outline: none;
  background: #fff;
  border-radius: 0 0 6px 6px;
}

.editor-hint {
  padding: 8px 10px;
  font-size: 12px;
  color: #909399;
}

.detail-content {
  margin-top: 12px;
  padding: 12px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  background: #fafafa;
  min-height: 120px;
  word-break: break-word;
}
</style>
