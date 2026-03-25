<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2 class="title">注册</h2>

      <input v-model="username" placeholder="账号" class="input" />
      <input v-model="password" type="password" placeholder="密码" class="input" />

      <button @click="register" class="btn">注册</button>

      <p class="switch-text">
        已有账号？
        <span @click="goLogin">去登录</span>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'

const username = ref('')
const password = ref('')
const router = useRouter()

const register = async () => {
  try {
    const res = await request.post('/user/register', {
      username: username.value,
      password: password.value
    })

    if (res.data.code === 200) {
      alert('注册成功')
      router.push('/login')
    } else {
      alert(res.data.msg)
    }
  } catch (e) {
    alert('请求失败')
    console.error(e)
  }
}

const goLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
/* 和 Login.vue 完全一样，可以复用 CSS */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #6b73ff, #000dff);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.auth-card {
  background: #fff;
  padding: 40px 30px;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.2);
  width: 320px;
  text-align: center;
}

.title {
  margin-bottom: 30px;
  color: #333;
  font-size: 28px;
  font-weight: bold;
}

.input {
  width: 100%;
  padding: 12px 10px;
  margin-bottom: 20px;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 16px;
  outline: none;
  transition: all 0.3s;
}

.input:focus {
  border-color: #6b73ff;
  box-shadow: 0 0 8px rgba(107, 115, 255, 0.3);
}

.btn {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 8px;
  background: #6b73ff;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.btn:hover {
  background: #4f5fff;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.2);
}

.switch-text {
  margin-top: 15px;
  font-size: 14px;
  color: #555;
}

.switch-text span {
  color: #6b73ff;
  cursor: pointer;
  font-weight: bold;
}

.switch-text span:hover {
  text-decoration: underline;
}
</style>