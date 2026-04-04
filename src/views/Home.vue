<template>
  <div class="home">

    <!-- 顶部导航 -->
    <el-menu mode="horizontal" :default-active="type" @select="changeType">
      <el-menu-item index="movie">影视</el-menu-item>
      <el-menu-item index="book">书籍</el-menu-item>
      <el-menu-item index="game">游戏</el-menu-item>
      <el-menu-item index="anime">动漫</el-menu-item>

      <div class="right-box">
        <el-input v-model="keyword" placeholder="搜索名称">
          <template #append>
            <el-button @click="searchByName">搜索</el-button>
          </template>
        </el-input>
        <el-button type="danger" @click="logout">退出</el-button>
      </div>
    </el-menu>

    <div class="main">

      <!-- 侧边栏 -->
      <div class="sidebar">
        <el-card>
          <h4>状态筛选</h4>
          <el-button @click="statusFilter='done'">已完成</el-button>
          <el-button @click="statusFilter='doing'">未完成</el-button>

          <h4>评分排序</h4>
          <el-button @click="sortOrder='asc'">升序</el-button>
          <el-button @click="sortOrder='desc'">降序</el-button>

          <h4>标签搜索</h4>
          <el-input v-model="tagFilter" placeholder="标签"/>
        </el-card>
      </div>

      <!-- 内容区 -->
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

    <!-- 添加弹窗 -->
    <el-dialog v-model="dialogVisible" title="添加记录">
      <el-input v-model="newRecord.name" placeholder="名称"/>
      <el-input v-model="newRecord.tags" placeholder="标签（空格分隔）" style="margin-top:10px"/>
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
        <el-button type="primary" @click="addRecord">保存</el-button>
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
            完成时间：{{ detailRecord.doneTime }}
          </p>

          <el-upload :auto-upload="false" :on-change="handleDetailUpload">
            <el-button>更换封面</el-button>
          </el-upload>

          <el-input
            type="textarea"
            v-model="detailRecord.comment"
            placeholder="写简评"
          />

          <div style="margin-top:10px;">
            <el-button type="primary" @click="saveDetail">保存</el-button>
            <el-button type="danger" @click="deleteRecord">删除</el-button>
          </div>
        </el-col>

      </el-row>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const type = ref('movie')
const keyword = ref('')
const statusFilter = ref('')
const tagFilter = ref('')
const sortOrder = ref('')

const records = ref([])
const defaultCover = 'https://via.placeholder.com/150'

// 弹窗
const dialogVisible = ref(false)
const detailVisible = ref(false)

// 数据
const newRecord = ref({})
const detailRecord = ref({})

// 模拟数据
records.value = [
  {id:1,name:'电影A',type:'movie',status:'done',statusText:'已完成',score:9,tags:'动作',cover:'',doneTime:'2026-04-01',comment:''},
]

// 计算
const filteredRecords = computed(()=>{
  return records.value
    .filter(r=>r.type===type.value)
    .filter(r=>!statusFilter.value||r.status===statusFilter.value)
    .filter(r=>!tagFilter.value||r.tags.includes(tagFilter.value))
})

// 方法
const changeType = (t)=> type.value=t

const openDialog = ()=>{
  dialogVisible.value=true
  newRecord.value={status:'doing'}
}

const addRecord = ()=>{
  const now = new Date().toLocaleString()
  records.value.push({
    ...newRecord.value,
    id: Date.now(),
    type: type.value,
    statusText: newRecord.value.status==='done'?'已完成':'未完成',
    doneTime: newRecord.value.status==='done'? now:'',
    comment:''
  })
  dialogVisible.value=false
}

const viewRecord = (r)=>{
  detailRecord.value={...r}
  detailVisible.value=true
}

const saveDetail = ()=>{
  const i = records.value.findIndex(r=>r.id===detailRecord.value.id)
  records.value[i] = {...detailRecord.value}
  detailVisible.value=false
}

const deleteRecord = ()=>{
  records.value = records.value.filter(r=>r.id!==detailRecord.value.id)
  detailVisible.value=false
}

// 上传
const handleUpload = (file)=>{
  const reader = new FileReader()
  reader.onload = e=> newRecord.value.cover=e.target.result
  reader.readAsDataURL(file.raw)
}

const handleDetailUpload = (file)=>{
  const reader = new FileReader()
  reader.onload = e=> detailRecord.value.cover=e.target.result
  reader.readAsDataURL(file.raw)
}

// 搜索
const searchByName = ()=>{
  const res = records.value.filter(r=>r.name.includes(keyword.value))
  alert(res.map(r=>r.name).join('\n'))
}

const logout = ()=>{
  router.push('/login')
}
</script>

<style scoped>
.home { background:#f5f5f5; min-height:100vh; }
.main { display:flex; }
.sidebar { width:220px; padding:10px; }
.content { flex:1; padding:20px; }
.cover { width:100%; height:180px; object-fit:cover; }
.detail-img { width:100%; height:250px; object-fit:cover; }
.right-box { margin-left:auto; display:flex; gap:10px; }
</style>