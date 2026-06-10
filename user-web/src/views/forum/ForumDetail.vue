<template>
  <div v-if="forum" style="background:#fff;padding:24px;border-radius:8px">
    <h2 style="margin:0 0 8px">{{ forum.title }}</h2>
    <div style="color:#999;margin-bottom:20px">
      <span style="margin-right:16px">{{ forum.nickname }}</span>
      <span style="margin-right:16px">浏览 {{ forum.hits }}</span>
      <span>{{ forum.createTime }}</span>
    </div>
    <div v-html="forum.content" style="line-height:1.8;margin-bottom:20px"></div>
    <el-divider />
    <h3>评论</h3>
    <div v-for="c in comments" :key="c.commentId" style="border-bottom:1px solid #f0f0f0;padding:12px 0">
      <div>{{ c.content }}</div>
      <div v-if="c.reply" style="color:#409eff;margin-top:4px">回复: {{ c.reply }}</div>
      <div style="color:#999;font-size:12px;margin-top:4px">{{ c.createTime }}</div>
    </div>
    <div v-if="isLogin" style="margin-top:16px;display:flex;gap:10px">
      <el-input v-model="commentText" placeholder="写评论..." />
      <el-button type="primary" @click="submitComment">发表</el-button>
    </div>
    <el-button style="margin-top:16px" @click="$router.back()">返回</el-button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const route = useRoute()
const forum = ref(null)
const comments = ref([])
const commentText = ref('')
const isLogin = computed(() => !!localStorage.getItem('user-token'))

onMounted(async () => {
  const res = await request.get('/api/forum/detail/' + route.params.id)
  if (res.code === 0) forum.value = res.data
  const cRes = await request.get('/api/comment/list/forum/' + route.params.id)
  comments.value = cRes.data?.list || []
})

const submitComment = async () => {
  if (!commentText.value) return
  await request.post('/api/comment/add', { content: commentText.value, sourceTable: 'forum', sourceId: Number(route.params.id) })
  commentText.value = ''
  ElMessage.success('评论成功')
  const cRes = await request.get('/api/comment/list/forum/' + route.params.id)
  comments.value = cRes.data?.list || []
}
</script>
