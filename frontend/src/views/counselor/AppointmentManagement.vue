<template>
  <div class="appointment-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>预约管理</span>
        </div>
      </template>

      <!-- 筛选条件 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 150px">
            <el-option label="待确认" :value="1" />
            <el-option label="未完成" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="爽约" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 预约列表 -->
      <el-table :data="appointmentList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="预约ID" width="180" />
        <el-table-column prop="studentId" label="学生" width="120">
          <template #default="{ row }">
            {{ getStudentName(row.studentId) || row.studentId }}
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
        <el-table-column label="操作" width="350">
          <template #default="{ row }">
            <el-button size="small" @click="handleDetail(row)">详情</el-button>
            <el-button
              v-if="row.status === 1"
              size="small"
              type="success"
              @click="handleConfirm(row)"
            >
              确认
            </el-button>
            <el-button
              v-if="row.status !== 1"
              size="small"
              type="primary"
              @click="handleEdit(row)"
            >
              编辑
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

    <!-- 编辑对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑预约状态" width="400px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="状态">
          <el-select v-model="editForm.status" placeholder="请选择状态">
            <el-option label="待确认" :value="1" />
            <el-option label="未完成" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="爽约" :value="4" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="预约详情" width="500px">
      <el-descriptions :column="1" border v-if="currentAppointment">
        <el-descriptions-item label="预约ID">{{ currentAppointment.id }}</el-descriptions-item>
        <el-descriptions-item label="学生">{{ studentInfo.real_name || currentAppointment.studentId }}</el-descriptions-item>
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
import { getAppointmentList, getAppointment, updateAppointment } from '@/api/appointment'
import { getUser } from '@/api/adminUser'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 列表数据
const appointmentList = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索条件
const searchForm = ref({
  status: null
})

// 详情
const detailDialogVisible = ref(false)
const currentAppointment = ref(null)
const studentInfo = ref({
  real_name: ''
})

// 学生信息缓存
const studentInfoCache = ref({})

// 编辑
const editDialogVisible = ref(false)
const editForm = ref({
  status: null
})
const currentEditId = ref(null)

// 获取预约列表
const fetchAppointments = async () => {
  try {
    loading.value = true
    const res = await getAppointmentList(page.value, pageSize.value, null, userStore.userId)
    
    // 直接使用后端返回的全部记录，不进行客户端过滤
    let records = res.data.records || []
    appointmentList.value = records
    total.value = res.data.total || 0
    
    // 预加载学生信息
    for (const appointment of records) {
      if (!studentInfoCache.value[appointment.studentId]) {
        try {
          const userRes = await getUser(appointment.studentId)
          studentInfoCache.value[appointment.studentId] = userRes.data
        } catch (error) {
          console.error(`获取学生 ${appointment.studentId} 信息失败:`, error)
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

// 获取学生姓名
const getStudentName = (studentId) => {
  return studentInfoCache.value[studentId]?.real_name
}

// 重置搜索
const resetSearch = () => {
  searchForm.value.status = null
  page.value = 1
  fetchAppointments()
}

// 处理搜索查询
const handleSearch = () => {
  page.value = 1
  loadFilteredAppointments()
}

// 加载并根据状态过滤预约列表
const loadFilteredAppointments = async () => {
  try {
    loading.value = true
    const res = await getAppointmentList(page.value, pageSize.value, null, userStore.userId)
    
    // 根据搜索条件过滤
    let records = res.data.records || []
    if (searchForm.value.status !== null && searchForm.value.status !== undefined) {
      records = records.filter(item => item.status === searchForm.value.status)
    }
    
    appointmentList.value = records
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取预约列表失败:', error)
    ElMessage.error('获取预约列表失败')
  } finally {
    loading.value = false
  }
}

// 确认预约
const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm('确认接受这个预约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    
    // 更新状态为未完成，发送 {"status": 2}
    await updateAppointment(row.id, { status: 2 })
    ElMessage.success('预约已确认')
    fetchAppointments()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('确认预约失败:', error)
      ElMessage.error('确认预约失败')
    }
  }
}

// 编辑预约状态
const handleEdit = (row) => {
  currentEditId.value = row.id
  editForm.value.status = row.status
  editDialogVisible.value = true
}

const submitEdit = async () => {
  try {
    if (editForm.value.status === null || editForm.value.status === undefined) {
      ElMessage.warning('请选择状态')
      return
    }
    
    await updateAppointment(currentEditId.value, { status: editForm.value.status })
    ElMessage.success('预约状态已更新')
    editDialogVisible.value = false
    fetchAppointments()
  } catch (error) {
    console.error('更新预约失败:', error)
    ElMessage.error('更新预约失败')
  }
}

// 完成预约
const handleComplete = async (row) => {
  try {
    await ElMessageBox.confirm('确认完成这个预约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success'
    })
    
    // 更新状态为已完成
    await updateAppointment(row.id, { status: 3 })
    ElMessage.success('预约已完成')
    fetchAppointments()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('完成预约失败:', error)
      ElMessage.error('完成预约失败')
    }
  }
}

// 标记爽约
const handleNoShow = async (row) => {
  try {
    await ElMessageBox.confirm('确认标记学生爽约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 更新状态为爽约
    await updateAppointment(row.id, { status: 4 })
    ElMessage.success('已标记为爽约')
    fetchAppointments()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('标记爽约失败:', error)
      ElMessage.error('标记爽约失败')
    }
  }
}

// 详情
const handleDetail = async (row) => {
  try {
    const res = await getAppointment(row.id)
    currentAppointment.value = res.data
    
    // 获取或使用缓存中的学生信息
    if (studentInfoCache.value[row.studentId]) {
      studentInfo.value = studentInfoCache.value[row.studentId]
    } else {
      try {
        const userRes = await getUser(row.studentId)
        studentInfo.value = userRes.data
        studentInfoCache.value[row.studentId] = userRes.data
      } catch (error) {
        console.error('获取学生信息失败:', error)
        studentInfo.value = { realName: '' }
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

onMounted(() => {
  fetchAppointments()
})
</script>

<style scoped>
.appointment-management-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>
