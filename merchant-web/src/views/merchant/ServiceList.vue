<template>
  <el-card>
    <p style="color:#666;margin:0 0 16px;font-size:14px">用户从商品页「联系商家」发起的会话会出现在此列表，点击进入可与用户一对一沟通。</p>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="goodsTitle" label="关联商品" min-width="160" show-overflow-tooltip />
      <el-table-column prop="userId" label="用户ID" width="90" />
      <el-table-column prop="lastMessage" label="最近消息" min-width="200" show-overflow-tooltip />
      <el-table-column prop="lastSenderType" label="发送方" width="90">
        <template #default="{row}">{{ row.lastSenderType === 'user' ? '用户' : '商家' }}</template>
      </el-table-column>
      <el-table-column prop="lastTime" label="时间" width="170" />
      <el-table-column prop="unreadCount" label="未读" width="70">
        <template #default="{row}">
          <el-badge v-if="row.unreadCount > 0" :value="row.unreadCount" />
          <span v-else>—</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{row}">
          <el-button size="small" type="primary" @click="$router.push('/consultation/' + row.sessionId)">进入会话</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'

const list = ref([])
const loading = ref(false)

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/merchant/service/list')
  list.value = Array.isArray(res.data) ? res.data : []
  loading.value = false
}

onMounted(loadData)
</script>
