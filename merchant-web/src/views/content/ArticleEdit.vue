<template>
  <el-card>
    <template #header>{{ isEdit ? '编辑文章' : '添加文章' }}</template>
    <el-form :model="form" label-width="80px" style="max-width: 800px">
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.type" placeholder="请选择">
          <el-option v-for="t in types" :key="t.typeId" :label="t.name" :value="t.typeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="来源"><el-input v-model="form.source" /></el-form-item>
      <el-form-item label="封面图">
        <div class="upload-block">
          <el-upload class="image-uploader" :show-file-list="false" :http-request="handleUpload" accept="image/*">
            <el-image v-if="form.img" :src="form.img" class="upload-preview" fit="cover" />
            <el-icon v-else class="upload-icon"><Plus /></el-icon>
          </el-upload>
          <el-button v-if="form.img" text type="danger" @click="form.img = ''">移除图片</el-button>
        </div>
      </el-form-item>
      <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="2" /></el-form-item>
      <el-form-item label="正文"><el-input v-model="form.content" type="textarea" :rows="10" /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
        <el-button @click="$router.back()">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'
import { uploadFile } from '../../utils/upload'

const route = useRoute()
const router = useRouter()
const types = ref([])
const isEdit = computed(() => !!route.query.id)
const form = reactive({ title: '', type: null, source: '', img: '', description: '', content: '' })

onMounted(async () => {
  const res = await request.get('/api/article/type/list')
  if (res.code === 0) types.value = res.data || []
  if (isEdit.value) {
    const r = await request.get('/api/article/detail/' + route.query.id)
    if (r.code === 0) Object.assign(form, r.data)
  }
})

const handleUpload = async ({ file }) => {
  const res = await uploadFile(file)
  if (res.code !== 0) {
    ElMessage.error(res.msg || '上传失败')
    return
  }
  form.img = res.data
  ElMessage.success('上传成功')
}

const handleSubmit = async () => {
  if (isEdit.value) {
    await request.put('/api/article/update', form)
  } else {
    await request.post('/api/article/add', form)
  }
  ElMessage.success('保存成功')
  router.push('/article')
}
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
  width: 180px;
  height: 120px;
  display: block;
}

.upload-icon {
  width: 180px;
  height: 120px;
  font-size: 28px;
  color: #8c939d;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
