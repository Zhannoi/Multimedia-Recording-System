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
  const res = await request.get('/admin/users')
  users.value = res.data.data
}

const delUser = async (id) => {
  await request.delete('/admin/user/' + id)
  getUsers()
}

getUsers()
</script>