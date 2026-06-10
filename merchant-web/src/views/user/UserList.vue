<template>
  <el-card>
    <div style="display:flex;gap:10px;margin-bottom:16px">
      <el-input v-model="searchName" placeholder="搜索用户名" clearable style="width:200px" />
      <el-button type="primary" @click="loadData">搜索</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="userId" label="ID" width="60" />
      <el-table-column label="头像" width="60">
        <template #default="{row}"><el-avatar :src="row.avatar" :size="36">{{ (row.nickname||'U')[0] }}</el-avatar></template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="nickname" label="昵称" width="120" />
      <el-table-column prop="phone" label="手机" width="120" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="state" label="状态" width="80">
        <template #default="{row}">
          <el-tag :type="row.state===1?'success':'danger'" size="small">{{ row.state===1?'正常':row.state===3?'冻结':'异常' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="170" />
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" :type="row.state===1?'warning':'success'" @click="toggleState(row)">{{ row.state===1?'冻结':'解冻' }}</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.userId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="page" v-model:page-size="size" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @change="loadData" @size-change="loadData" />
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
const searchName = ref('')

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/user/list', { params: { page: page.value, size: size.value, username: searchName.value || undefined } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

const toggleState = async (row) => {
  const newState = row.state === 1 ? 3 : 1
  await request.put('/api/user/state', { userId: row.userId, state: newState })
  ElMessage.success('操作成功')
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete('/api/user/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
