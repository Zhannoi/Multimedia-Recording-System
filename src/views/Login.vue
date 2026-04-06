<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2 class="title">登录</h2>

      <input v-model="username" placeholder="账号" class="input" />
      <input v-model="password" type="password" placeholder="密码" class="input" />

      <button @click="login" class="btn">登录</button>

      <p class="switch-text">
        没有账号？
        <span @click="goRegister">去注册</span>
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

const login = async () => {
  try {
    const res = await request.post('/user/login', {
      username: username.value,
      password: password.value
    })

    if (res.data.code === 200) {
      // 后端返回的是User对象，可以存储id或username作为token
      localStorage.setItem('token', res.data.data.id || res.data.data.username)
      alert('登录成功')
      router.push('/home')
    } else {
      alert(res.data.msg)
    }
  } catch (e) {
    alert('请求失败')
    console.error(e)
  }
}

const goRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
/* 整体居中 */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #6b73ff, #000dff);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 卡片 */
.auth-card {
  background: #fff;
  padding: 40px 30px;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.2);
  width: 320px;
  text-align: center;
}

/* 标题 */
.title {
  margin-bottom: 30px;
  color: #333;
  font-size: 28px;
  font-weight: bold;
}

/* 输入框 */
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

/* 按钮 */
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

/* 切换文字 */
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