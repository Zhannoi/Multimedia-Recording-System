<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2 class="title">登录</h2>

      <input v-model="username" placeholder="账号" class="input" />
      <input v-model="password" type="password" placeholder="密码" class="input" />

      <button @click="login" class="btn">登录</button>

      <button @click="adminLogin" class="btn admin-btn">管理员登录</button>

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
      // 存储完整用户对象
      const user = res.data.data
      localStorage.setItem('user', JSON.stringify(user))

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

// 管理员登录
const adminLogin = async () => {
  try {
    const res = await request.post('/admin/login', {
      username: username.value,
      password: password.value
    })

    if (res.data.code === 200) {
      const admin = res.data.data
      localStorage.setItem('admin', JSON.stringify(admin))

      alert('管理员登录成功')
      router.push('/admin')
    } else {
      alert(res.data.msg)
    }
  } catch (e) {
    alert('管理员登录失败')
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
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  overflow: hidden;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 背景图层 */
.auth-container::before {
  content: "";
  position: absolute;
  inset: 0;

  /* 背景图片 */
  background: url('@/store/bg.png') center center / cover no-repeat;

  /* 毛玻璃核心 */
  filter: blur(3px);

  /* 防止模糊后露白边 */
  transform: scale(1.1);

  z-index: -2;
}

/* 暗色遮罩层（提升文字可读性） */
.auth-container::after {
  content: "";
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.25);
  z-index: -1;
}

/* 卡片 */
.auth-card {
  background: rgba(255, 255, 255, 0.95);
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.2);
  width: 360px;
  text-align: center;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

/* 标题 */
.title {
  margin-bottom: 30px;
  color: #333;
  font-size: 32px;
  font-weight: bold;
  background: linear-gradient(135deg, #6b73ff, #000dff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* 输入框 */
.input {
  width: 90%;
  padding: 14px 16px;
  margin-bottom: 20px;
  border-radius: 10px;
  border: 1px solid #e0e0e0;
  font-size: 16px;
  outline: none;
  transition: all 0.3s;
  background: rgba(255, 255, 255, 0.8);
}

.input:focus {
  border-color: #6b73ff;
  box-shadow: 0 0 12px rgba(107, 115, 255, 0.3);
  transform: translateY(-2px);
}

/* 按钮 */
.btn {
  width: 100%;
  padding: 14px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #6b73ff, #4f5fff);
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 12px;
}

.btn:hover {
  background: linear-gradient(135deg, #5a62ff, #3f4fff);
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(107, 115, 255, 0.4);
}

/* 切换文字 */
.switch-text {
  margin-top: 20px;
  font-size: 14px;
  color: #555;
}

.switch-text span {
  color: #6b73ff;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.3s;
}

.switch-text span:hover {
  text-decoration: underline;
  color: #4f5fff;
}

.admin-btn {
  background: linear-gradient(135deg, #ff4d4f, #d9363e);
}

.admin-btn:hover {
  background: linear-gradient(135deg, #e04546, #c22f35);
  box-shadow: 0 8px 20px rgba(255, 77, 79, 0.4);
}
</style>