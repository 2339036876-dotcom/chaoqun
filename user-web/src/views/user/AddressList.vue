<template>
  <div style="background:#fff;padding:24px;border-radius:8px">
    <div style="display:flex;justify-content:space-between;margin-bottom:20px">
      <h2 style="margin:0">收货地址</h2>
      <el-button type="primary" @click="openDialog()">新增地址</el-button>
    </div>
    <div v-for="a in list" :key="a.addressId" style="border:1px solid #eee;padding:16px;border-radius:8px;margin-bottom:12px;display:flex;justify-content:space-between;align-items:center">
      <div>
        <span style="font-weight:bold;margin-right:12px">{{ a.name }}</span>
        <span style="margin-right:12px">{{ a.phone }}</span>
        <span>{{ a.address }}</span>
        <el-tag v-if="a.isDefault" size="small" type="success" style="margin-left:8px">默认</el-tag>
      </div>
      <div>
        <el-button size="small" @click="openDialog(a)">编辑</el-button>
        <el-button size="small" type="danger" @click="handleDelete(a.addressId)">删除</el-button>
      </div>
    </div>
    <el-dialog v-model="dialogVisible" :title="editData?'编辑地址':'新增地址'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="邮编"><el-input v-model="form.postcode" /></el-form-item>
        <el-form-item label="默认"><el-switch v-model="form.isDefault" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const list = ref([])
const dialogVisible = ref(false)
const editData = ref(null)
const form = reactive({ name: '', phone: '', address: '', postcode: '', isDefault: false })

const loadData = async () => {
  const res = await request.get('/api/address/list')
  list.value = res.data || []
}

const openDialog = (row) => {
  editData.value = row || null
  if (row) { Object.assign(form, row) } else { form.name=''; form.phone=''; form.address=''; form.postcode=''; form.isDefault=false }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (editData.value) { await request.put('/api/address/update', { ...editData.value, ...form }) }
  else { await request.post('/api/address/add', form) }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete('/api/address/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>
