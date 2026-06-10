<template>
  <el-card>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="commentId" label="ID" width="60" />
      <el-table-column prop="content" label="评论内容" min-width="250" show-overflow-tooltip />
      <el-table-column prop="reply" label="回复" min-width="200" show-overflow-tooltip />
      <el-table-column prop="sourceTable" label="来源" width="100" />
      <el-table-column prop="createTime" label="时间" width="170" />
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" type="primary" @click="handleReply(row)">回复</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.commentId)">删除</el-button>
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
  const res = await request.get('/api/comment/list/goods/0', { params: { page: page.value, size: size.value } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

const handleReply = (row) => {
  ElMessageBox.prompt('请输入回复', '回复评论', { inputType: 'textarea' }).then(async ({ value }) => {
    await request.put('/api/comment/reply', { commentId: row.commentId, reply: value })
    ElMessage.success('回复成功')
    loadData()
  }).catch(() => {})
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete('/api/comment/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
