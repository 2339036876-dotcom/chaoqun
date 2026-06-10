<template>
  <div style="background: #fff; padding: 24px; border-radius: 8px">
    <h2 style="margin: 0 0 20px">个人中心</h2>
    <el-form :model="form" label-width="80px" style="max-width: 500px">
      <el-form-item label="用户名"><el-input v-model="form.username" disabled /></el-form-item>
      <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
      <el-form-item label="手机"><el-input v-model="form.phone" /></el-form-item>
      <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
      <el-form-item label="头像">
        <div class="upload-block">
          <el-upload class="avatar-uploader" :show-file-list="false" :http-request="handleUpload" accept="image/*">
            <el-avatar v-if="form.avatar" :src="form.avatar" :size="96" />
            <el-icon v-else class="avatar-icon"><Plus /></el-icon>
          </el-upload>
          <el-button v-if="form.avatar" text type="danger" @click="form.avatar = ''">移除头像</el-button>
        </div>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="handleSave">保存修改</el-button></el-form-item>
    </el-form>
    <el-divider />
    <h3>修改密码</h3>
    <el-form :model="pwdForm" label-width="80px" style="max-width: 500px">
      <el-form-item label="原密码"><el-input v-model="pwdForm.oldPassword" type="password" show-password /></el-form-item>
      <el-form-item label="新密码"><el-input v-model="pwdForm.newPassword" type="password" show-password /></el-form-item>
      <el-form-item><el-button type="warning" @click="changePwd">修改密码</el-button></el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'
import { uploadFile } from '../../utils/upload'

const form = reactive({ username: '', nickname: '', phone: '', email: '', avatar: '' })
const pwdForm = reactive({ oldPassword: '', newPassword: '' })

onMounted(async () => {
  const res = await request.get('/api/user/info')
  if (res.code === 0) Object.assign(form, res.data)
})

const handleUpload = async ({ file }) => {
  const res = await uploadFile(file)
  if (res.code !== 0) {
    ElMessage.error(res.msg || '上传失败')
    return
  }
  form.avatar = res.data
  ElMessage.success('上传成功')
}

const handleSave = async () => {
  const res = await request.put('/api/user/update', form)
  if (res.code === 0) {
    ElMessage.success('保存成功')
    localStorage.setItem('user-info', JSON.stringify({ ...JSON.parse(localStorage.getItem('user-info') || '{}'), ...form }))
  }
}

const changePwd = async () => {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) {
    ElMessage.warning('请填写密码')
    return
  }
  const res = await request.put('/api/user/password', pwdForm)
  if (res.code === 0) {
    ElMessage.success('密码修改成功')
    pwdForm.oldPassword = ''
    pwdForm.newPassword = ''
  } else {
    ElMessage.error(res.msg)
  }
}
</script>

<style scoped>
.upload-block {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.avatar-uploader :deep(.el-upload) {
  width: 96px;
  height: 96px;
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 28px;
  color: #8c939d;
}
</style>
