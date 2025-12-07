<template>
  <div class="counselors-container">
    <div class="header">
      <el-input
        v-model="keyword"
        placeholder="搜索咨询师姓名"
        style="width: 300px"
        clearable
        @clear="fetchCounselors"
        @keyup.enter="fetchCounselors"
      >
        <template #append>
          <el-button :icon="Search" @click="fetchCounselors" />
        </template>
      </el-input>
      <el-button type="primary" @click="openDialog()">新增咨询师</el-button>
    </div>

    <el-table :data="counselors" stripe style="width:100%">
      <el-table-column prop="id" label="ID" width="180" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="specialty" label="擅长领域" />
      <el-table-column prop="introduction" label="个人简介" show-overflow-tooltip />
      <el-table-column prop="rating" label="评分" width="100" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="fetchCounselors"
      @current-change="fetchCounselors"
      style="margin-top: 20px; justify-content: flex-end"
    />

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑咨询师' : '新增咨询师'"
      width="500px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="选择用户" prop="user_id">
          <div style="display: flex; align-items: center; gap: 10px;">
            <el-input 
              v-model="selectedUserNickname" 
              placeholder="点击选择用户"
              readonly
              style="width: 200px"
            />
            <el-button type="primary" @click="openUserSelectDialog">选择用户</el-button>
          </div>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" style="width: 300px" />
        </el-form-item>
        <el-form-item label="擅长领域" prop="specialty">
          <el-input v-model="form.specialty" style="width: 300px" />
        </el-form-item>
        <el-form-item label="个人简介" prop="introduction">
          <el-input
            v-model="form.introduction"
            type="textarea"
            :rows="4"
            style="width: 300px"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 用户选择对话框 -->
    <el-dialog
      v-model="userSelectDialogVisible"
      title="选择用户"
      width="600px"
    >
      <el-input
        v-model="userSearchKeyword"
        placeholder="输入用户昵称搜索"
        style="width: 100%; margin-bottom: 20px"
        clearable
        @clear="fetchUsers"
        @keyup.enter="fetchUsers"
      >
        <template #append>
          <el-button :icon="Search" @click="fetchUsers" />
        </template>
      </el-input>
      
      <el-table
        :data="users"
        stripe
        style="width: 100%"
        max-height="400px"
        highlight-current-row
        @current-change="handleUserSelect"
      >
        <el-table-column prop="id" label="用户ID" width="100" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="email" label="邮箱" />
      </el-table>

      <el-pagination
        v-model:current-page="userPage"
        v-model:page-size="userPageSize"
        :total="userTotal"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="fetchUsers"
        @current-change="fetchUsers"
        style="margin-top: 20px; justify-content: flex-end"
      />

      <template #footer>
        <el-button @click="userSelectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmUserSelect">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import {
  getCounselorList,
  createCounselor,
  updateCounselor,
  deleteCounselor
} from '@/api/counselor'
import { getUserList } from '@/api/adminUser'

const counselors = ref([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const keyword = ref('')

const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = ref({
  user_id: '',
  name: '',
  specialty: '',
  introduction: ''
})
const currentId = ref('')

// 用户选择相关
const userSelectDialogVisible = ref(false)
const users = ref([])
const userPage = ref(1)
const userPageSize = ref(10)
const userTotal = ref(0)
const userSearchKeyword = ref('')
const selectedUser = ref(null)
const selectedUserNickname = ref('')

const rules = {
  user_id: [{ required: true, message: '请选择对应用户', trigger: 'change' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  specialty: [{ required: true, message: '请输入擅长领域', trigger: 'blur' }],
  introduction: [{ required: true, message: '请输入个人简介', trigger: 'blur' }]
}

const fetchCounselors = async () => {
  try {
    const res = await getCounselorList(page.value, pageSize.value, keyword.value)
    if (res.code === 200) {
      counselors.value = res.data.records || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    ElMessage.error('获取咨询师列表失败')
  }
}

const fetchUsers = async () => {
  try {
    const res = await getUserList(userPage.value, userPageSize.value, userSearchKeyword.value)
    if (res.code === 200) {
      users.value = res.data.records || []
      userTotal.value = res.data.total || 0
    } else {
      ElMessage.error(res.msg || '获取用户列表失败')
    }
  } catch (error) {
    console.error('获取用户列表错误:', error)
    ElMessage.error('获取用户列表失败: ' + (error.message || '未知错误'))
  }
}

const openUserSelectDialog = () => {
  userSearchKeyword.value = ''
  userPage.value = 1
  selectedUser.value = null
  fetchUsers()
  userSelectDialogVisible.value = true
}

const handleUserSelect = (row) => {
  selectedUser.value = row
}

const confirmUserSelect = () => {
  if (!selectedUser.value) {
    ElMessage.warning('请选择一个用户')
    return
  }
  form.value.user_id = selectedUser.value.id
  selectedUserNickname.value = selectedUser.value.nickname
  userSelectDialogVisible.value = false
}

const openDialog = (row = null) => {
  if (row) {
    isEdit.value = true
    currentId.value = row.id
    form.value = {
      user_id: row.userId,
      name: row.name,
      specialty: row.specialty,
      introduction: row.introduction
    }
    selectedUserNickname.value = row.userId // 编辑时显示用户ID，因为可能没有昵称信息
  } else {
    isEdit.value = false
    currentId.value = ''
    form.value = {
      user_id: '',
      name: '',
      specialty: '',
      introduction: ''
    }
    selectedUserNickname.value = ''
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!formRef.value) {
    ElMessage.error('表单加载失败')
    return
  }
  
  try {
    await formRef.value.validate(async (valid) => {
      if (!valid) return

      try {
        if (isEdit.value) {
          await updateCounselor(currentId.value, form.value)
          ElMessage.success('更新成功')
        } else {
          await createCounselor(form.value)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        fetchCounselors()
      } catch (error) {
        console.error('保存错误:', error)
        ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
      }
    })
  } catch (error) {
    console.error('验证错误:', error)
    ElMessage.error('表单验证失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个咨询师吗？', '提示', {
      type: 'warning'
    })
    await deleteCounselor(id)
    ElMessage.success('删除成功')
    fetchCounselors()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  fetchCounselors()
})
</script>

<style scoped>
.counselors-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
