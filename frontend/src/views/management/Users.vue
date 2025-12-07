<template>
  <div>
    <el-card>
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px">
        <el-input v-model="query.keyword" placeholder="按用户名/昵称搜索" style="width:300px" clearable @clear="fetchUsers" @keyup.enter.native="fetchUsers" />
        <div>
          <el-button type="primary" @click="openCreate">新建用户</el-button>
        </div>
      </div>

      <el-table :data="users" stripe style="width:100%">
        <el-table-column prop="id" label="ID" width="180" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="role" label="角色" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="editUser(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="removeUser(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination v-if="total>0" style="margin-top:12px;" :current-page.sync="query.page" :page-size="query.pageSize" :total="total" @current-change="fetchUsers" />
    </el-card>

    <el-dialog title="用户" v-model="dialogVisible" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" style="width: 300px" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input v-model="form.password" type="password" style="width: 300px" show-password />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" style="width: 300px" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="real_name">
          <el-input v-model="form.real_name" style="width: 300px" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" style="width: 300px" />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :http-request="handleAvatarUpload"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="角色" prop="role_id">
          <el-select v-model="form.role_id" placeholder="请选择角色" style="width: 300px">
            <el-option v-for="role in roleList" :key="role.id" :label="role.name" :value="role.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getUserList, createUser, updateUser, deleteUser } from '@/api/adminUser'
import { getRoleList } from '@/api/role'
import { uploadAvatar } from '@/api/file'
import request from '@/utils/request'

const users = ref([])
const total = ref(0)
const query = reactive({ page: 1, pageSize: 10, keyword: '' })

const dialogVisible = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, username: '', password: '', nickname: '', real_name: '', email: '', role_id: '', avatar: '' })
const uploading = ref(false)
const roleList = ref([])
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  real_name: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
  avatar: [{ required: true, message: '请上传头像', trigger: 'change' }],
  role_id: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

function fetchUsers() {
  getUserList(query.page, query.pageSize, query.keyword).then(res => {
    if (res.data) {
      users.value = res.data.records || res.data.list || []
      total.value = res.data.total || res.data.count || users.value.length
    }
  }).catch(()=>{})
}

function openCreate() {
  form.id = null
  form.username = ''
  form.password = ''
  form.nickname = ''
  form.real_name = ''
  form.email = ''
  form.role_id = ''
  form.avatar = ''
  fetchRoles(false) // 新增用户不展示咨询师角色
  dialogVisible.value = true
}

function editUser(row) {
  form.id = row.id
  form.username = row.username
  form.nickname = row.nickname
  form.real_name = row.real_name || ''
  form.email = row.email
  form.role_id = row.role_id || ''
  form.avatar = row.avatar || ''
  fetchRoles(true) // 编辑时保留全部角色（含咨询师）
  dialogVisible.value = true
}

async function saveUser() {
  try {
    await formRef.value.validate()
    if (form.id) {
      await updateUser(form.id, form)
    } else {
      await createUser(form)
    }
    dialogVisible.value = false
    fetchUsers()
    ElMessage.success('保存成功')
  } catch (e) {
    // 验证或保存失败
  }
}

async function removeUser(row) {
  try {
    await ElMessageBox.confirm(`确认删除用户 ${row.username} ?`, '删除确认', { type: 'warning' })
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    fetchUsers()
  } catch (e) {
    // 取消或出错
  }
}

function beforeAvatarUpload(file) {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('头像只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}

async function handleAvatarUpload(options) {
  try {
    uploading.value = true
    const res = await uploadAvatar(options.file)
    if (res.data && res.data.url) {
      let url = res.data.url
      // 如果返回的是相对路径，则拼接 axios 的 baseURL
      if (url && !/^https?:\/\//i.test(url)) {
        const base = request.defaults && request.defaults.baseURL ? request.defaults.baseURL : ''
        url = `${base.replace(/\/+$/,'')}/${url.replace(/^\/+/, '')}`
      }
      form.avatar = url
      ElMessage.success('头像上传成功')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error('头像上传失败')
  } finally {
    uploading.value = false
  }
}

function fetchRoles(includeCounselor = true) {
  getRoleList({ page: 1, pageSize: 100 }).then(res => {
    const list = res.data?.records || res.data?.list || []
    roleList.value = includeCounselor ? list : list.filter(r => r.name !== '咨询师')
  }).catch(() => {})
}

onMounted(fetchUsers)
</script>

<style scoped>
.avatar-uploader {
  display: inline-block;
}

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
  object-fit: cover;
}
</style>
