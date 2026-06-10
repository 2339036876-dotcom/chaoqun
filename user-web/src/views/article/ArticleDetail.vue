<template>
  <div v-if="article" style="background:#fff;padding:24px;border-radius:8px">
    <h2 style="margin:0 0 8px">{{ article.title }}</h2>
    <div style="color:#999;margin-bottom:20px">来源: {{ article.source || '本站' }} | 浏览 {{ article.hits }} | {{ article.createTime }}</div>
    <div v-html="article.content" style="line-height:1.8"></div>
    <el-button style="margin-top:20px" @click="$router.back()">返回</el-button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import request from '../../utils/request'

const route = useRoute()
const article = ref(null)

onMounted(async () => {
  const res = await request.get('/api/article/detail/' + route.params.id)
  if (res.code === 0) article.value = res.data
})
</script>
