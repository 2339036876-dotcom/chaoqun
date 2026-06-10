<template>
  <div v-if="order" style="background:#fff;padding:24px;border-radius:8px">
    <h2 style="margin:0 0 20px">订单详情</h2>
    <el-descriptions :column="2" border>
      <el-descriptions-item label="订单号">{{ order.orderNumber }}</el-descriptions-item>
      <el-descriptions-item label="状态"><el-tag>{{ order.state }}</el-tag></el-descriptions-item>
      <el-descriptions-item label="收件人">{{ order.contactName }}</el-descriptions-item>
      <el-descriptions-item label="电话">{{ order.contactPhone }}</el-descriptions-item>
      <el-descriptions-item label="地址" :span="2">{{ order.contactAddress }}</el-descriptions-item>
      <el-descriptions-item label="数量">{{ order.num }}</el-descriptions-item>
      <el-descriptions-item label="总价">¥{{ order.priceCount }}</el-descriptions-item>
      <el-descriptions-item label="配送状态">{{ order.deliveryState }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ order.createTime }}</el-descriptions-item>
      <el-descriptions-item label="备注" :span="2">{{ order.remark || '无' }}</el-descriptions-item>
    </el-descriptions>
    <div style="margin-top:20px;display:flex;gap:12px">
      <el-button v-if="order && ['待付款','待发货'].includes(order.state)" @click="cancelOrder">取消订单</el-button>
      <el-button @click="$router.back()">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const route = useRoute()
const order = ref(null)

const loadOrder = async () => {
  const res = await request.get('/api/order/detail/' + route.params.id)
  if (res.code === 0) order.value = res.data
}

const cancelOrder = () => {
  if (!order.value) return
  ElMessageBox.confirm('确定取消该订单？取消后将退回商品库存。', '取消订单').then(async () => {
    const res = await request.put('/api/order/cancel', { orderId: order.value.orderId })
    if (res.code !== 0) {
      ElMessage.error(res.msg || '取消失败')
      return
    }
    ElMessage.success('订单已取消')
    await loadOrder()
  }).catch(() => {})
}

onMounted(loadOrder)
</script>
