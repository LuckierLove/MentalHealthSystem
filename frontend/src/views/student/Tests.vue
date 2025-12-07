<template>
  <div class="test-answer">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>心理测试</span>
        </div>
      </template>

      <el-table :data="tests" v-loading="loading" border stripe>
        <el-table-column type="index" width="60" label="#" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="create_time" label="发布时间" width="160">
          <template #default="{ row }">{{ formatTime(row.create_time || row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="getAnswerStatus(row.id).answered" type="success">已作答</el-tag>
            <el-tag v-else type="info">未作答</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" type="primary" plain @click="openAnswer(row)" v-if="!getAnswerStatus(row.id).answered">作答</el-button>
            <el-button size="small" type="success" plain @click="openResult(row)" v-else>查看结果</el-button>
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

    <!-- 作答问卷对话框 -->
    <el-dialog v-model="answerDialogVisible" :title="`作答问卷：${answerTest?.title}`" width="700px" @close="resetAnswerForm">
      <el-form v-if="answerTest" :model="answerForm" label-width="0">
        <div v-for="(question, qIndex) in parsedContent" :key="qIndex" style="margin-bottom: 20px; padding: 12px; border: 1px solid #e4e7ed; border-radius: 6px">
          <div style="font-weight: 600; margin-bottom: 12px">{{ qIndex + 1 }}. {{ question.question }}</div>
          <el-form-item :prop="`answers.${qIndex}`">
            <el-radio-group v-model="answerForm.answers[qIndex]">
              <el-radio v-for="(option, oIndex) in question.choose" :key="oIndex" :label="option.value">
                {{ option.text }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
      </el-form>

      <template #footer>
        <el-button @click="answerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAnswer">提交作答</el-button>
      </template>
    </el-dialog>

    <!-- 查看作答结果对话框 -->
    <el-dialog v-model="resultDialogVisible" :title="`作答结果：${resultTest?.title}`" width="700px">
      <div v-if="resultAnswer">
        <div style="margin-bottom: 20px; padding: 12px; background-color: #f0f9ff; border-left: 4px solid #409EFF; border-radius: 4px">
          <p style="margin: 0 0 8px 0"><strong>作答时间：</strong>{{ formatTime(resultAnswer.answer_time || resultAnswer.answerTime) }}</p>
          <p style="margin: 0 0 8px 0">
            <strong>得分：</strong>
            <span class="score-text" :class="{ 'score-warning': isOverThreshold(resultAnswer.score || 0, resultTest?.threshold) }">
              {{ resultAnswer.score || 0 }}/{{ getTotalScore() }} 分
            </span>
          </p>
          <p v-if="resultTest?.threshold" style="margin: 0">
            <strong>警告阈值：</strong>{{ resultTest.threshold }}分
            <el-tag v-if="isOverThreshold(resultAnswer.score || 0, resultTest?.threshold)" type="warning" style="margin-left: 8px">超过警告阈值</el-tag>
            <el-tag v-else type="success" style="margin-left: 8px">未超过警告阈值</el-tag>
          </p>
        </div>
        <h4>详细作答情况</h4>
        <div v-for="(question, qIndex) in parsedResultContent" :key="qIndex" style="margin-bottom: 16px; padding: 12px; border: 1px solid #e4e7ed; border-radius: 6px">
          <p style="margin: 0 0 8px 0; font-weight: 600">{{ qIndex + 1 }}. {{ question.question }}</p>
          <div>
            <div v-for="(option, oIndex) in question.choose" :key="oIndex" style="margin: 4px 0; padding: 4px 8px; border-radius: 4px" :style="getOptionStyle(option.value, resultAnswerMap[qIndex]?.choose)">
              <span>{{ option.text }}</span>
              <span style="margin-left: 8px; color: #909399">({{ option.value }}分)</span>
              <el-icon v-if="option.value === resultAnswerMap[qIndex]?.choose" style="margin-left: 8px; color: #67C23A"><SuccessFilled /></el-icon>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="resultDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { SuccessFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getTestList, createAnswer, getAnswerList, getAnswer } from '@/api/mentaltest'

const userStore = useUserStore()
const tests = ref([])
const total = ref(0)
const loading = ref(false)
const answerDialogVisible = ref(false)
const answerTest = ref(null)
const resultDialogVisible = ref(false)
const resultTest = ref(null)
const resultAnswer = ref(null)
const resultAnswerMap = ref({})
const answeredTests = ref({})

const query = reactive({
  page: 1,
  pageSize: 10,
  keyword: ''
})

const answerForm = reactive({
  answers: []
})

const formatTime = (val) => (val ? dayjs(val).format('YYYY-MM-DD HH:mm') : '-')

const parsedContent = computed(() => {
  if (!answerTest.value?.content) return []
  try {
    return JSON.parse(answerTest.value.content)
  } catch {
    return []
  }
})

const parsedResultContent = computed(() => {
  if (!resultTest.value?.content) return []
  try {
    return JSON.parse(resultTest.value.content)
  } catch {
    return []
  }
})

const fetchTests = async () => {
  loading.value = true
  try {
    const res = await getTestList(query.page, query.pageSize)
    tests.value = res?.data?.records || []
    total.value = res?.data?.total || 0
    // 加载当前学生的作答记录
    await loadAnsweredTests()
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const loadAnsweredTests = async () => {
  try {
    const res = await getAnswerList(1, 1000, null, userStore.userId)
    const answers = res?.data?.records || []
    answeredTests.value = {}
    answers.forEach(answer => {
      if (!answeredTests.value[answer.test_id || answer.testId]) {
        answeredTests.value[answer.test_id || answer.testId] = answer
      }
    })
  } catch (e) {
    console.error(e)
  }
}

const getAnswerStatus = (testId) => {
  return {
    answered: !!answeredTests.value[testId],
    answer: answeredTests.value[testId]
  }
}

const getTotalScore = () => {
  if (!parsedResultContent.value) return 0
  let total = 0
  parsedResultContent.value.forEach(question => {
    question.choose?.forEach(option => {
      total += option.value || 0
    })
  })
  return total
}

const isOverThreshold = (score, threshold) => {
  if (!threshold || threshold === 0) return false
  return score > threshold
}

const getOptionStyle = (optionValue, selectedValue) => {
  if (optionValue === selectedValue) {
    return {
      backgroundColor: '#c6e2ff',
      borderLeft: '3px solid #409EFF'
    }
  }
  return {}
}

const resetAnswerForm = () => {
  answerForm.answers = []
  answerTest.value = null
}

const handlePageChange = (page) => {
  query.page = page
  fetchTests()
}

const openAnswer = (row) => {
  answerTest.value = row
  const content = JSON.parse(row.content || '[]')
  answerForm.answers = new Array(content.length).fill(null)
  answerDialogVisible.value = true
}

const openResult = async (row) => {
  resultTest.value = row
  const answerStatus = getAnswerStatus(row.id)
  if (answerStatus.answered) {
    resultAnswer.value = answerStatus.answer
    const answerContent = JSON.parse(answerStatus.answer.content || '[]')
    resultAnswerMap.value = {}
    answerContent.forEach(item => {
      resultAnswerMap.value[item.question - 1] = item
    })
    resultDialogVisible.value = true
  }
}

const submitAnswer = async () => {
  if (answerForm.answers.some(a => a === null || a === undefined)) {
    ElMessage.warning('请完成所有问题的作答')
    return
  }

  const answerContent = answerForm.answers.map((chooseValue, qIndex) => ({
    question: qIndex + 1,
    choose: chooseValue
  }))

  const payload = {
    test_id: answerTest.value.id,
    content: JSON.stringify(answerContent),
    answer_time: new Date().toISOString(),
    score: calculateScore(answerContent)
  }

  await createAnswer(payload)
  ElMessage.success('作答成功')
  answerDialogVisible.value = false
  fetchTests()
}

const calculateScore = (answers) => {
  const testContent = parsedContent.value
  let score = 0
  answers.forEach(answer => {
    const question = testContent[answer.question - 1]
    if (question) {
      const option = question.choose.find(c => c.value === answer.choose)
      if (option) {
        score += option.value
      }
    }
  })
  return score
}

onMounted(() => {
  fetchTests()
})
</script>

<style scoped>
.test-answer {
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

.score-text {
  font-weight: 600;
  font-size: 16px;
  color: #67C23A;
}

.score-text.score-warning {
  color: #E6A23C;
}
</style>
