<template>
  <div class="test-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>问卷管理</span>
          <div class="actions">
            <el-input
              v-model="query.keyword"
              size="small"
              placeholder="搜索问卷标题"
              clearable
              style="width: 220px"
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            />
            <el-button type="primary" size="small" @click="openCreate">创建问卷</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tests" v-loading="loading" border stripe>
        <el-table-column type="index" width="60" label="#" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="description" label="描述" min-width="260" show-overflow-tooltip />
        <el-table-column prop="create_time" label="创建时间" width="180">
          <template #default="{ row }">{{ formatTime(row.create_time || row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" type="primary" plain @click="openDetail(row)">查看</el-button>
            <el-button size="small" type="success" plain @click="openEdit(row)">编辑</el-button>
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

    <!-- 创建/编辑问卷对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑问卷' : '创建问卷'" width="800px">
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入问卷标题" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入问卷描述" />
        </el-form-item>
        <el-form-item label="问题列表" prop="questions">
          <div class="questions-list">
            <el-card v-for="(question, qIndex) in form.questions" :key="qIndex" class="question-card">
              <template #header>
                <div style="display: flex; justify-content: space-between; align-items: center">
                  <span>问题 {{ qIndex + 1 }}</span>
                  <el-button type="danger" size="small" text @click="removeQuestion(qIndex)">删除</el-button>
                </div>
              </template>

              <el-form-item label="问题文本" :prop="`questions.${qIndex}.question`">
                <el-input v-model="question.question" placeholder="输入问题内容" />
              </el-form-item>

              <el-form-item label="选项">
                <div class="options-container">
                  <div class="options-header">
                    <span class="option-label">选项文本</span>
                    <span class="option-score">分值</span>
                    <span class="option-action">操作</span>
                  </div>
                  <div v-for="(option, oIndex) in question.choose" :key="oIndex" class="option-row">
                    <el-input v-model="option.text" placeholder="输入选项文本" size="small" class="option-input" />
                    <el-input-number
                      v-model="option.value"
                      :min="0"
                      :max="5"
                      size="small"
                      class="option-score-input"
                      placeholder="分值"
                    />
                    <el-button type="danger" size="small" text class="option-btn" @click="removeOption(qIndex, oIndex)">删除</el-button>
                  </div>
                </div>
                <el-button type="dashed" @click="addOption(qIndex)" style="margin-top: 12px; width: 100%">+ 添加选项</el-button>
              </el-form-item>
            </el-card>
          </div>
          <el-button type="dashed" @click="addQuestion" style="margin-top: 12px">+ 添加问题</el-button>
        </el-form-item>
        <el-form-item label="警告阈值" prop="threshold">
          <el-input-number v-model="form.threshold" :min="0" :max="100" placeholder="0-100分" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitTest">保存</el-button>
      </template>
    </el-dialog>

    <!-- 查看问卷详情 -->
    <el-drawer v-model="detailVisible" title="问卷详情" size="60%">
      <div v-if="detailTest">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="标题">{{ detailTest.title }}</el-descriptions-item>
          <el-descriptions-item label="描述">{{ detailTest.description }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatTime(detailTest.create_time || detailTest.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="警告阈值">{{ detailTest.threshold }}分</el-descriptions-item>
        </el-descriptions>
        <div style="margin-top: 20px">
          <h4>问卷内容</h4>
          <div v-for="(question, qIndex) in parsedContent" :key="qIndex" style="margin-bottom: 16px; padding: 12px; border: 1px solid #e4e7ed; border-radius: 6px">
            <p style="margin: 0 0 8px 0; font-weight: 600">{{ qIndex + 1 }}. {{ question.question }}</p>
            <div>
              <el-radio v-for="(option, oIndex) in question.choose" :key="oIndex" :label="option.value" disabled style="display: block; margin: 4px 0">
                {{ option.text }} ({{ option.value }}分)
              </el-radio>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { useUserStore } from '@/stores/user'
import { getTestList, createTest, updateTest, deleteTest } from '@/api/mentaltest'

const userStore = useUserStore()
const tests = ref([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const formRef = ref(null)
const detailTest = ref(null)

const query = reactive({
  page: 1,
  pageSize: 10,
  keyword: ''
})

const form = reactive({
  title: '',
  description: '',
  questions: [{ question: '', choose: [{ text: '', value: 1 }] }],
  threshold: 0
})

const rules = {
  title: [{ required: true, message: '请输入问卷标题', trigger: 'blur' }],
  description: [{ required: true, message: '请输入问卷描述', trigger: 'blur' }],
  questions: [{ required: true, message: '请至少添加一个问题', trigger: 'change' }]
}

const formatTime = (val) => (val ? dayjs(val).format('YYYY-MM-DD HH:mm') : '-')

const parsedContent = computed(() => {
  if (!detailTest.value?.content) return []
  try {
    return JSON.parse(detailTest.value.content)
  } catch {
    return []
  }
})

const fetchTests = async () => {
  loading.value = true
  try {
    const res = await getTestList(query.page, query.pageSize, query.keyword, userStore.userId)
    tests.value = res?.data?.records || []
    total.value = res?.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  query.page = page
  fetchTests()
}

const handleSearch = () => {
  query.page = 1
  fetchTests()
}

const resetForm = () => {
  form.title = ''
  form.description = ''
  form.questions = [{ question: '', choose: [{ text: '', value: 1 }] }]
  form.threshold = 0
  editingId.value = null
  isEdit.value = false
}

const openCreate = () => {
  resetForm()
  dialogVisible.value = true
}

const openEdit = (row) => {
  isEdit.value = true
  editingId.value = row.id
  form.title = row.title
  form.description = row.description
  form.threshold = row.threshold || 0
  try {
    form.questions = JSON.parse(row.content) || [{ question: '', choose: [{ text: '', value: 1 }] }]
  } catch {
    form.questions = [{ question: '', choose: [{ text: '', value: 1 }] }]
  }
  dialogVisible.value = true
}

const openDetail = (row) => {
  detailTest.value = row
  detailVisible.value = true
}

const addQuestion = () => {
  form.questions.push({ question: '', choose: [{ text: '', value: 1 }] })
}

const removeQuestion = (index) => {
  form.questions.splice(index, 1)
}

const addOption = (qIndex) => {
  form.questions[qIndex].choose.push({ text: '', value: 1 })
}

const removeOption = (qIndex, oIndex) => {
  form.questions[qIndex].choose.splice(oIndex, 1)
}

// 验证选项分值：必须是整数0-5且互不相同
const validateOptionValues = () => {
  for (let qIndex = 0; qIndex < form.questions.length; qIndex++) {
    const question = form.questions[qIndex]
    const values = question.choose.map(opt => opt.value)
    
    // 检查是否有重复的分值
    const uniqueValues = new Set(values)
    if (uniqueValues.size !== values.length) {
      ElMessage.error(`第 ${qIndex + 1} 个问题的选项分值不能重复`)
      return false
    }
    
    // 检查是否都是整数0-5
    for (let oIndex = 0; oIndex < question.choose.length; oIndex++) {
      const value = question.choose[oIndex].value
      if (value === null || value === undefined || !Number.isInteger(value) || value < 0 || value > 5) {
        ElMessage.error(`第 ${qIndex + 1} 个问题第 ${oIndex + 1} 个选项的分值必须是0-5之间的整数`)
        return false
      }
    }
  }
  return true
}

const submitTest = async () => {
  await formRef.value.validate()
  
  // 验证选项分值
  if (!validateOptionValues()) {
    return
  }
  
  const payload = {
    title: form.title,
    description: form.description,
    content: JSON.stringify(form.questions),
    threshold: form.threshold
  }
  const action = isEdit.value ? updateTest(editingId.value, payload) : createTest(payload)
  await action
  ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
  dialogVisible.value = false
  fetchTests()
}

const remove = (row) => {
  ElMessageBox.confirm('确认删除该问卷？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteTest(row.id)
      ElMessage.success('已删除')
      fetchTests()
    })
    .catch(() => {})
}

import { computed } from 'vue'
onMounted(() => {
  fetchTests()
})
</script>

<style scoped>
.test-manage {
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

.questions-list {
  max-height: 600px;
  overflow-y: auto;
}

.question-card {
  margin-bottom: 12px;
}

.options-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  width: 100%;
}

.options-header {
  display: grid;
  grid-template-columns: 1fr 120px 80px;
  gap: 8px;
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #dcdfe6;
  font-weight: 600;
  font-size: 12px;
  color: #606266;
}

.option-label,
.option-score,
.option-action {
  display: flex;
  align-items: center;
}

.option-row {
  display: grid;
  grid-template-columns: 1fr 120px 80px;
  gap: 8px;
  padding: 8px 12px;
  border-bottom: 1px solid #ebeef5;
  align-items: center;
}

.option-row:last-child {
  border-bottom: none;
}

.option-input {
  width: 100%;
}

.option-score-input {
  width: 100%;
}

.option-btn {
  padding: 4px 8px;
}
</style>
