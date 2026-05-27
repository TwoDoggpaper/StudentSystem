<template>
  <div class="register-container">
    <div class="register-card">
      <h2>用户注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>用户名</label>
          <input 
            type="text" 
            v-model="form.username" 
            placeholder="请输入用户名" 
            class="form-control" 
            autocomplete="username"
          />
          <div class="error-msg" v-if="errors.username">{{ errors.username }}</div>
        </div>

        <div class="form-group">
          <label>密码</label>
          <input 
            type="password" 
            v-model="form.password" 
            placeholder="请输入密码" 
            class="form-control" 
            autocomplete="new-password"
          />
          <div class="error-msg" v-if="errors.password">{{ errors.password }}</div>
        </div>

        <div class="form-group">
          <label>确认密码</label>
          <input 
            type="password" 
            v-model="form.confirmPassword" 
            placeholder="请再次输入密码" 
            class="form-control" 
            autocomplete="new-password"
          />
          <div class="error-msg" v-if="errors.confirmPassword">{{ errors.confirmPassword }}</div>
        </div>

        <div class="form-group">
          <label>邮箱（选填）</label>
          <input 
            type="email" 
            v-model="form.email" 
            placeholder="请输入邮箱" 
            class="form-control" 
            autocomplete="email"
          />
          <div class="error-msg" v-if="errors.email">{{ errors.email }}</div>
        </div>

        <button type="submit" class="btn-register" :disabled="loading">
          {{ loading ? '注册中...' : '立即注册' }}
        </button>

        <button type="button" class="btn-test" @click="randomFillAndRegister" :disabled="loading">
          测试按钮(随机生成自动登录)
        </button>

        <div class="login-link">
          已有账号？<router-link to="/login">去登录</router-link>
        </div>
      </form>

      <div v-if="message" class="message" :class="messageType">{{ message }}</div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
// 修复重复导入问题，只保留一次
import { registerApi, loginApi } from '@/request/userApi'

const router = useRouter()

// 表单数据
const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: ''
})

// 错误提示
const errors = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: ''
})

// 状态管理
const loading = ref(false)
const message = ref('')
const messageType = ref('') // 'success' 或 'error'

// ---------- 表单校验 ----------
const validateForm = () => {
  let valid = true
  // 清空所有错误提示
  Object.keys(errors).forEach(key => errors[key] = '')

  // 用户名校验
  if (!form.username.trim()) {
    errors.username = '用户名不能为空'
    valid = false
  } else if (form.username.length < 3 || form.username.length > 20) {
    errors.username = '用户名长度需在3-20个字符之间'
    valid = false
  } else if (!/^[a-zA-Z0-9_]+$/.test(form.username)) {
    errors.username = '用户名仅支持字母、数字和下划线'
    valid = false
  }

  // 密码校验
  if (!form.password) {
    errors.password = '密码不能为空'
    valid = false
  } else if (form.password.length < 6 || form.password.length > 20) {
    errors.password = '密码长度需在6-20个字符之间'
    valid = false
  }

  // 确认密码校验
  if (form.password !== form.confirmPassword) {
    errors.confirmPassword = '两次输入的密码不一致'
    valid = false
  }

  // 邮箱校验（选填但填了就要符合格式）
  if (form.email && !/^[^\s@]+@([^\s@]+\.)+[^\s@]+$/.test(form.email)) {
    errors.email = '邮箱格式不正确'
    valid = false
  }

  return valid
}

// ---------- 工具函数：生成随机字符串 ----------
const randomStr = (len = 6) => {
  const chars = 'abcdefghijklmnopqrstuvwxyz0123456789'
  let result = ''
  for (let i = 0; i < len; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

// ---------- 随机填充测试数据并自动注册登录 ----------
const randomFillAndRegister = async () => {
  const randomUsername = `test_${randomStr(6)}`
  const randomPassword = '123456'
  const randomEmail = `${randomUsername}@example.com`

  // 填充随机数据
  form.username = randomUsername
  form.password = randomPassword
  form.confirmPassword = randomPassword
  form.email = randomEmail

  // 清除错误提示
  Object.keys(errors).forEach(key => errors[key] = '')
  
  // 自动执行注册 + 登录
  await autoRegisterAndLogin()
}

// ---------- 注册 + 自动登录（供测试按钮使用）----------
const autoRegisterAndLogin = async () => {
  loading.value = true
  message.value = ''
  
  try {
    // 1. 执行注册（优先使用封装的api）
    const regRes = await registerApi({
      username: form.username,
      password: form.password,
      email: form.email
    })

    if (regRes.data.code !== 200) {
      message.value = regRes.data.message || '注册失败'
      messageType.value = 'error'
      return
    }
    
    message.value = '注册成功，正在自动登录...'
    messageType.value = 'success'

    // 2. 自动登录（修复接口地址错误问题）
    const loginRes = await loginApi({
      username: form.username,
      password: form.password
    })

    if (loginRes.data.code === 200) {
      // 存储用户信息
      const token = loginRes.data.data?.token
      if (token) {
        localStorage.setItem('token', token)
        localStorage.setItem('username', form.username)
      }
      
      message.value = '登录成功，即将跳转至主页'
      messageType.value = 'success'
      setTimeout(() => router.push('/home'), 1500)
    } else {
      message.value = '注册成功但自动登录失败，请手动登录'
      messageType.value = 'error'
      setTimeout(() => router.push('/login'), 1500)
    }
  } catch (error) {
    console.error('自动注册登录失败：', error)
    const errMsg = error.response?.data?.message || '网络错误，请检查后端服务'
    message.value = errMsg
    messageType.value = 'error'
  } finally {
    loading.value = false
  }
}

// ---------- 普通注册（仅注册，成功后跳转登录页）----------
const handleRegister = async () => {
  // 先校验表单
  if (!validateForm()) return

  loading.value = true
  message.value = ''
  
  try {
    // 使用封装的注册API（替换硬编码的接口地址）
    const response = await registerApi({
      username: form.username,
      password: form.password,
      email: form.email
    })

    if (response.data.code === 200) {
      message.value = '注册成功，即将跳转到登录页...'
      messageType.value = 'success'
      setTimeout(() => router.push('/login'), 2000)
    } else {
      message.value = response.data.message || '注册失败，请稍后重试'
      messageType.value = 'error'
    }
  } catch (error) {
    console.error('注册失败：', error)
    const errMsg = error.response?.data?.message || '网络错误，请检查后端服务'
    message.value = errMsg
    messageType.value = 'error'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin: 0;
  padding: 0 20px;
  box-sizing: border-box;
}

.register-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  width: 100%;
  max-width: 420px;
  box-sizing: border-box;
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #333;
  margin-top: 0;
}

.form-group {
  margin-bottom: 1.2rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #555;
}

.form-control {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.2s;
  box-sizing: border-box;
}

.form-control:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.error-msg {
  color: #e53e3e;
  font-size: 0.8rem;
  margin-top: 0.25rem;
  line-height: 1.2;
}

.btn-register {
  width: 100%;
  background: #667eea;
  color: white;
  border: none;
  padding: 12px;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
  margin-top: 0.5rem;
}

.btn-register:hover:not(:disabled) {
  background: #5a67d8;
}

.btn-register:disabled {
  background: #b2b7ff;
  cursor: not-allowed;
}

.btn-test {
  width: 100%;
  background: #48bb78;
  color: white;
  border: none;
  padding: 12px;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  margin-top: 10px;
  transition: background 0.2s;
}

.btn-test:hover:not(:disabled) {
  background: #38a169;
}

.btn-test:disabled {
  background: #9ae6b4;
  cursor: not-allowed;
}

.login-link {
  text-align: center;
  margin-top: 1.2rem;
  font-size: 0.9rem;
  color: #666;
}

.login-link a {
  color: #667eea;
  text-decoration: none;
  transition: color 0.2s;
}

.login-link a:hover {
  text-decoration: underline;
  color: #5a67d8;
}

.message {
  margin-top: 1rem;
  padding: 0.75rem;
  border-radius: 6px;
  text-align: center;
  font-size: 0.9rem;
  line-height: 1.4;
}

.success {
  background-color: #c6f6d5;
  color: #22543d;
  border: 1px solid #9ae6b4;
}

.error {
  background-color: #fed7d7;
  color: #742a2a;
  border: 1px solid #fc8181;
}
</style>