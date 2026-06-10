<template>
  <div class="auth-page register-page">
    <div class="auth-shell">
      <section class="auth-hero">
        <div class="hero-tag">Create Account</div>
        <h1>加入电脑配件商城</h1>
        <p class="hero-desc">
          注册后即可收藏商品、管理订单、参与论坛互动，并接收平台公告与最新装机资讯。
        </p>
        <div class="hero-stats">
          <div class="stat-card">
            <strong>下单更便捷</strong>
            <span>默认保存个人资料与常用收货信息</span>
          </div>
          <div class="stat-card">
            <strong>互动更完整</strong>
            <span>收藏、评论、论坛发帖都将关联到个人账号</span>
          </div>
          <div class="stat-card">
            <strong>信息更及时</strong>
            <span>公告、资讯、订单状态都能持续跟踪</span>
          </div>
        </div>
      </section>

      <section class="auth-panel">
        <div class="auth-card">
          <div class="form-kicker">Quick Sign Up</div>
          <h2>用户注册</h2>
          <p class="sub-text">填写基础信息后即可创建新账号。</p>
          <el-form ref="formRef" :model="form" :rules="rules">
            <el-form-item prop="username">
              <el-input v-model="form.username" prefix-icon="User" placeholder="请输入用户名" size="large" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="form.password" prefix-icon="Lock" type="password" placeholder="请输入密码" size="large" show-password />
            </el-form-item>
            <el-form-item prop="nickname">
              <el-input v-model="form.nickname" prefix-icon="User" placeholder="请输入昵称" size="large" />
            </el-form-item>
            <el-form-item prop="email">
              <el-input v-model="form.email" :prefix-icon="Message" placeholder="请输入邮箱" size="large" />
            </el-form-item>
            <el-form-item prop="phone">
              <el-input v-model="form.phone" prefix-icon="Phone" placeholder="请输入手机号" size="large" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" class="submit-btn" @click="handleRegister">
                注册
              </el-button>
            </el-form-item>
            <div class="switch-link">
              <span>已经有账号？</span>
              <router-link to="/login">去登录</router-link>
            </div>
          </el-form>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Message } from '@element-plus/icons-vue'
import request from '../utils/request'

const router = useRouter()
const formRef = ref(null)
const form = reactive({ username: '', password: '', nickname: '', email: '', phone: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '邮箱格式不正确', trigger: 'blur' }
  ]
}

const handleRegister = () => {
  formRef.value.validate(async valid => {
    if (!valid) return
    const res = await request.post('/api/user/register', form)
    if (res.code === 0) {
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 28px;
  position: relative;
  overflow: hidden;
  background:
    radial-gradient(circle at left 18%, rgba(251, 191, 36, 0.2), transparent 24%),
    radial-gradient(circle at right bottom, rgba(168, 85, 247, 0.16), transparent 28%),
    linear-gradient(135deg, #fffbeb 0%, #f8fafc 52%, #eef2ff 100%);
}

.auth-page::before,
.auth-page::after {
  content: '';
  position: absolute;
  border-radius: 999px;
  filter: blur(12px);
}

.auth-page::before {
  width: 320px;
  height: 320px;
  left: -110px;
  top: 8%;
  background: rgba(245, 158, 11, 0.18);
}

.auth-page::after {
  width: 380px;
  height: 380px;
  right: -120px;
  bottom: -60px;
  background: rgba(99, 102, 241, 0.16);
}

.auth-shell {
  width: min(1140px, 100%);
  min-height: 720px;
  display: grid;
  grid-template-columns: 1.02fr 0.98fr;
  border-radius: 32px;
  overflow: hidden;
  position: relative;
  z-index: 1;
  background: rgba(255, 255, 255, 0.78);
  border: 1px solid rgba(255, 255, 255, 0.86);
  box-shadow: 0 32px 80px rgba(30, 41, 59, 0.1);
  backdrop-filter: blur(18px);
}

.auth-hero {
  padding: 56px 52px;
  position: relative;
  background:
    radial-gradient(circle at 22% 18%, rgba(250, 204, 21, 0.22), transparent 24%),
    linear-gradient(155deg, rgba(255, 255, 255, 0.72), rgba(255, 255, 255, 0.22));
}

.auth-hero::after {
  content: '';
  position: absolute;
  right: 46px;
  bottom: 42px;
  width: 250px;
  height: 220px;
  border-radius: 36px;
  background:
    radial-gradient(circle at 30% 30%, rgba(251, 191, 36, 0.22), transparent 42%),
    linear-gradient(145deg, rgba(99, 102, 241, 0.16), rgba(255, 255, 255, 0.18));
  border: 1px solid rgba(255, 255, 255, 0.74);
  transform: rotate(10deg);
}

.hero-tag {
  display: inline-flex;
  padding: 8px 14px;
  border-radius: 999px;
  color: #7c3aed;
  background: rgba(255, 255, 255, 0.82);
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.12em;
  text-transform: uppercase;
}

.auth-hero h1 {
  margin: 26px 0 16px;
  max-width: 420px;
  color: #111827;
  font-size: 46px;
  line-height: 1.08;
}

.hero-desc {
  max-width: 520px;
  margin: 0;
  color: #475569;
  font-size: 16px;
  line-height: 1.85;
}

.hero-stats {
  margin-top: 42px;
  display: grid;
  gap: 16px;
  position: relative;
  z-index: 1;
}

.stat-card {
  padding: 18px 20px;
  border-radius: 22px;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.86);
  box-shadow: 0 16px 34px rgba(148, 163, 184, 0.12);
}

.stat-card strong {
  display: block;
  margin-bottom: 8px;
  color: #111827;
  font-size: 17px;
}

.stat-card span {
  color: #64748b;
  font-size: 14px;
  line-height: 1.7;
}

.auth-panel {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 34px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.92), rgba(255, 255, 255, 0.8));
}

.auth-card {
  width: 100%;
  max-width: 406px;
  padding: 40px 34px;
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(226, 232, 240, 0.9);
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.08);
}

.form-kicker {
  margin-bottom: 12px;
  color: #7c3aed;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.16em;
  text-transform: uppercase;
}

.auth-card h2 {
  margin: 0 0 10px;
  color: #111827;
  font-size: 32px;
}

.sub-text {
  margin: 0 0 28px;
  color: #64748b;
  line-height: 1.7;
}

.submit-btn {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 14px;
  background: linear-gradient(135deg, #7c3aed 0%, #2563eb 100%);
  box-shadow: 0 14px 28px rgba(124, 58, 237, 0.18);
}

.switch-link {
  text-align: center;
  color: #64748b;
}

.switch-link a {
  margin-left: 6px;
  color: #7c3aed;
  text-decoration: none;
  font-weight: 600;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-input__wrapper) {
  min-height: 46px;
  border-radius: 14px;
  box-shadow: 0 0 0 1px rgba(203, 213, 225, 0.9);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px rgba(124, 58, 237, 0.42);
}

@media (max-width: 980px) {
  .auth-shell {
    grid-template-columns: 1fr;
    min-height: auto;
  }

  .auth-hero {
    padding: 40px 28px 24px;
  }

  .auth-hero::after {
    display: none;
  }
}

@media (max-width: 640px) {
  .auth-page {
    padding: 16px;
  }

  .auth-panel {
    padding: 18px;
  }

  .auth-card {
    padding: 28px 22px;
  }

  .auth-hero h1 {
    font-size: 36px;
  }
}
</style>
