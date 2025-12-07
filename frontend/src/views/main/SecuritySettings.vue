<template>
  <div class="security-settings">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>安全设置</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="修改头像" name="avatar">
          <el-form label-width="100px" style="max-width: 500px">
             <el-form-item label="头像">
                <el-upload
                  class="avatar-uploader"
                  action=""
                  :show-file-list="false"
                  :http-request="handleAvatarUpload"
                  :before-upload="beforeAvatarUpload"
                >
                  <img v-if="infoForm.avatar" :src="infoForm.avatar" class="avatar" />
                  <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                </el-upload>
                <div class="el-upload__tip">点击图片上传新头像，支持 JPG/PNG</div>
             </el-form-item>
             <el-form-item>
               <el-button type="primary" @click="updateAvatar" :loading="loading">保存头像</el-button>
             </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="修改密码" name="password">
          <el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="100px" style="max-width: 500px">
            <el-form-item label="新密码" prop="password">
              <el-input v-model="pwdForm.password" type="password" show-password placeholder="请输入新密码（8-20位）" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updatePassword" :loading="loading">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getUserInfo, updateUser } from '@/api/user'
import { uploadAvatar } from '@/api/file'
import request from '@/utils/request'

const userStore = useUserStore()
const activeTab = ref('avatar')
const loading = ref(false)
const pwdFormRef = ref(null)

const infoForm = reactive({
  avatar: ''
})

const pwdForm = reactive({
  password: '',
  confirmPassword: ''
})

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== pwdForm.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const pwdRules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, max: 20, message: '长度在 8 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ]
}

async function fetchUserInfo() {
  if (!userStore.userId) return
  try {
    const res = await getUserInfo(userStore.userId)
    if (res.code === 200 && res.data) {
      infoForm.avatar = res.data.avatar
    }
  } catch (err) {
    console.error('获取用户信息失败', err)
  }
}

function beforeAvatarUpload(file) {
  const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPGOrPNG) {
    ElMessage.error('上传头像图片只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
  }
  return isJPGOrPNG && isLt2M
}

async function handleAvatarUpload(options) {
  try {
    loading.value = true
    const res = await uploadAvatar(options.file)
    if (res.code === 200) {
      let url = res.data.url
      // 如果返回的是相对路径，则拼接 axios 的 baseURL
      if (url && !/^https?:\/\//i.test(url)) {
        const base = request.defaults && request.defaults.baseURL ? request.defaults.baseURL : ''
        // 合并时注意去除多余斜杠
        url = `${base.replace(/\/+$/,'')}/${url.replace(/^\/+/, '')}`
      }
      infoForm.avatar = url
      ElMessage.success('头像上传成功，请点击保存修改以应用')
    } else {
      ElMessage.error(res.msg || '头像上传失败')
    }
  } catch (err) {
    ElMessage.error('头像上传失败')
  } finally {
    loading.value = false
  }
}

async function updateAvatar() {
  loading.value = true
  try {
    await updateUser(userStore.userId, {
      avatar: infoForm.avatar
    })
    ElMessage.success('头像更新成功')
    // 更新 store 中的信息
    userStore.fetchUserInfo()
  } catch (err) {
    ElMessage.error('更新失败')
  } finally {
    loading.value = false
  }
}

async function updatePassword() {
  if (!pwdFormRef.value) return
  await pwdFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await updateUser(userStore.userId, {
          password: pwdForm.password
        })
        ElMessage.success('密码修改成功，请重新登录')
        userStore.logout()
        window.location.href = '/login'
      } catch (err) {
        ElMessage.error('密码修改失败')
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.security-settings {
  max-width: 800px;
  margin: 20px auto;
}

.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  display: block;
  border-radius: 50%;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
}
</style>
