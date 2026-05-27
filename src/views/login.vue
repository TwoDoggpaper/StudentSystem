<template>
  <div class="login-container">
    <div class="login-card">
      <h2>用户登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>用户名</label>
          <input type="text" v-model="form.username" placeholder="请输入用户名" class="form-control" />
          <div class="error-msg" v-if="errors.username">{{ errors.username }}</div>
        </div>

        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="form.password" placeholder="请输入密码" class="form-control" />
          <div class="error-msg" v-if="errors.password">{{ errors.password }}</div>
        </div>

        <button type="submit" class="btn-login" :disabled="loading">
          {{ loading ? '登录中...' : '立即登录' }}
        </button>

        <div class="register-link">
          还没有账号？<router-link to="/register">立即注册</router-link>
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

const router = useRouter()

const form = reactive({
  username: '',
  password: ''
})

const errors = reactive({
  username: '',
  password: ''
})

const loading = ref(false)
const message = ref('')
const messageType = ref('') // 'success' 或 'error'

// 前端简单校验
const validateForm = () => {
  let valid = true
  errors.username = ''
  errors.password = ''

  if (!form.username.trim()) {
    errors.username = '用户名不能为空'
    valid = false
  }
  if (!form.password) {
    errors.password = '密码不能为空'
    valid = false
  }
  return valid
}

// const handleLogin = async () => {
//   if (!validateForm()) return

//   loading.value = true
//   message.value = ''
//   try {
//     const response = await axios.post('http://localhost:8080/user/getByName', {
//       username: form.username,
//       password: form.password
//     })

//     const { code, message: msg, data } = response.data
//     if (code === 200) {
//       // 存储 token（如果有）和用户名
//       if (data?.token) localStorage.setItem('token', data.token)
//       localStorage.setItem('username', form.username)
//       message.value = '登录成功，正在跳转...'
//       messageType.value = 'success'
//       setTimeout(() => {
//         router.push('/home')
//       }, 1500)
//     } else {
//       message.value = msg || '登录失败，请检查用户名或密码'
//       messageType.value = 'error'
//     }
//   } catch (error) {
//     console.error('登录请求失败:', error)
//     const errorMsg = error.response?.data?.message || '网络错误，请检查后端服务'
//     message.value = errorMsg
//     messageType.value = 'error'
//   } finally {
//     loading.value = false
//   }
// }
const handleLogin = async () => {
  if (!validateForm()) return;

  loading.value = true;
  message.value = '';
  try {
    // 100% 匹配后端接口的写法
    const response = await axios.get('http://localhost:8080/user/getByName', {
      params: {
        username: form.username,
        password: form.password
      }
    });

    // 打印后端返回的原始数据，方便调试
    console.log("后端返回：", response.data);

    const { code, message: msg, data } = response.data;
    if (code === 200) {
    const realToken = data.token || 'logged-in' // 兜底
    localStorage.setItem('token', realToken)     // 守卫要的是这个！
    localStorage.setItem('username', form.username)

    message.value = '登录成功，正在跳转...'
    messageType.value = 'success'

    setTimeout(() => {
    router.push('/home')
  }, 800)
  
    } else {
      message.value = msg || '用户名或密码错误';
      messageType.value = 'error';
    }
  } catch (error) {
    console.error('登录请求失败:', error);
    const errorMsg = error.response?.data?.message || '网络错误，请检查后端服务';
    message.value = errorMsg;
    messageType.value = 'error';
  } finally {
    loading.value = false;
  }
};

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
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  width: 100%;
  max-width: 400px;
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #333;
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
  box-sizing: border-box;
  transition: border-color 0.2s;
}

.form-control:focus {
  outline: none;
  border-color: #667eea;
}

.error-msg {
  color: #e53e3e;
  font-size: 0.8rem;
  margin-top: 0.25rem;
}

.btn-login {
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

.btn-login:hover {
  background: #5a67d8;
}

.btn-login:disabled {
  background: #b2b7ff;
  cursor: not-allowed;
}

.register-link {
  text-align: center;
  margin-top: 1.2rem;
  font-size: 0.9rem;
}

.register-link a {
  color: #667eea;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}

.message {
  margin-top: 1rem;
  padding: 0.75rem;
  border-radius: 6px;
  text-align: center;
  font-size: 0.9rem;
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