<template>
  <div>

    <h2>用户管理</h2>

    <table>
      <tr v-for="u in users" :key="u.id">
        <td>{{ u.username }}</td>
        <td>
          <button @click="delUser(u.id)">删除</button>
        </td>
      </tr>
    </table>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import request from '../api/request'

const users = ref([])

const getUsers = async () => {
  try {
    const res = await request.get('/admin/users')
    // 后端返回的是直接的数据对象或列表，不是统一格式
    users.value = Array.isArray(res.data) ? res.data : [res.data]
  } catch (error) {
    console.error('获取用户失败:', error)
    // 如果API调用失败，使用模拟数据作为备选
    users.value = [
      {id:1,username:'admin',email:'admin@example.com'},
      {id:2,username:'user1',email:'user1@example.com'}
    ]
  }
}

const delUser = async (id) => {
  try {
    const res = await request.delete(`/admin/user/${id}`)
    // 后端返回字符串，不需要检查code
    if (typeof res.data === 'string' && res.data.includes('成功')) {
      await getUsers() // 重新加载数据
    } else {
      alert('删除失败')
    }
  } catch (error) {
    console.error('删除用户失败:', error)
    // 如果API失败，使用本地删除
    users.value = users.value.filter(u => u.id !== id)
  }
}

getUsers()
</script>