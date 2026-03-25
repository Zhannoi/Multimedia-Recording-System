<template>
  <div class="home-container">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="nav-left">
        <span :class="{active:type==='movie'}" @click="changeType('movie')">影视</span>
        <span :class="{active:type==='book'}" @click="changeType('book')">书籍</span>
        <span :class="{active:type==='game'}" @click="changeType('game')">游戏</span>
        <span :class="{active:type==='anime'}" @click="changeType('anime')">动漫</span>
      </div>
      <div class="nav-right">
        <input v-model="keyword" placeholder="按名字搜索全局" class="search-input"/>
        <button @click="searchByName" class="btn">搜索</button>
        <button @click="logout" class="btn logout-btn">退出</button>
      </div>
    </header>

    <div class="main">
      <!-- 左侧筛选栏 -->
      <aside class="sidebar">
        <h4>状态筛选</h4>
        <button @click="statusFilter='done'">已完成</button>
        <button @click="statusFilter='doing'">未完成</button>

        <h4>评分排序</h4>
        <button @click="sortOrder='asc'">升序</button>
        <button @click="sortOrder='desc'">降序</button>

        <h4>标签搜索</h4>
        <input v-model="tagFilter" placeholder="输入标签" class="tag-input"/>
        <button @click="searchTag" class="btn small-btn">搜索标签</button>
      </aside>

      <!-- 右侧内容区域 -->
      <section class="content">
        <button @click="openDialog" class="btn add-btn">+ 添加记录</button>

        <div class="list">
          <div class="card" v-for="record in filteredRecords" :key="record.id" @click="viewRecord(record)">
            <img :src="record.cover || defaultCover" alt="封面"/>
            <p class="name">{{ record.name }}</p>
            <p class="status">{{ record.statusText }}</p>
          </div>
        </div>
      </section>
    </div>

    <!-- 添加记录弹窗 -->
    <div v-if="dialogVisible" class="dialog-mask">
      <div class="dialog">
        <h3>添加记录</h3>
        <input v-model="newRecord.name" placeholder="记录名称" class="input"/>
        <input v-model="newRecord.tags" placeholder="标签（空格分隔多个）" class="input"/>
        <p class="tag-tip">提示：多个标签请用空格分开</p>

        <div class="row">
          <label>评分：</label>
          <input type="number" v-model.number="newRecord.score" min="0" max="10" class="input-score"/>
        </div>

        <div class="row">
          <label>完成状态：</label>
          <select v-model="newRecord.status" class="input-status">
            <option value="doing">未完成</option>
            <option value="done">已完成</option>
          </select>
        </div>

        <div class="row">
          <label>封面上传：</label>
          <input type="file" @change="onFileChange" />
        </div>

        <div class="dialog-btns">
          <button class="btn save-btn" @click="addRecord">保存</button>
          <button class="btn close-btn" @click="dialogVisible=false">取消</button>
        </div>
      </div>
    </div>

    <!-- 详细信息弹窗 -->
    <div v-if="detailVisible" class="dialog-mask">
      <div class="dialog dialog-large">
        <h3>记录详情</h3>
        <div class="dialog-body">
          <!-- 左侧封面 -->
          <img :src="detailRecord.cover || defaultCover" alt="封面" class="detail-cover"/>

          <!-- 右侧信息 -->
          <div class="detail-info">
            <div class="row">
              <label>名称：</label>
              <input v-model="detailRecord.name" class="input"/>
            </div>

            <div class="row">
              <label>标签：</label>
              <input v-model="detailRecord.tags" class="input"/>
              <p class="tag-tip">提示：多个标签请用空格分开</p>
            </div>

            <div class="row">
              <label>评分：</label>
              <input type="number" v-model.number="detailRecord.score" min="0" max="10" class="input-score"/>
            </div>

            <div class="row">
              <label>完成状态：</label>
              <select v-model="detailRecord.status" class="input-status">
                <option value="doing">未完成</option>
                <option value="done">已完成</option>
              </select>
            </div>

            <div v-if="detailRecord.status==='done'" class="row">
              <label>完成时间：</label>
              <span>{{ detailRecord.doneTime }}</span>
            </div>

            <div class="row">
              <label>封面上传：</label>
              <input type="file" @change="onDetailFileChange"/>
            </div>

            <div class="row">
              <label>简评：</label>
              <textarea v-model="detailRecord.comment" placeholder="写下你的简评..." class="input-comment"></textarea>
            </div>

            <!-- 按钮 -->
            <div class="dialog-btns">
              <button class="btn save-btn" @click="saveDetail">保存修改</button>
              <button class="btn delete-btn" @click="deleteRecord">删除记录</button>
              <button class="btn close-btn" @click="detailVisible=false">关闭</button>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const type = ref('movie')
const keyword = ref('')
const statusFilter = ref('')
const tagFilter = ref('')
const sortOrder = ref('')

const records = ref([])
const defaultCover = 'https://via.placeholder.com/120x150?text=封面'
const router = useRouter()

// 添加记录弹窗
const dialogVisible = ref(false)
const newRecord = ref({
  name:'',
  tags:'',
  score:0,
  status:'doing',
  cover:''
})

// 记录详情弹窗
const detailVisible = ref(false)
const detailRecord = ref({})

// 模拟数据
records.value = [
  {id:1,name:'示例影视1',type:'movie',status:'done',statusText:'已完成',tags:'喜剧 热血',score:8,cover:'',doneTime:'2026-03-25 10:00', comment:'很棒的电影'},
  {id:2,name:'示例书籍1',type:'book',status:'doing',statusText:'未完成',tags:'小说 经典',score:9,cover:'',doneTime:'', comment:''},
  {id:3,name:'示例游戏1',type:'game',status:'doing',statusText:'在玩',tags:'动作 冒险',score:7,cover:'',doneTime:'', comment:''},
  {id:4,name:'示例动漫1',type:'anime',status:'done',statusText:'已完成',tags:'热血 奇幻',score:9,cover:'',doneTime:'2026-03-24 18:00', comment:'剧情精彩'},
  {id:5,name:'示例影视2',type:'movie',status:'doing',statusText:'未完成',tags:'悬疑 犯罪',score:8,cover:'',doneTime:'', comment:''},
]

// 计算属性
const currentTypeRecords = computed(()=>records.value.filter(r=>r.type===type.value))
const filteredRecords = computed(()=>{
  return currentTypeRecords.value
    .filter(r=>!statusFilter.value || r.status===statusFilter.value)
    .filter(r=>!tagFilter.value || r.tags.includes(tagFilter.value))
    .sort((a,b)=>{
      if(!sortOrder.value) return 0
      return sortOrder.value==='asc'? a.score-b.score : b.score-a.score
    })
})

// 顶部类型切换
const changeType = t=>{
  type.value=t
  statusFilter.value=''
  tagFilter.value=''
  sortOrder.value=''
}

// 顶部全局搜索
const searchByName=()=>{
  const result = records.value.filter(r=>r.name.includes(keyword.value))
  alert('搜索结果：\n'+result.map(r=>r.name).join('\n'))
}

// 侧边栏标签搜索
const searchTag=()=>alert('标签搜索结果数量: '+filteredRecords.value.length)

// 打开添加记录弹窗
const openDialog=()=>{
  dialogVisible.value=true
  newRecord.value={name:'',tags:'',score:0,status:'doing',cover:''}
}

// 上传封面
const onFileChange=(event)=>{
  const file=event.target.files[0]
  if(file){
    const reader=new FileReader()
    reader.onload=e=>newRecord.value.cover=e.target.result
    reader.readAsDataURL(file)
  }
}

// 详细弹窗上传封面
const onDetailFileChange = (event) => {
  const file = event.target.files[0]
  if(file){
    const reader = new FileReader()
    reader.onload = e => {
      detailRecord.value.cover = e.target.result
    }
    reader.readAsDataURL(file)
  }
}

// 添加记录
const addRecord=()=>{
  if(!newRecord.value.name){alert('请输入记录名称');return}
  const dateNow = new Date()
  const record = {
    id: records.value.length+1,
    name:newRecord.value.name,
    tags:newRecord.value.tags,
    score:newRecord.value.score,
    status:newRecord.value.status,
    statusText:newRecord.value.status==='done'? '已完成':'未完成',
    cover:newRecord.value.cover,
    type:type.value,
    doneTime:newRecord.value.status==='done'? dateNow.toLocaleString():'',
    comment:''
  }
  records.value.push(record)
  dialogVisible.value=false
  alert('记录添加成功')
}

// 查看记录详情
const viewRecord=(record)=>{
  detailRecord.value = {...record} // 浅拷贝避免直接修改列表
  detailVisible.value=true
}

// 保存修改
const saveDetail=()=>{
  const index = records.value.findIndex(r=>r.id===detailRecord.value.id)
  if(index>-1){
    if(detailRecord.value.status==='done' && !records.value[index].doneTime){
      detailRecord.value.doneTime = new Date().toLocaleString()
    }
    detailRecord.value.statusText = detailRecord.value.status==='done'?'已完成':'未完成'
    records.value[index] = {...detailRecord.value}
    alert('记录修改成功')
    detailVisible.value=false
  }
}

// 删除记录
const deleteRecord=()=>{
  const index = records.value.findIndex(r=>r.id===detailRecord.value.id)
  if(index>-1){
    if(confirm('确定删除该记录吗？')){
      records.value.splice(index,1)
      detailVisible=false
      alert('记录已删除')
    }
  }
}

// 退出登录
const logout=()=>{
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

<style scoped>
/* 整体布局 */
.home-container { font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; height:100vh; display:flex; flex-direction:column; background:#f0f2f5;}
.header{display:flex; justify-content:space-between; align-items:center; padding:12px 24px; background:#6b73ff; color:#fff;}
.nav-left span{margin-right:20px; font-weight:500; cursor:pointer; transition:all 0.3s;}
.nav-left span.active{font-weight:bold; border-bottom:2px solid #fff;}
.nav-left span:hover{color:#ffd700;}
.nav-right{display:flex; align-items:center;}
.search-input{padding:6px 10px; border-radius:6px; border:none; margin-right:8px;}
.btn{padding:6px 12px; border:none; border-radius:6px; background:#ffd700; color:#333; font-weight:bold; cursor:pointer; margin-right:8px; transition:all 0.3s;}
.btn:hover{background:#ffc107; transform:translateY(-1px);}
.logout-btn{background:#ff4d4f; color:#fff;}
.logout-btn:hover{background:#d9363e;}
.main{display:flex; flex:1; overflow:hidden;}
.sidebar{width:200px; background:#fff; padding:20px; box-shadow:2px 0 5px rgba(0,0,0,0.05); display:flex; flex-direction:column;}
.sidebar h4{margin-top:10px;margin-bottom:8px;font-size:14px;color:#555;}
.sidebar button{margin-bottom:6px;width:100%;}
.tag-input{width:100%; padding:5px; border-radius:6px; border:1px solid #ccc; margin-bottom:6px;}
.small-btn{width:100%; padding:6px;}
.content{flex:1; padding:20px; overflow-y:auto;}
.add-btn{margin-bottom:20px; background:#6b73ff; color:#fff;}
.add-btn:hover{background:#4f5fff;}
.list{display:flex; flex-wrap:wrap; gap:15px;}
.card{width:120px; background:#fff; border-radius:10px; padding:8px; text-align:center; box-shadow:0 4px 12px rgba(0,0,0,0.1); cursor:pointer; transition:all 0.3s;}
.card:hover{transform:translateY(-3px); box-shadow:0 8px 20px rgba(0,0,0,0.15);}
.card img{width:100%; height:150px; object-fit:cover; border-radius:6px; margin-bottom:6px;}
.card .name{font-weight:600; font-size:14px; margin-bottom:4px;}
.card .status{font-size:12px; color:#888;}

/* 弹窗 */
.dialog-mask{position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.5); display:flex; justify-content:center; align-items:center; z-index:1000;}
.dialog{background:#fff; padding:20px 25px; border-radius:12px; width:360px; box-shadow:0 10px 25px rgba(0,0,0,0.2);}
.dialog h3{margin-bottom:15px; text-align:center;}
.input{width:100%; padding:8px 10px; margin-bottom:10px; border-radius:6px; border:1px solid #ccc;}
.tag-tip{font-size:12px; color:#888; margin-bottom:10px;}
.row{display:flex; align-items:center; margin-bottom:10px;}
.row label{width:70px;}
.input-score{width:60px; padding:5px; border-radius:6px; border:1px solid #ccc;}
.input-status{flex:1; padding:5px; border-radius:6px; border:1px solid #ccc;}
.input-comment{width:100%; min-height:80px; border-radius:6px; border:1px solid #ccc; padding:5px;}
.dialog-btns{display:flex; justify-content:flex-end; gap:12px; margin-top:15px;}
.dialog-btns .btn{padding:8px 16px; border-radius:6px; font-weight:bold; cursor:pointer; transition:all 0.3s;}
.dialog-btns .btn:hover{transform:translateY(-2px);}
.delete-btn{background:#ff4d4f; color:#fff;}
.delete-btn:hover{background:#d9363e;}
.save-btn{background:#6b73ff; color:#fff;}
.save-btn:hover{background:#4f5fff;}
.close-btn{background:#ccc; color:#333;}
.close-btn:hover{background:#aaa;}

/* 详细弹窗大尺寸 */
.dialog-large{width:600px; max-height:90vh; overflow-y:auto; display:flex; flex-direction:column; border-radius:12px; padding:20px; background:#fff; box-shadow:0 15px 40px rgba(0,0,0,0.3);}
.dialog-large h3{text-align:center; margin-bottom:15px; font-size:22px; color:#333;}
.dialog-body{display:flex; gap:20px;}
.detail-cover{width:180px; height:240px; object-fit:cover; border-radius:8px; box-shadow:0 4px 12px rgba(0,0,0,0.2); flex-shrink:0;}
.detail-info{flex:1; display:flex; flex-direction:column; gap:12px;}
.detail-info label{font-weight:600; color:#555; margin-bottom:4px;}
</style>