<template>
  <el-card>
    <!-- 分类管理区域 -->
    <div style="margin-bottom:20px;padding-bottom:16px;border-bottom:1px solid #e4e7ed">
      <h3 style="margin:0 0 16px">分类管理</h3>
      <div style="display:flex;justify-content:space-between;margin-bottom:12px">
        <el-button type="primary" @click="dialogVisible = true">新增分类</el-button>
      </div>
      <el-table :data="typeList" stripe v-loading="typeLoading" size="small">
        <el-table-column prop="typeId" label="ID" width="60" />
        <el-table-column prop="name" label="分类名称" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="80">
          <template #default="{row}">
            <el-button size="small" type="danger" @click="handleTypeDelete(row.typeId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 帖子管理区域 -->
    <h3 style="margin:0 0 16px">帖子管理</h3>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="forumId" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="nickname" label="发帖人" width="100" />
      <el-table-column prop="type" label="分类" width="100" />
      <el-table-column prop="hits" label="浏览" width="80" />
      <el-table-column prop="praiseLen" label="点赞" width="80" />
      <el-table-column prop="istop" label="置顶" width="70">
        <template #default="{row}"><el-tag :type="row.istop?'success':'info'" size="small">{{ row.istop?'是':'否' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="toggleTop(row)">{{ row.istop?'取消置顶':'置顶' }}</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.forumId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="page" v-model:page-size="size" :total="total" layout="total,prev,pager,next" @change="loadData" />
    
    <!-- 新增分类对话框 -->
    <el-dialog v-model="dialogVisible" title="新增分类">
      <el-form :model="typeForm" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="typeForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="typeForm.description" type="textarea" placeholder="请输入分类描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleTypeAdd">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

// 帖子管理数据
const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)

// 分类管理数据
const typeList = ref([])
const typeLoading = ref(false)
const dialogVisible = ref(false)
const typeForm = ref({ name: '', description: '' })

// 加载帖子列表
const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/forum/list', { params: { page: page.value, size: size.value } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

// 加载分类列表
const loadTypeData = async () => {
  typeLoading.value = true
  const res = await request.get('/api/forum/type/list')
  typeList.value = res.data || []
  typeLoading.value = false
}

// 切换置顶状态
const toggleTop = async (row) => {
  await request.put('/api/forum/update', { forumId: row.forumId, istop: row.istop ? 0 : 1 })
  ElMessage.success('操作成功')
  loadData()
}

// 删除帖子
const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete('/api/forum/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

// 新增分类
const handleTypeAdd = async () => {
  if (!typeForm.value.name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  await request.post('/api/forum/type/add', typeForm.value)
  ElMessage.success('添加成功')
  dialogVisible.value = false
  typeForm.value = { name: '', description: '' }
  loadTypeData()
}

// 删除分类
const handleTypeDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete(`/api/forum/type/delete/${id}`)
    ElMessage.success('删除成功')
    loadTypeData()
  }).catch(() => {})
}

// 初始化加载
onMounted(() => {
  loadData()
  loadTypeData()
})
</script>
