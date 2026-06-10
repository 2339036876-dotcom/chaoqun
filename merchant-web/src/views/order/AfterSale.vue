<template>
  <el-card>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="orderAfterSaleId" label="ID" width="60" />
      <el-table-column prop="orderId" label="订单ID" width="80" />
      <el-table-column prop="type" label="售后类型" width="120" />
      <el-table-column prop="contentDesc" label="售后内容" min-width="200" show-overflow-tooltip />
      <el-table-column prop="afterState" label="状态" width="100">
        <template #default="{row}"><el-tag :type="row.afterState==='已通过'?'success':row.afterState==='未通过'?'danger':'warning'" size="small">{{ row.afterState }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="170" />
      <el-table-column label="操作" width="200">
        <template #default="{row}">
          <template v-if="row.afterState==='未审核'">
            <el-button size="small" type="success" @click="handleReview(row,'已通过')">通过</el-button>
            <el-button size="small" type="danger" @click="handleReview(row,'未通过')">拒绝</el-button>
          </template>
          <el-button size="small" type="danger" @click="handleDelete(row.orderAfterSaleId)">删除</el-button>
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

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/order/afterSale/list', { params: { page: page.value, size: size.value } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

const handleReview = async (row, state) => {
  ElMessageBox.prompt('审核回复', '审核', { inputValue: '' }).then(async ({ value }) => {
    await request.put('/api/order/afterSale/review', { orderAfterSaleId: row.orderAfterSaleId, orderId: row.orderId, afterState: state, afterStateReply: value })
    ElMessage.success('审核完成')
    loadData()
  }).catch(() => {})
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete('/api/order/afterSale/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
