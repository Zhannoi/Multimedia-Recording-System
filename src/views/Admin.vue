<template>
  <div class="admin">

    <!-- 顶部 -->
    <div class="header">
      <h2>管理员后台</h2>
      <div class="right">
        <el-button type="danger" @click="logout">退出</el-button>
      </div>
    </div>

    <!-- 用户表 -->
    <el-card style="margin-top:20px;">
      <h3>用户列表</h3>

      <el-table :data="users" style="width:100%;margin-top:10px;">
        <el-table-column prop="id" label="ID" width="80"/>
        <el-table-column prop="username" label="用户名"/>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" type="primary" @click="viewUserRecords(scope.row)">
              查看记录
            </el-button>
            <el-button size="small" type="danger" @click="deleteUser(scope.row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 用户记录弹窗 -->
    <el-dialog v-model="recordDialog" title="用户记录" width="80%">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in userRecords" :key="item.id">
          <el-card>
            <img :src="item.cover || defaultCover" class="cover"/>
            <h4>{{ item.name }}</h4>
            <p>{{ item.type }} | {{ item.status === 'done' ? '已完成' : '未完成' }}</p>
            <el-button type="danger" size="small" @click="deleteRecord(item)">
              删除
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../api/request'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const users = ref([])
const userRecords = ref([])
const recordDialog = ref(false)
const currentUserId = ref(null)

const defaultCover = 'https://via.placeholder.com/150'

// 加载用户
const loadUsers = async () => {
  const res = await request.get('/admin/users')
  users.value = res.data.data || []
}

// 删除用户
const deleteUser = async (id) => {
  if (!confirm('确定删除该用户吗？')) return
  await request.delete(`/admin/user/${id}`)
  await loadUsers()
}

// 查看用户记录
const viewUserRecords = async (user) => {
  currentUserId.value = user.id

  const res = await request.get('/record/search', {
    params: {
      keyword: '',       // 不搜索，查全部
      userId: user.id
    }
  })

  userRecords.value = res.data.data || []
  recordDialog.value = true
}

// 删除用户记录（带确认提示）
const deleteRecord = async (item) => {
  try {
    await ElMessageBox.confirm(
      `确认删除记录 "${item.name}" 吗？`,
      '警告',
      { type: 'warning' }
    )

    // 根据类型调用接口删除
    const urlMap = {
      movie: `/movie/${item.id}`,
      book: `/book/${item.id}`,
      game: `/game/${item.id}`,
      anime: `/anime/${item.id}`
    }

    await request.delete(urlMap[item.type])

    ElMessage.success('删除成功')
    
    // 删除后刷新用户记录列表
    await viewUserRecords({ id: currentUserId.value })
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
      ElMessage.error('删除失败')
    }
  }
}

// 退出
const logout = () => {
  localStorage.removeItem('admin')
  router.push('/login')
}

onMounted(loadUsers)
</script>

<style scoped>
.admin {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.right {
  display: flex;
  gap: 10px;
}

.cover {
  width: 100%;
  height: 150px;
  object-fit: cover;
}
</style>