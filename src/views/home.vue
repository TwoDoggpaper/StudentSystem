<template>
  <div class="home-container">
    <div class="home-card">
      <h1>欢迎回来，{{ username }}！</h1>
      <p>您已成功登录学生系统。</p>
      <div class="info-panel">
        <h3>系统信息</h3>
        <p>当前时间：{{ currentTime }}</p>
        <p>登录状态：已登录</p>
        <p>角色：普通用户</p>
      </div>
      <button @click="logout" class="btn-logout">退出登录</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref(localStorage.getItem('username') || '用户')
const currentTime = ref('')

let timer = null

// 更新时间
const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN')
}

// 退出登录
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.home-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.home-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  width: 100%;
  max-width: 500px;
  text-align: center;
}

h1 {
  color: #333;
  margin-bottom: 0.5rem;
}

.info-panel {
  background: #f7fafc;
  border-radius: 8px;
  padding: 1rem;
  margin: 1.5rem 0;
  text-align: left;
}

.info-panel h3 {
  margin-top: 0;
  margin-bottom: 0.75rem;
  color: #2d3748;
}

.info-panel p {
  margin: 0.5rem 0;
  color: #4a5568;
}

.btn-logout {
  background: #e53e3e;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-logout:hover {
  background: #c53030;
}
</style>