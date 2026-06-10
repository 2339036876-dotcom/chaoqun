<template>
  <el-card>
    <div style="display:flex;gap:10px;margin-bottom:16px">
      <el-select v-model="search.state" placeholder="订单状态" clearable style="width:150px" @change="loadData">
        <el-option v-for="s in states" :key="s" :label="s" :value="s" />
      </el-select>
      <el-button type="primary" @click="loadData">搜索</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="orderId" label="ID" width="60" />
      <el-table-column prop="orderNumber" label="订单号" width="180" show-overflow-tooltip />
      <el-table-column prop="contactName" label="收件人" width="100" />
      <el-table-column prop="contactPhone" label="电话" width="120" />
      <el-table-column prop="num" label="数量" width="60" />
      <el-table-column prop="priceCount" label="总价" width="100">
        <template #default="{row}">¥{{ row.priceCount }}</template>
      </el-table-column>
      <el-table-column prop="state" label="状态" width="100">
        <template #default="{row}"><el-tag :type="stateType(row.state)" size="small">{{ row.state }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="deliveryState" label="配送" width="80" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{row}">
          <el-button size="small" v-if="row.state==='待发货'" type="primary" @click="handleShip(row)">发货</el-button>
          <el-button size="small" @click="handleState(row)">改状态</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.orderId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="page" v-model:page-size="size" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @change="loadData" @size-change="loadData" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)
const search = reactive({ state: '' })
const states = ['待付款', '待发货', '待签收', '已签收', '待退款', '已退款', '已拒绝', '已完成']

const stateType = (s) => {
  if (s === '已完成' || s === '已签收') return 'success'
  if (s === '待退款' || s === '已拒绝') return 'danger'
  if (s === '待发货') return 'warning'
  return 'info'
}

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/order/list', { params: { page: page.value, size: size.value, ...search } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

const handleShip = (row) => {
  ElMessageBox.confirm('确认发货?').then(async () => {
    await request.put('/api/order/delivery', { orderId: row.orderId, deliveryState: '已配送' })
    ElMessage.success('发货成功')
    loadData()
  }).catch(() => {})
}

const handleState = (row) => {
  ElMessageBox.prompt('请输入新状态', '修改状态', { inputValue: row.state }).then(async ({ value }) => {
    await request.put('/api/order/state', { orderId: row.orderId, state: value })
    ElMessage.success('更新成功')
    loadData()
  }).catch(() => {})
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete('/api/order/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
