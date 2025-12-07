<template>
  <el-dialog
    v-model="dialogVisible"
    title="用户注册"
    width="500px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="registerFormRef"
      :model="registerForm"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="registerForm.username"
          placeholder="请输入用户名"
          clearable
        />
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input
          v-model="registerForm.password"
          type="password"
          placeholder="请输入密码"
          show-password
          clearable
        />
      </el-form-item>

      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="registerForm.confirmPassword"
          type="password"
          placeholder="请再次输入密码"
          show-password
          clearable
        />
      </el-form-item>

      <el-form-item label="昵称" prop="nickname">
        <el-input
          v-model="registerForm.nickname"
          placeholder="请输入昵称"
          clearable
        />
      </el-form-item>

      <el-form-item label="真实姓名" prop="real_name">
        <el-input
          v-model="registerForm.real_name"
          placeholder="请输入真实姓名"
          clearable
        />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="registerForm.email"
          placeholder="请输入邮箱"
          clearable
        />
      </el-form-item>

      <el-form-item label="头像" prop="avatar">
        <el-upload
          class="avatar-uploader"
          :show-file-list="false"
          :http-request="handleAvatarUpload"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="registerForm.avatar" :src="registerForm.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon">
            <Plus />
          </el-icon>
        </el-upload>
        <div class="upload-tip">点击上传头像（支持jpg、png格式，大小不超过2MB）</div>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleRegister">
          注册
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { register } from '@/api/auth'
import { uploadAvatar } from '@/api/file'
import request from '@/utils/request'
// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'success'])

// 对话框显示状态
const dialogVisible = ref(props.modelValue)
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
})
watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

// 表单引用
const registerFormRef = ref(null)

// 注册表单数据
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  real_name: '',
  email: '',
  avatar: ''
})

// 加载状态
const loading = ref(false)

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  real_name: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

/**
 * 上传前的校验
 */
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

/**
 * 自定义上传方法
 */
async function handleAvatarUpload(options) {
  try {
    loading.value = true
    const res = await uploadAvatar(options.file)
    if (res.data && res.data.url) {
      let url = res.data.url
      // 如果返回的是相对路径，则拼接 axios 的 baseURL
      if (url && !/^https?:\/\//i.test(url)) {
        const base = request.defaults && request.defaults.baseURL ? request.defaults.baseURL : ''
        // 合并时注意去除多余斜杠
        url = `${base.replace(/\/+$/,'')}/${url.replace(/^\/+/, '')}`
      }
      registerForm.avatar = url
      ElMessage.success('头像上传成功')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error('头像上传失败')
  } finally {
    loading.value = false
  }
}

/**
 * 处理注册
 */
async function handleRegister() {
  try {
    // 验证表单
    await registerFormRef.value.validate()
    
    loading.value = true
    
    // 准备注册数据（移除确认密码字段）
    const { confirmPassword, ...registerData } = registerForm
    
    // 调用注册接口
    await register(registerData)
    
    ElMessage.success('注册成功！请登录')
    
    // 关闭对话框
    handleClose()
    
    // 通知父组件注册成功
    emit('success')
  } catch (error) {
    console.error('注册失败:', error)
    if (!error.message) {
      ElMessage.error('注册失败，请重试')
    }
  } finally {
    loading.value = false
  }
}

/**
 * 关闭对话框
 */
function handleClose() {
  // 重置表单
  registerFormRef.value?.resetFields()
  registerForm.avatar = ''
  dialogVisible.value = false
}
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

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}
</style>
