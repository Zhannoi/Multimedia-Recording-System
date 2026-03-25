<template>
  <div class="layout">

    <!-- 顶部 -->
    <div class="header">
      <div class="left">
        <span @click="changeType('movie')">影视</span>
        <span @click="changeType('book')">书籍</span>
        <span @click="changeType('game')">游戏</span>
        <span @click="changeType('anime')">动漫</span>
      </div>

      <!-- 搜索 -->
      <div class="right">
        <input v-model="keyword" placeholder="搜索名称" />
        <button @click="search">搜索</button>
      </div>
    </div>

    <div class="main">

      <!-- 左侧筛选 -->
      <Sidebar @filter="handleFilter"/>

      <!-- 内容 -->
      <div class="content">
        <slot />
      </div>

    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import Sidebar from '../components/Sidebar.vue'

const emit = defineEmits(['changeType','search','filter'])

const keyword = ref('')

const changeType = (type) => {
  emit('changeType', type)
}

const search = () => {
  emit('search', keyword.value)
}

const handleFilter = (data) => {
  emit('filter', data)
}
</script>