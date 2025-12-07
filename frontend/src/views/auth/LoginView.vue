<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>大学生心理健康系统</h2>
        <p>欢迎登录</p>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="rules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
            clearable
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            clearable
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>

        <div class="login-footer">
          <el-button type="text" @click="showRegisterDialog">
            没有账号？注册
          </el-button>
        </div>
      </el-form>
    </div>

    <!-- 注册对话框 -->
    <RegisterDialog
      v-model="registerDialogVisible"
      @success="handleRegisterSuccess"
    />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { login } from '@/api/auth'
import RegisterDialog from '@/components/common/RegisterDialog.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 表单引用
const loginFormRef = ref(null)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: ''
})

// 加载状态
const loading = ref(false)

// 注册对话框显示状态
const registerDialogVisible = ref(false)

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

/**
 * 处理登录
 */
async function handleLogin() {
  try {
    // 验证表单
    await loginFormRef.value.validate()
    
    loading.value = true
    
    // 调用登录接口
    const res = await login(loginForm)
    
    if (res.data && res.data.token) {
      // 从返回的 token 对象中提取 tokenValue
      const tokenValue = res.data.token.tokenValue
      
      // 存储 token
      userStore.setToken(tokenValue)
      
      // 获取用户信息
      await userStore.fetchUserInfo()
      
      ElMessage.success('登录成功')
      
      // 跳转到首页或重定向页面
      const redirect = route.query.redirect || '/'
      router.push(redirect)
    }
  } catch (error) {
    console.error('登录失败:', error)
    if (!error.message) {
      ElMessage.error('登录失败，请检查用户名和密码')
    }
  } finally {
    loading.value = false
  }
}

/**
 * 显示注册对话框
 */
function showRegisterDialog() {
  registerDialogVisible.value = true
}

/**
 * 注册成功回调
 */
function handleRegisterSuccess() {
  ElMessage.success('注册成功！请登录')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 24px;
}

.login-header p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.login-form {
  margin-top: 20px;
}

.login-button {
  width: 100%;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
}

.login-footer .el-button {
  color: #667eea;
}
</style>
