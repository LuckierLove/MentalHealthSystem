<template>
  <div class="profile-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>个人中心</span>
          <div>
            <el-button v-if="!editing" type="primary" :icon="Edit" @click="startEdit">编辑</el-button>
            <template v-else>
              <el-button @click="cancelEdit">取消</el-button>
              <el-button type="primary" :loading="saving" @click="saveProfile">保存</el-button>
            </template>
          </div>
        </div>
      </template>

      <div class="profile-main">
        <div class="avatar-box">
          <el-avatar :size="96" :src="detail.avatar" icon="User" />
          <div class="display-name">{{ displayName }}</div>
          <div class="role-tags">
            <el-tag v-for="role in roleList" :key="role" type="info" class="tag">{{ role }}</el-tag>
          </div>
        </div>

        <el-form v-if="editing" :model="editForm" ref="formRef" :rules="rules" label-width="100px" class="edit-form">
          <el-form-item label="用户ID">
            <el-input :value="detail.id" disabled />
          </el-form-item>
          <el-form-item label="用户名">
            <el-input :value="detail.username" disabled />
          </el-form-item>
          <el-form-item label="昵称" prop="nickname">
            <el-input v-model="editForm.nickname" />
          </el-form-item>
          <el-form-item label="真实姓名" prop="real_name">
            <el-input v-model="editForm.real_name" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="editForm.email" />
          </el-form-item>
        </el-form>

        <el-descriptions v-else :column="1" border class="desc">
          <el-descriptions-item label="用户ID">{{ detail.id || '—' }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ detail.username || '—' }}</el-descriptions-item>
          <el-descriptions-item label="昵称">{{ detail.nickname || '—' }}</el-descriptions-item>
          <el-descriptions-item label="真实姓名">{{ detail.real_name || detail.realName || '—' }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ detail.email || '—' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getUserInfo } from '@/api/user'
import { updateUser } from '@/api/adminUser'

const userStore = useUserStore()
const detail = ref({})
const loading = ref(false)
const editing = ref(false)
const saving = ref(false)
const formRef = ref(null)

const editForm = reactive({
  nickname: '',
  real_name: '',
  email: ''
})

const rules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  real_name: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const displayName = computed(() => detail.value.nickname || userStore.nickname || '用户')
const roleList = computed(() => detail.value.role || userStore.roles || [])

async function loadProfile() {
  try {
    loading.value = true
    if (!userStore.userId) {
      await userStore.fetchUserInfo()
    }
    const id = userStore.userId
    if (!id) {
      throw new Error('未获取到用户ID')
    }
    const res = await getUserInfo(id)
    if (res?.data) {
      detail.value = res.data
    }
  } catch (err) {
    console.error('加载个人信息失败', err)
    ElMessage.error('获取个人信息失败')
  } finally {
    loading.value = false
  }
}

function startEdit() {
  editForm.nickname = detail.value.nickname || ''
  editForm.real_name = detail.value.real_name || detail.value.realName || ''
  editForm.email = detail.value.email || ''
  editing.value = true
}

function cancelEdit() {
  editing.value = false
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

async function saveProfile() {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    saving.value = true
    
    await updateUser(detail.value.id, {
      nickname: editForm.nickname,
      real_name: editForm.real_name,
      email: editForm.email
    })
    
    ElMessage.success('保存成功')
    editing.value = false
    await loadProfile()
    await userStore.fetchUserInfo()
  } catch (err) {
    if (err !== false) { // 不是验证失败
      console.error('保存失败', err)
      ElMessage.error('保存失败')
    }
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<style scoped>
.profile-container {
  max-width: 900px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.profile-main {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}

.avatar-box {
  width: 200px;
  text-align: center;
}

.display-name {
  margin-top: 12px;
  font-size: 18px;
  font-weight: 600;
}

.role-tags {
  margin-top: 8px;
}

.tag + .tag {
  margin-left: 6px;
}

.desc {
  flex: 1;
}

.edit-form {
  flex: 1;
}
</style>
