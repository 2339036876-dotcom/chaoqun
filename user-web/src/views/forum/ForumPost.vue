<template>
  <div style="background:#fff;padding:24px;border-radius:8px">
    <h2 style="margin:0 0 20px">发帖</h2>
    <el-form :model="form" label-width="80px" style="max-width:700px">
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.type" placeholder="请选择">
          <el-option v-for="t in types" :key="t.typeId" :label="t.name" :value="t.name" />
        </el-select>
      </el-form-item>
      <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="10" /></el-form-item>
      <el-form-item label="标签"><el-input v-model="form.tag" placeholder="多个标签用逗号分隔" /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">发布</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const router = useRouter()
const types = ref([])
const form = reactive({ title: '', type: '', content: '', tag: '' })

onMounted(async () => {
  const res = await request.get('/api/forum/type/list')
  types.value = res.data || []
})

const handleSubmit = async () => {
  if (!form.title) { ElMessage.warning('请输入标题'); return }
  await request.post('/api/forum/add', form)
  ElMessage.success('发布成功')
  router.push('/forum')
}
</script>
