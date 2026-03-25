<template>
  <div class="sidebar">

    <h4>状态</h4>
    <button @click="filterStatus('done')">已完成</button>
    <button @click="filterStatus('doing')">未完成</button>

    <h4>评分排序</h4>
    <button @click="sort('asc')">升序</button>
    <button @click="sort('desc')">降序</button>

    <h4>标签搜索</h4>
    <input v-model="tag" placeholder="输入标签"/>
    <button @click="searchTag">搜索</button>

  </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['filter'])

const tag = ref('')

// 状态
const filterStatus = (status) => {
  emit('filter', { status })
}

// 排序
const sort = (order) => {
  emit('filter', { order })
}

// 标签
const searchTag = () => {
  emit('filter', { tag: tag.value })
}
</script>