<template>
  <div v-loading="loading" style="background:#fff;padding:24px;border-radius:8px">
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px">
      <div>
        <h2 style="margin:0 0 6px">联系商家</h2>
        <div style="color:#666" v-if="goods">商品：{{ goods.title }}</div>
      </div>
      <el-button @click="$router.back()">返回</el-button>
    </div>

    <div style="border:1px solid #eee;border-radius:8px;padding:16px;min-height:360px;max-height:420px;overflow:auto" ref="msgBox">
      <div v-for="msg in messages" :key="msg.merchantCustomerServiceId" style="margin-bottom:12px">
        <div :style="msg.senderType === 'user' ? userStyle : merchantStyle">
          <div style="font-size:12px;color:#999">{{ msg.senderType === 'user' ? '我' : '商家' }} · {{ msg.createTime }}</div>
          <div style="margin-top:6px;white-space:pre-wrap">{{ msg.messageContent }}</div>
        </div>
      </div>
      <div v-if="!messages.length" style="color:#999;text-align:center;padding:40px 0">暂无消息</div>
    </div>

    <div style="display:flex;gap:12px;margin-top:16px">
      <el-input v-model="draft" type="textarea" :rows="3" placeholder="请输入消息" />
      <el-button type="primary" style="height:80px" @click="sendMessage">发送</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const route = useRoute()
const goodsId = Number(route.params.id)
const goods = ref(null)
const messages = ref([])
const sessionId = ref('')
const draft = ref('')
const loading = ref(false)
const msgBox = ref(null)
let timer = null

const userStyle = { textAlign: 'right', background: '#f0f9ff', padding: '10px 12px', borderRadius: '8px' }
const merchantStyle = { textAlign: 'left', background: '#f5f5f5', padding: '10px 12px', borderRadius: '8px' }

const loadGoods = async () => {
  const res = await request.get('/api/goods/detail/' + goodsId)
  if (res.code === 0) goods.value = res.data
}

const loadSessions = async () => {
  const res = await request.get('/api/merchant/service/my-sessions')
  const list = res.data || []
  const current = list.find(item => Number(item.goodsId) === goodsId)
  sessionId.value = current?.sessionId || ''
}

const loadMessages = async () => {
  if (!sessionId.value) return
  const res = await request.get('/api/merchant/service/messages/' + sessionId.value)
  messages.value = res.data || []
  await nextTick()
  if (msgBox.value) msgBox.value.scrollTop = msgBox.value.scrollHeight
}

const sendMessage = async () => {
  if (!draft.value.trim()) return
  const payload = {
    goodsId,
    goodsTitle: goods.value?.title,
    messageContent: draft.value.trim()
  }
  const res = await request.post('/api/merchant/service/add', payload)
  if (res.code !== 0) {
    ElMessage.error(res.msg || '发送失败')
    return
  }
  draft.value = ''
  if (!sessionId.value && res.data?.sessionId) {
    sessionId.value = res.data.sessionId
  }
  await loadMessages()
}

const startPolling = () => {
  timer = setInterval(() => {
    loadMessages()
  }, 5000)
}

onMounted(async () => {
  loading.value = true
  await loadGoods()
  await loadSessions()
  await loadMessages()
  startPolling()
  loading.value = false
})

onBeforeUnmount(() => {
  if (timer) clearInterval(timer)
})
</script>
