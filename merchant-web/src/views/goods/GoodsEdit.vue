<template>
  <el-card>
    <template #header>{{ isEdit ? '编辑商品' : '添加商品' }}</template>
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" style="max-width: 800px">
      <el-form-item label="商品名称" prop="title">
        <el-input v-model="form.title" />
      </el-form-item>
      <el-form-item label="商品分类" prop="type">
        <el-select v-model="form.type" placeholder="请选择">
          <el-option v-for="t in types" :key="t.typeId" :label="t.name" :value="t.typeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="原价" prop="priceAgo">
        <el-input-number v-model="form.priceAgo" :min="0" :precision="2" />
      </el-form-item>
      <el-form-item label="售价" prop="price">
        <el-input-number v-model="form.price" :min="0" :precision="2" />
      </el-form-item>
      <el-form-item label="库存" prop="inventory">
        <el-input-number v-model="form.inventory" :min="0" />
      </el-form-item>

      <el-form-item label="封面图" prop="img">
        <div class="upload-block">
          <el-upload
            class="image-uploader"
            :show-file-list="false"
            :http-request="uploadSingleImage('img')"
            accept="image/*"
          >
            <el-image v-if="form.img" :src="form.img" class="upload-preview" fit="cover" />
            <el-icon v-else class="upload-icon"><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">点击上传封面图，不再手填 URL</div>
        </div>
      </el-form-item>

      <el-form-item label="详情图">
        <div class="detail-images">
          <div v-for="field in detailImageFields" :key="field.key" class="detail-image-item">
            <div class="detail-image-label">{{ field.label }}</div>
            <el-upload
              class="image-uploader"
              :show-file-list="false"
              :http-request="uploadSingleImage(field.key)"
              accept="image/*"
            >
              <el-image v-if="form[field.key]" :src="form[field.key]" class="upload-preview" fit="cover" />
              <el-icon v-else class="upload-icon"><Plus /></el-icon>
            </el-upload>
            <el-button v-if="form[field.key]" text type="danger" @click="form[field.key] = ''">移除</el-button>
          </div>
        </div>
      </el-form-item>

      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" :rows="3" />
      </el-form-item>
      <el-form-item label="详情内容">
        <el-input v-model="form.content" type="textarea" :rows="6" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          {{ isEdit ? '保存修改' : '立即添加' }}
        </el-button>
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
const formRef = ref(null)
const loading = ref(false)
const types = ref([])
const isEdit = computed(() => !!route.params.id)
const detailImageFields = [
  { key: 'img1', label: '详情图 1' },
  { key: 'img2', label: '详情图 2' },
  { key: 'img3', label: '详情图 3' },
  { key: 'img4', label: '详情图 4' },
  { key: 'img5', label: '详情图 5' }
]
const form = reactive({
  title: '',
  type: null,
  priceAgo: 0,
  price: 0,
  inventory: 0,
  img: '',
  img1: '',
  img2: '',
  img3: '',
  img4: '',
  img5: '',
  description: '',
  content: ''
})
const rules = {
  title: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入售价', trigger: 'blur' }],
  img: [{ required: true, message: '请上传封面图', trigger: 'change' }]
}

const uploadSingleImage = (field) => async ({ file }) => {
  const res = await uploadFile(file)
  if (res.code !== 0) {
    ElMessage.error(res.msg || '上传失败')
    return
  }
  form[field] = res.data
  ElMessage.success('上传成功')
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      if (isEdit.value) {
        await request.put('/api/goods/update', form)
        ElMessage.success('修改成功')
      } else {
        await request.post('/api/goods/add', form)
        ElMessage.success('添加成功')
      }
      router.push('/goods')
    } finally {
      loading.value = false
    }
  })
}

onMounted(async () => {
  const res = await request.get('/api/goods/type/list')
  if (res.code === 0) types.value = res.data || []
  if (isEdit.value) {
    const r = await request.get('/api/goods/detail/' + route.params.id)
    if (r.code === 0) Object.assign(form, r.data)
  }
})
</script>

<style scoped>
.upload-block {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-images {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.detail-image-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.detail-image-label {
  font-size: 12px;
  color: #666;
}

.image-uploader :deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.image-uploader :deep(.el-upload:hover) {
  border-color: var(--el-color-primary);
}

.upload-preview {
  width: 148px;
  height: 148px;
  display: block;
}

.upload-icon {
  width: 148px;
  height: 148px;
  font-size: 28px;
  color: #8c939d;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-tip {
  font-size: 12px;
  color: #999;
}
</style>
