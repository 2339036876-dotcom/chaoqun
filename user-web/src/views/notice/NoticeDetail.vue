<template>
  <el-card v-loading="loading">
    <template v-if="detail">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px">
        <h2 style="margin: 0">{{ detail.notificationTitle }}</h2>
        <el-button text @click="$router.push('/notice')">返回列表</el-button>
      </div>
      <div style="color: #999; margin-bottom: 20px">
        发布时间：{{ detail.notificationTime || detail.createTime }} | 浏览 {{ detail.hits || 0 }}
      </div>
      <div style="line-height: 1.8; white-space: pre-wrap">{{ detail.notificationContent }}</div>
    </template>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import request from '../../utils/request'

const route = useRoute()
const loading = ref(false)
const detail = ref(null)

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/notice/detail/' + route.params.id)
    detail.value = res.data
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>
