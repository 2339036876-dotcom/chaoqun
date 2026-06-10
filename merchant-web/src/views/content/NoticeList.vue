<template>
  <el-card>
    <div style="display:flex;justify-content:space-between;margin-bottom:16px">
      <el-input v-model="searchTitle" placeholder="搜索公告" clearable style="width:200px" @keyup.enter="loadData" />
      <el-button type="primary" @click="openDialog()">添加公告</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="noticeAnnouncementId" label="ID" width="60" />
      <el-table-column prop="notificationTitle" label="标题" min-width="200" />
      <el-table-column prop="notificationTime" label="通知时间" width="120" />
      <el-table-column prop="hits" label="浏览" width="80" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.noticeAnnouncementId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="page" v-model:page-size="size" :total="total" layout="total,prev,pager,next" @change="loadData" />
    <el-dialog v-model="dialogVisible" :title="editData?'编辑公告':'添加公告'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.notificationTitle" /></el-form-item>
        <el-form-item label="时间"><el-date-picker v-model="form.notificationTime" type="date" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.notificationContent" type="textarea" :rows="5" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
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
const searchTitle = ref('')
const dialogVisible = ref(false)
const editData = ref(null)
const form = reactive({ notificationTitle: '', notificationTime: '', notificationContent: '' })

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/notice/list', { params: { page: page.value, size: size.value, title: searchTitle.value || undefined } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

const openDialog = (row) => {
  editData.value = row || null
  if (row) { Object.assign(form, row) } else { form.notificationTitle = ''; form.notificationTime = ''; form.notificationContent = '' }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (editData.value) {
    await request.put('/api/notice/update', { ...editData.value, ...form })
  } else {
    await request.post('/api/notice/add', form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete('/api/notice/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
