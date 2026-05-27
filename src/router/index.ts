import { createRouter, createWebHistory } from 'vue-router'
import Register from '../views/register.vue'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'

const routes = [
  { path: '/', redirect: '/register' },
  { path: '/register', name: 'Register', component: Register },
  { path: '/login', name: 'Login', component: Login },
  { path: '/home', name: 'Home', component: Home }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// src/router/index.ts
router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (to.path === '/home' && !token) {
    return '/login'
  }
})

export default router