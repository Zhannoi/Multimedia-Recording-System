<template>
  <div class="home">

    <!-- 顶部导航 -->
    <el-menu mode="horizontal" :default-active="type" @select="changeType">
      <el-menu-item index="movie">影视</el-menu-item>
      <el-menu-item index="book">书籍</el-menu-item>
      <el-menu-item index="game">游戏</el-menu-item>
      <el-menu-item index="anime">动漫</el-menu-item>

      <div style="flex:1"></div>

      <el-menu-item index="search" @click="openSearchDialog">搜索</el-menu-item>
      <el-menu-item index="logout" @click="logout">退出</el-menu-item>
    </el-menu>

    <div class="main">

      <!-- 侧边栏 -->
      <div class="sidebar">
        <el-card>
          <h4>状态筛选</h4>
          <el-button
            :type="statusFilter==='done'?'primary':''"
            @click="statusFilter='done'"
          >已完成</el-button>
          <el-button
            :type="statusFilter==='doing'?'primary':''"
            @click="statusFilter='doing'"
          >未完成</el-button>

          <h4>评分排序</h4>
          <el-button
            :type="sortOrder==='asc'?'primary':''"
            @click="sortOrder = sortOrder==='asc' ? '' : 'asc'"
          >升序</el-button>
          <el-button
            :type="sortOrder==='desc'?'primary':''"
            @click="sortOrder = sortOrder==='desc' ? '' : 'desc'"
          >降序</el-button>

          <h4>标签搜索</h4>
          <el-input v-model="tagFilter" placeholder="标签"/>
        </el-card>
      </div>

      <!-- 内容 -->
      <div class="content">
        <el-button type="primary" @click="openDialog">+ 添加记录</el-button>

        <el-row :gutter="20" style="margin-top:20px;">
          <el-col :span="6" v-for="item in filteredRecords" :key="item.id">
            <el-card shadow="hover" @click="viewRecord(item)">
              <img :src="item.cover || defaultCover" class="cover"/>
              <h4>{{ item.name }}</h4>
              <p>{{ item.statusText }}</p>
            </el-card>
          </el-col>
        </el-row>
      </div>

    </div>

    <!-- 添加记录弹窗 -->
    <el-dialog v-model="dialogVisible" title="添加记录">
      <el-input v-model="newRecord.name" placeholder="名称"/>
      <el-input v-model="newRecord.tags" placeholder="标签（多个标签用空格隔开）"/>
      <el-input-number v-model="newRecord.score" :min="0" :max="10"/>
      <el-select v-model="newRecord.status">
        <el-option label="未完成" value="doing"/>
        <el-option label="已完成" value="done"/>
      </el-select>

      <el-upload :auto-upload="false" :on-change="handleUpload">
        <el-button>上传封面</el-button>
      </el-upload>

      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="handleAddRecord">保存</el-button>
      </template>
    </el-dialog>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" title="记录详情" width="700px">
      <el-row :gutter="20">
        <el-col :span="8">
          <img :src="detailRecord.cover || defaultCover" class="detail-img"/>
        </el-col>

        <el-col :span="16">
          <el-input v-model="detailRecord.name"/>
          <el-input v-model="detailRecord.tags"/>
          <el-input-number v-model="detailRecord.score" :min="0" :max="10"/>
          <el-select v-model="detailRecord.status">
            <el-option label="未完成" value="doing"/>
            <el-option label="已完成" value="done"/>
          </el-select>

          <p v-if="detailRecord.status==='done'">
            完成时间：{{ formatTime(detailRecord.finishTime) }}
          </p>

          <el-upload :auto-upload="false" :on-change="handleDetailUpload">
            <el-button>更换封面</el-button>
          </el-upload>

          <el-input type="textarea" v-model="detailRecord.comment" placeholder="写简评"/>

          <div style="margin-top:10px;">
            <el-button type="primary" @click="handleSaveDetail">保存</el-button>
            <el-button type="danger" @click="handleDeleteRecord">删除</el-button>
          </div>
        </el-col>
      </el-row>
    </el-dialog>

    <!-- 搜索弹窗 -->
    <el-dialog v-model="searchDialog" title="搜索记录">
      <el-input v-model="keyword" placeholder="输入名称"/>
      <template #footer>
        <el-button @click="searchDialog=false">取消</el-button>
        <el-button type="primary" @click="searchByName">搜索</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getRecords, addRecord, updateRecord, deleteRecord, searchAllRecords } from '../api/record'

const router = useRouter()
const type = ref('movie')
const records = ref([])

const dialogVisible = ref(false)
const detailVisible = ref(false)
const searchDialog = ref(false)

const newRecord = ref({})
const detailRecord = ref({})
const keyword = ref('')
const statusFilter = ref('')
const tagFilter = ref('')
const sortOrder = ref('')
const defaultCover = 'https://via.placeholder.com/150'

// 从 localStorage 获取登录用户
const storedUser = localStorage.getItem('user')
const currentUser = ref(storedUser ? JSON.parse(storedUser) : null)

if (!currentUser.value || !currentUser.value.id) {
  router.push('/login')
}

const userId = ref(currentUser.value?.id || 0)

// 时间格式化
const formatTime = t => {
  if (!t) return ''
  const d = new Date(t)
  return `${d.getFullYear()}-${d.getMonth()+1}-${d.getDate()}`
}

// 加载记录
const loadRecords = async () => {
  if (!userId.value) return
  const res = await getRecords(type.value, userId.value)
  records.value = (res.data || []).map(r => ({
    ...r,
    type: type.value,
    statusText: r.status==='done'?'已完成':'未完成'
  }))
}

onMounted(loadRecords)

// 计算属性：筛选 + 排序
const filteredRecords = computed(() => {
  let list = [...records.value]
  if (statusFilter.value) list = list.filter(r => r.status === statusFilter.value)
  if (tagFilter.value) list = list.filter(r => r.tags?.includes(tagFilter.value))
  if (sortOrder.value==='asc') list.sort((a,b)=>(a.score||0)-(b.score||0))
  if (sortOrder.value==='desc') list.sort((a,b)=>(b.score||0)-(a.score||0))
  return list
})

// 切换类型
const changeType = async (selected) => {
  // 搜索和退出按钮不触发类型切换
  if (!['movie', 'book', 'game', 'anime'].includes(selected)) return;

  type.value = selected;
  records.value = [];
  statusFilter.value = '';
  tagFilter.value = '';
  sortOrder.value = '';
  await loadRecords();
};

// 添加
const openDialog = () => {
  dialogVisible.value = true
  newRecord.value = { status: 'doing' }
}

const handleAddRecord = async () => {
  await addRecord(type.value, {
    ...newRecord.value,
    userId: userId.value,
    finishTime: newRecord.value.status==='done'?new Date():null
  })
  dialogVisible.value = false
  await loadRecords()
}

// 上传封面
const handleUpload = file => {
  const reader = new FileReader()
  reader.onload = e => newRecord.value.cover = e.target.result
  reader.readAsDataURL(file.raw)
}

// 查看详情
const viewRecord = r => {
  detailRecord.value = {...r}
  detailVisible.value = true
}

// 详情上传
const handleDetailUpload = file => {
  const reader = new FileReader()
  reader.onload = e => detailRecord.value.cover = e.target.result
  reader.readAsDataURL(file.raw)
}

// 保存更新
const handleSaveDetail = async () => {
  if(detailRecord.value.status==='done' && !detailRecord.value.finishTime) {
    detailRecord.value.finishTime = new Date()
  }
  if(detailRecord.value.status!=='done') {
    detailRecord.value.finishTime = null
  }
  await updateRecord(type.value, detailRecord.value)
  detailVisible.value = false
  await loadRecords()
}

// 删除
const handleDeleteRecord = async () => {
  await deleteRecord(type.value, detailRecord.value.id)
  detailVisible.value = false
  await loadRecords()
}

// 搜索
const openSearchDialog = () => searchDialog.value = true

const searchByName = async () => {
  if (!keyword.value) {
    alert("请输入搜索关键词");
    return;
  }

  try {
    const res = await searchAllRecords(keyword.value, userId.value);
    records.value = (res.data.data || []).map(r => ({
      ...r,
      statusText: r.status === 'done' ? '已完成' : '未完成'
    }));
    searchDialog.value = false;
    type.value = ''; // 搜索结果显示所有类型
  } catch (err) {
    console.error(err);
    alert("搜索失败，请检查后端接口");
  }
}

// 退出
const logout = () => {
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<style scoped>
.home {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 背景图 */
.home::before {
  content: "";
  position: fixed;
  inset: 0;

  background:
    linear-gradient(
      rgba(0,0,0,0.25),
      rgba(0,0,0,0.25)
    ),
    url('@/store/bg.png');

  background-size: cover;
  background-position: center;

  /* 模糊强度 */
  filter: blur(3px);

  /* 防止模糊后边缘露白 */
  transform: scale(1.05);

  z-index: -2;
}

/* 渐变氛围层 */
.home {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 背景图 */
.home::before {
  content: "";
  position: fixed;
  inset: 0;

  background:
    linear-gradient(
      rgba(0,0,0,0.25),
      rgba(0,0,0,0.25)
    ),
    url('@/store/bg.png');

  background-size: cover;
  background-position: center;

  filter: blur(3px);

  transform: scale(1.05);

  z-index: -2;
}

/* 氛围渐变 */
.home::after {
  content: "";
  position: fixed;
  inset: 0;

  background:
    linear-gradient(
      135deg,
      rgba(102, 126, 234, 0.18),
      rgba(118, 75, 162, 0.18)
    );

  z-index: -1;
}

/* 顶部栏（改为基本不透明） */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  margin: 20px;
  padding: 18px 28px;

  background: rgba(255,255,255,0.96);

  border-radius: 22px;

  box-shadow:
    0 6px 24px rgba(0,0,0,0.12);

  border: 1px solid rgba(255,255,255,0.5);
}

.header h2 {
  color: #333;
  font-weight: 700;
}

/* 主体 */
.main {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px 20px;
  gap: 24px;
}

/* 左侧栏 */
.sidebar {
  width: 260px;
  padding: 0;

  background: rgba(255, 255, 255, 0.16);

  backdrop-filter: blur(18px);
  -webkit-backdrop-filter: blur(18px);

  border: 1px solid rgba(255,255,255,0.18);

  border-radius: 24px;

  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.22);

  height: fit-content;

  position: sticky;
  top: 20px;

  overflow: hidden;
}

/* 内容区 */
.content {
  flex: 1;
  padding: 28px;

  background: rgba(255, 255, 255, 0.14);

  backdrop-filter: blur(18px);
  -webkit-backdrop-filter: blur(18px);

  border: 1px solid rgba(255,255,255,0.16);

  border-radius: 24px;

  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.22);
}

/* 封面图 */
.cover {
  width: 100%;
  height: 220px;

  object-fit: cover;

  border-radius: 18px 18px 0 0;

  transition:
    transform 0.35s ease,
    filter 0.35s ease;
}

.cover:hover {
  transform: scale(1.03);
  filter: brightness(1.06);
}

/* 详情图 */
.detail-img {
  width: 100%;
  height: 300px;

  object-fit: cover;

  border-radius: 14px;

  box-shadow:
    0 4px 14px rgba(0, 0, 0, 0.15);
}

/* 菜单 */
:deep(.el-menu) {
  background: transparent !important;
  border: none !important;
  padding: 16px;
}

:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;

  border-radius: 12px;

  color: rgba(255,255,255,0.88);

  margin-bottom: 8px;

  transition: all 0.3s ease;
}

:deep(.el-menu-item:hover) {
  background: rgba(255,255,255,0.12) !important;
  color: #fff;
}

:deep(.el-menu-item.is-active) {
  background: rgba(255,255,255,0.2) !important;
  color: #fff;
  font-weight: 600;
}

/* 卡片 */
:deep(.el-card) {
  overflow: hidden;

  background: rgba(255,255,255,0.14) !important;

  border: 1px solid rgba(255,255,255,0.14) !important;

  backdrop-filter: blur(18px);

  border-radius: 20px;

  transition: all 0.35s ease;

  box-shadow:
    0 8px 24px rgba(0,0,0,0.16);
}

:deep(.el-card:hover) {
  transform: translateY(-6px);

  box-shadow:
    0 14px 30px rgba(0,0,0,0.22);
}

/* 按钮 */
:deep(.el-button) {
  border-radius: 12px;

  font-weight: 600;

  border: none;

  transition: all 0.3s ease;
}

:deep(.el-button:hover) {
  transform: translateY(-2px);

  box-shadow:
    0 6px 18px rgba(0,0,0,0.18);
}

/* 详情弹窗（恢复正常风格，但保留轻微透明） */
:deep(.el-dialog) {
  border-radius: 18px !important;

  overflow: hidden;

  background: rgba(255,255,255,0.96) !important;

  backdrop-filter: blur(8px);

  color: #333;

  box-shadow:
    0 10px 36px rgba(0,0,0,0.2);
}

/* 弹窗标题 */
:deep(.el-dialog__title) {
  color: #333;
  font-weight: 700;
}

/* 输入框 */
:deep(.el-input__wrapper) {
  border-radius: 12px;

  background: rgba(255,255,255,0.85);

  box-shadow: none !important;
}

/* 输入文字 */
:deep(.el-input__inner) {
  color: #333;
}

/* 标题 */
h4 {
  color: white;
  font-weight: 700;
  margin: 14px 0;
}

/* 内容文字 */
p {
  color: rgb(8, 8, 8);
  font-size: 14px;
  line-height: 1.7;
}
</style>