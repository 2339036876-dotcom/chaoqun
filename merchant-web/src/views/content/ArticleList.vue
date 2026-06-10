<template>
  <el-card>
    <div style="display:flex;justify-content:space-between;margin-bottom:16px">
      <el-input v-model="searchTitle" placeholder="搜索文章" clearable style="width:200px" @clear="loadData" @keyup.enter="loadData" />
      <el-button type="success" @click="$router.push('/article/edit')">添加文章</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="articleId" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="hits" label="浏览" width="80" />
      <el-table-column prop="praiseLen" label="点赞" width="80" />
      <el-table-column prop="createTime" label="时间" width="170" />
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.articleId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="page" v-model:page-size="size" :total="total" layout="total,prev,pager,next" @change="loadData" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const router = useRouter()
const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)
const searchTitle = ref('')

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/article/list', { params: { page: page.value, size: size.value, title: searchTitle.value || undefined } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

const handleEdit = (row) => {
  router.push({ path: '/article/edit', query: { id: row.articleId } })
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete('/api/article/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
