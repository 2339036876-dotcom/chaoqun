<template>
  <div style="background:#fff;padding:24px;border-radius:8px">
    <h2 style="margin:0 0 20px">我的订单</h2>
    <el-tabs v-model="activeTab" @tab-change="loadData">
      <el-tab-pane label="全部" name="" />
      <el-tab-pane label="待付款" name="待付款" />
      <el-tab-pane label="待发货" name="待发货" />
      <el-tab-pane label="待签收" name="待签收" />
      <el-tab-pane label="已完成" name="已完成" />
    </el-tabs>
    <el-table :data="list" v-loading="loading">
      <el-table-column prop="orderNumber" label="订单号" width="180" />
      <el-table-column prop="contactName" label="收件人" width="100" />
      <el-table-column prop="num" label="数量" width="60" />
      <el-table-column label="总价" width="100"><template #default="{row}">¥{{ row.priceCount }}</template></el-table-column>
      <el-table-column prop="state" label="状态" width="100">
        <template #default="{row}"><el-tag size="small">{{ row.state }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="170" />
      <el-table-column label="操作" width="260">
        <template #default="{row}">
          <el-button size="small" v-if="row.state==='待付款'" type="primary" @click="pay(row)">付款</el-button>
          <el-button size="small" v-if="['待付款','待发货'].includes(row.state)" @click="cancelOrder(row)">取消</el-button>
          <el-button size="small" v-if="row.state==='待签收'" type="success" @click="confirmReceive(row)">确认收货</el-button>
          <el-button size="small" v-if="['已签收','已完成'].includes(row.state)" type="warning" @click="afterSale(row)">售后</el-button>
          <el-button size="small" @click="$router.push('/order/'+row.orderId)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:center" v-model:current-page="page" :total="total" layout="prev,pager,next" @change="loadData" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const list = ref([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const activeTab = ref('')

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/order/my', { params: { page: page.value, size: 10, state: activeTab.value || undefined } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

const pay = (row) => {
  ElMessageBox.confirm('确认付款 ¥' + row.priceCount + '?').then(async () => {
    await request.put('/api/order/state', { orderId: row.orderId, state: '待发货' })
    ElMessage.success('付款成功')
    loadData()
  }).catch(() => {})
}

const cancelOrder = (row) => {
  ElMessageBox.confirm('确定取消该订单？取消后将退回商品库存。', '取消订单').then(async () => {
    const res = await request.put('/api/order/cancel', { orderId: row.orderId })
    if (res.code !== 0) {
      ElMessage.error(res.msg || '取消失败')
      return
    }
    ElMessage.success('订单已取消')
    loadData()
  }).catch(() => {})
}

const confirmReceive = (row) => {
  ElMessageBox.confirm('确认收货?').then(async () => {
    await request.put('/api/order/state', { orderId: row.orderId, state: '已完成' })
    ElMessage.success('已确认收货')
    loadData()
  }).catch(() => {})
}

const afterSale = (row) => {
  ElMessageBox.prompt('请描述售后原因', '申请售后', { inputType: 'textarea' }).then(async ({ value }) => {
    await request.post('/api/order/afterSale/add', { orderId: row.orderId, type: '退款', contentDesc: value })
    ElMessage.success('售后申请已提交')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
