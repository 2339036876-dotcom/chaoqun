<template>
  <el-card v-loading="loading">
    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px">
      <div>
        <h2 style="margin:0 0 6px;font-size:18px">会话</h2>
        <div style="color:#666;font-size:14px" v-if="meta.goodsTitle">商品：{{ meta.goodsTitle }} · 用户ID：{{ meta.userId }}</div>
      </div>
      <el-button @click="$router.push('/consultation')">返回列表</el-button>
    </div>
    <div style="border:1px solid #eee;border-radius:8px;padding:16px;min-height:360px;max-height:480px;overflow:auto" ref="msgBox">
      <div v-for="msg in messages" :key="msg.merchantCustomerServiceId" style="margin-bottom:12px">
        <div :style="msg.senderType === 'user' ? userStyle : merchantStyle">
          <div style="font-size:12px;color:#999">{{ msg.senderType === 'user' ? '用户' : '我（商家）' }} · {{ msg.createTime }}</div>
          <div style="margin-top:6px;white-space:pre-wrap">{{ msg.messageContent }}</div>
        </div>
      </div>
      <div v-if="!messages.length" style="color:#999;text-align:center;padding:40px 0">暂无消息</div>
    </div>
    <div style="display:flex;gap:12px;margin-top:16px">
      <el-input v-model="draft" type="textarea" :rows="3" placeholder="回复用户" />
      <el-button type="primary" style="height:80px" @click="sendReply">发送</el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, nextTick, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const route = useRoute()
const sessionId = ref(decodeURIComponent(route.params.sessionId || ''))
const messages = ref([])
const meta = reactive({ goodsTitle: '', userId: null })
const draft = ref('')
const loading = ref(false)
const msgBox = ref(null)
let timer = null

const userStyle = { textAlign: 'left', background: '#f0f9ff', padding: '10px 12px', borderRadius: '8px' }
const merchantStyle = { textAlign: 'right', background: '#f5f5f5', padding: '10px 12px', borderRadius: '8px' }

const loadMessages = async () => {
  if (!sessionId.value) return
  const res = await request.get('/api/merchant/service/detail/' + encodeURIComponent(sessionId.value))
  if (res.code !== 0) {
    ElMessage.error(res.msg || '加载失败')
    return
  }
  const list = res.data || []
  messages.value = list
  if (list.length) {
    const first = list[0]
    meta.goodsTitle = first.goodsTitle
    meta.userId = first.userId
  }
  await nextTick()
  if (msgBox.value) msgBox.value.scrollTop = msgBox.value.scrollHeight
}

const sendReply = async () => {
  if (!draft.value.trim() || !sessionId.value) return
  const res = await request.put('/api/merchant/service/reply', {
    sessionId: sessionId.value,
    messageContent: draft.value.trim()
  })
  if (res.code !== 0) {
    ElMessage.error(res.msg || '发送失败')
    return
  }
  draft.value = ''
  await loadMessages()
}

watch(
  () => route.params.sessionId,
  (v) => {
    sessionId.value = decodeURIComponent(v || '')
    loadMessages()
  }
)

const tick = () => {
  loadMessages()
}

onMounted(async () => {
  loading.value = true
  await loadMessages()
  loading.value = false
  timer = setInterval(tick, 5000)
})

onBeforeUnmount(() => {
  if (timer) clearInterval(timer)
})
</script>
