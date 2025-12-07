<template>
  <div class="appointments-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的预约</span>
          <el-button type="primary" @click="handleCreate">新建预约</el-button>
        </div>
      </template>

      <!-- 预约列表 -->
      <el-table :data="appointmentList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="预约ID" width="180" />
        <el-table-column prop="counselorId" label="咨询师" width="120">
          <template #default="{ row }">
            {{ getCounselorName(row.counselorId) || row.counselorId }}
          </template>
        </el-table-column>
        <el-table-column prop="appointmentTime" label="预约时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.appointmentTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="type" label="咨询类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 1 ? 'success' : 'info'">
              {{ row.type === 1 ? '线上' : '线下' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="评分" width="150">
          <template #default="{ row }">
            <el-rate v-if="row.rating" :model-value="row.rating" disabled />
            <span v-else>未评分</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button size="small" @click="handleDetail(row)">详情</el-button>
            <el-button
              v-if="row.status === 1 || row.status === 2"
              size="small"
              type="danger"
              @click="handleCancel(row)"
            >
              取消
            </el-button>
            <el-button
              v-if="row.status === 3 && (!row.rating || row.rating === 0 || row.rating === null)"
              size="small"
              type="warning"
              @click="handleRate(row)"
            >
              评分
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchAppointments"
        @current-change="fetchAppointments"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 新建预约对话框 -->
    <el-dialog v-model="createDialogVisible" title="新建预约" width="500px">
      <el-form :model="createForm" :rules="createRules" ref="createFormRef" label-width="100px">
        <el-form-item label="咨询师" prop="counselorId">
          <el-select v-model="createForm.counselorId" placeholder="请选择咨询师" style="width: 100%">
            <el-option
              v-for="counselor in counselorList"
              :key="counselor.userId"
              :label="`${counselor.name} (${counselor.specialty})`"
              :value="counselor.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker
            v-model="createForm.appointmentTime"
            type="datetime"
            placeholder="选择预约时间"
            style="width: 100%"
            :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item label="咨询类型" prop="type">
          <el-radio-group v-model="createForm.type">
            <el-radio :label="1">线上</el-radio>
            <el-radio :label="2">线下</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCreate">确定</el-button>
      </template>
    </el-dialog>

    <!-- 评分对话框 -->
    <el-dialog v-model="rateDialogVisible" title="预约评分" width="400px">
      <el-form :model="rateForm" ref="rateFormRef" label-width="80px">
        <el-form-item label="评分">
          <el-input-number
            v-model="rateForm.rating"
            :min="0"
            :max="5"
            :step="0.1"
            :precision="1"
            placeholder="请输入评分（0-5）"
            style="width: 100%"
          />
          <div style="margin-top: 10px; font-size: 12px; color: #999;">
            请输入0-5的评分，保留一位小数
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRate">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="预约详情" width="500px">
      <el-descriptions :column="1" border v-if="currentAppointment">
        <el-descriptions-item label="预约ID">{{ currentAppointment.id }}</el-descriptions-item>
        <el-descriptions-item label="咨询师">{{ counselorInfo.real_name || currentAppointment.counselorId }}</el-descriptions-item>
        <el-descriptions-item label="预约时间">{{ formatDateTime(currentAppointment.appointmentTime) }}</el-descriptions-item>
        <el-descriptions-item label="咨询类型">
          <el-tag :type="currentAppointment.type === 1 ? 'success' : 'info'">
            {{ currentAppointment.type === 1 ? '线上' : '线下' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentAppointment.status)">
            {{ getStatusText(currentAppointment.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="评分">
          <el-rate v-if="currentAppointment.rating" :model-value="currentAppointment.rating" disabled />
          <span v-else>未评分</span>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAppointmentList, createAppointment, cancelAppointment, getAppointment, updateAppointment } from '@/api/appointment'
import { getCounselorList } from '@/api/counselor'
import { getUser } from '@/api/adminUser'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 列表数据
const appointmentList = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 咨询师列表
const counselorList = ref([])

// 新建预约
const createDialogVisible = ref(false)
const createFormRef = ref(null)
const createForm = ref({
  counselorId: '',
  appointmentTime: '',
  type: 1
})
const createRules = {
  counselorId: [{ required: true, message: '请选择咨询师', trigger: 'change' }],
  appointmentTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }],
  type: [{ required: true, message: '请选择咨询类型', trigger: 'change' }]
}

// 评分
const rateDialogVisible = ref(false)
const rateFormRef = ref(null)
const rateForm = ref({
  rating: null
})
const currentRateId = ref(null)

// 详情
const detailDialogVisible = ref(false)
const currentAppointment = ref(null)
const counselorInfo = ref({
  real_name: ''
})

// 咨询师信息缓存
const counselorInfoCache = ref({})

// 获取预约列表
const fetchAppointments = async () => {
  try {
    loading.value = true
    const res = await getAppointmentList(page.value, pageSize.value, userStore.userId, null)
    const records = res.data.records || []
    appointmentList.value = records
    total.value = res.data.total || 0
    
    // 预加载咨询师信息
    for (const appointment of records) {
      if (!counselorInfoCache.value[appointment.counselorId]) {
        try {
          const userRes = await getUser(appointment.counselorId)
          counselorInfoCache.value[appointment.counselorId] = userRes.data
        } catch (error) {
          console.error(`获取咨询师 ${appointment.counselorId} 信息失败:`, error)
        }
      }
    }
  } catch (error) {
    console.error('获取预约列表失败:', error)
    ElMessage.error('获取预约列表失败')
  } finally {
    loading.value = false
  }
}

// 获取咨询师列表
const fetchCounselors = async () => {
  try {
    const res = await getCounselorList(1, 1000, '')
    counselorList.value = res.data.records || []
  } catch (error) {
    console.error('获取咨询师列表失败:', error)
  }
}

// 获取咨询师姓名
const getCounselorName = (counselorId) => {
  return counselorInfoCache.value[counselorId]?.real_name
}

// 新建预约
const handleCreate = async () => {
  await fetchCounselors()
  createDialogVisible.value = true
}

const submitCreate = async () => {
  try {
    await createFormRef.value?.validate()
    const data = {
      counselor_id: createForm.value.counselorId,
      appointment_time: createForm.value.appointmentTime,
      type: createForm.value.type
    }
    await createAppointment(data)
    ElMessage.success('预约创建成功')
    createDialogVisible.value = false
    createForm.value = {
      counselorId: '',
      appointmentTime: '',
      type: 1
    }
    fetchAppointments()
  } catch (error) {
    if (error !== false) {
      console.error('创建预约失败:', error)
      ElMessage.error('创建预约失败')
    }
  }
}

// 取消预约
const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm('确定要取消这个预约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await cancelAppointment(row.id)
    ElMessage.success('预约已取消')
    fetchAppointments()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消预约失败:', error)
      ElMessage.error('取消预约失败')
    }
  }
}

// 评分
const handleRate = (row) => {
  currentRateId.value = row.id
  rateForm.value.rating = null
  rateDialogVisible.value = true
}

const submitRate = async () => {
  try {
    if (rateForm.value.rating === 0 || rateForm.value.rating === null) {
      ElMessage.warning('请选择评分')
      return
    }
    // 将评分保留一位小数，转换为浮点数发送给后端
    const rating = parseFloat(rateForm.value.rating.toFixed(1))
    await updateAppointment(currentRateId.value, { rating })
    ElMessage.success('评分成功')
    rateDialogVisible.value = false
    fetchAppointments()
  } catch (error) {
    console.error('评分失败:', error)
    ElMessage.error('评分失败')
  }
}

// 详情
const handleDetail = async (row) => {
  try {
    const res = await getAppointment(row.id)
    currentAppointment.value = res.data
    
    // 获取或使用缓存中的咨询师信息
    if (counselorInfoCache.value[row.counselorId]) {
      counselorInfo.value = counselorInfoCache.value[row.counselorId]
    } else {
      try {
        const userRes = await getUser(row.counselorId)
        counselorInfo.value = userRes.data
        counselorInfoCache.value[row.counselorId] = userRes.data
      } catch (error) {
        console.error('获取咨询师信息失败:', error)
        counselorInfo.value = { real_name: '' }
      }
    }
    
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取预约详情失败:', error)
    ElMessage.error('获取预约详情失败')
  }
}

// 状态文本
const getStatusText = (status) => {
  const statusMap = {
    1: '待确认',
    2: '未完成',
    3: '已完成',
    4: '爽约'
  }
  return statusMap[status] || '未知'
}

// 状态类型
const getStatusType = (status) => {
  const typeMap = {
    1: 'warning',
    2: 'info',
    3: 'success',
    4: 'danger'
  }
  return typeMap[status] || ''
}

// 格式化时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 24 * 60 * 60 * 1000
}

onMounted(() => {
  fetchAppointments()
})
</script>

<style scoped>
.appointments-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
