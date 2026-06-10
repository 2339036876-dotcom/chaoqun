<template>
  <el-card>
    <div style="display: flex; justify-content: flex-end; margin-bottom: 16px">
      <el-button type="primary" @click="openDialog()">添加轮播图</el-button>
    </div>
    <el-table :data="list" stripe>
      <el-table-column prop="slidesId" label="ID" width="60" />
      <el-table-column label="图片" width="120">
        <template #default="{ row }">
          <el-image :src="row.img" style="width: 80px; height: 45px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="content" label="内容" min-width="200" />
      <el-table-column prop="url" label="链接" min-width="150" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.slidesId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="editData ? '编辑' : '添加'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" /></el-form-item>
        <el-form-item label="链接"><el-input v-model="form.url" /></el-form-item>
        <el-form-item label="图片">
          <div class="upload-block">
            <el-upload class="image-uploader" :show-file-list="false" :http-request="handleUpload" accept="image/*">
              <el-image v-if="form.img" :src="form.img" class="upload-preview" fit="cover" />
              <el-icon v-else class="upload-icon"><Plus /></el-icon>
            </el-upload>
            <el-button v-if="form.img" text type="danger" @click="form.img = ''">移除图片</el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { uploadFile } from '../../utils/upload'

const list = ref([])
const dialogVisible = ref(false)
const editData = ref(null)
const form = reactive({ title: '', content: '', url: '', img: '' })

const loadData = async () => {
  const res = await request.get('/api/slides/list')
  if (res.code === 0) list.value = res.data || []
}

const openDialog = (row) => {
  editData.value = row || null
  if (row) {
    Object.assign(form, row)
  } else {
    form.title = ''
    form.content = ''
    form.url = ''
    form.img = ''
  }
  dialogVisible.value = true
}

const handleUpload = async ({ file }) => {
  const res = await uploadFile(file)
  if (res.code !== 0) {
    ElMessage.error(res.msg || '上传失败')
    return
  }
  form.img = res.data
  ElMessage.success('上传成功')
}

const handleSave = async () => {
  if (editData.value) {
    await request.put('/api/slides/update', { ...editData.value, ...form })
  } else {
    await request.post('/api/slides/add', form)
  }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除?').then(async () => {
    await request.delete('/api/slides/delete/' + id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(loadData)
</script>

<style scoped>
.upload-block {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.image-uploader :deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
}

.upload-preview {
  width: 160px;
  height: 90px;
  display: block;
}

.upload-icon {
  width: 160px;
  height: 90px;
  font-size: 28px;
  color: #8c939d;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
