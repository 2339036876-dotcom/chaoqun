<template>
  <div>
    <h2>公告通知</h2>
    <el-card
      v-for="item in list"
      :key="item.noticeAnnouncementId"
      shadow="hover"
      style="margin-bottom: 12px; cursor: pointer"
      @click="$router.push('/notice/' + item.noticeAnnouncementId)"
    >
      <h3 style="margin: 0 0 8px">{{ item.notificationTitle }}</h3>
      <p style="color: #666; margin: 0">
        {{ (item.notificationContent || '').slice(0, 80) }}{{ (item.notificationContent || '').length > 80 ? '...' : '' }}
      </p>
      <div style="color: #999; font-size: 12px; margin-top: 8px">
        {{ item.notificationTime }} | 浏览 {{ item.hits || 0 }}
      </div>
    </el-card>
    <el-pagination
      style="justify-content: center"
      v-model:current-page="page"
      :total="total"
      layout="prev,pager,next"
      @change="loadData"
    />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import request from '../../utils/request'

const list = ref([])
const total = ref(0)
const page = ref(1)

const loadData = async () => {
  const res = await request.get('/api/notice/list', { params: { page: page.value, size: 10 } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

onMounted(loadData)
</script>
