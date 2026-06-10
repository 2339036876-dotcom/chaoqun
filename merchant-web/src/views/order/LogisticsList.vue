<template>
  <el-card>
    <div style="margin-bottom:16px;display:flex;gap:10px">
      <el-input v-model="searchOrder" placeholder="搜索订单号" clearable style="width:200px" />
      <el-button type="primary" @click="loadData">搜索</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="logisticsDeliveryId" label="ID" width="60" />
      <el-table-column prop="orderNumber" label="订单号" width="180" />
      <el-table-column prop="productName" label="商品" min-width="150" />
      <el-table-column prop="purchaseQuantity" label="数量" width="60" />
      <el-table-column prop="totalTransactionAmount" label="总额" width="100">
        <template #default="{row}">¥{{ row.totalTransactionAmount }}</template>
      </el-table-column>
      <el-table-column prop="deliveryStatus" label="配送状态" width="100" />
      <el-table-column prop="signingStatus" label="签收状态" width="100" />
      <el-table-column prop="contactName" label="联系人" width="100" />
      <el-table-column label="操作" width="120">
        <template #default="{row}">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="page" v-model:page-size="size" :total="total" layout="total,prev,pager,next" @change="loadData" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)
const searchOrder = ref('')

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/logistics/list', { params: { page: page.value, size: size.value, orderNumber: searchOrder.value || undefined } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

const handleEdit = (row) => {
  ElMessageBox.prompt('配送状态', '更新', { inputValue: row.deliveryStatus }).then(async ({ value }) => {
    await request.put('/api/logistics/update', { ...row, deliveryStatus: value })
    ElMessage.success('更新成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
