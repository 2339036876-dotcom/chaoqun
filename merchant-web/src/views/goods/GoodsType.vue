<template>
  <el-card>
    <div style="display:flex;justify-content:space-between;margin-bottom:16px">
      <h3 style="margin:0">商品分类管理</h3>
      <el-button type="primary" @click="openDialog()">添加分类</el-button>
    </div>
    <p style="margin:0 0 12px;font-size:13px;color:#909399">
      「序号」为列表排序；「分类ID」由数据库自增生成，删除记录后不会复用已占用的编号，这是正常现象，不影响使用。
    </p>
    <el-table :data="list" stripe>
      <el-table-column label="序号" width="70">
        <template #default="{ $index }">{{ $index + 1 }}</template>
      </el-table-column>
      <el-table-column prop="typeId" label="分类ID" width="90" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="desc" label="描述" />
      <el-table-column label="操作" width="160">
        <template #default="{row}">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.typeId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="editData ? '编辑分类' : '添加分类'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.desc" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="form.icon" /></el-form-item>
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
const dialogVisible = ref(false)
const editData = ref(null)
const form = reactive({ name: '', desc: '', icon: '' })

const resetForm = () => {
  form.name = ''
  form.desc = ''
  form.icon = ''
}

const loadData = async () => {
  const res = await request.get('/api/goods/type/list')
  if (res.code === 0) list.value = res.data || []
}

const openDialog = (row) => {
  editData.value = row || null
  if (row) {
    form.name = row.name || ''
    form.desc = row.desc || ''
    form.icon = row.icon || ''
  } else {
    resetForm()
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.name?.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }
  let res
  if (editData.value) {
    res = await request.put('/api/goods/type/update', {
      ...editData.value,
      name: form.name.trim(),
      desc: form.desc,
      icon: form.icon
    })
  } else {
    res = await request.post('/api/goods/type/add', {
      name: form.name.trim(),
      desc: form.desc,
      icon: form.icon
    })
  }
  if (res.code !== 0) {
    ElMessage.error(res.msg || '保存失败')
    return
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?', '提示', { type: 'warning' }).then(async () => {
    await request.delete('/api/goods/type/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
