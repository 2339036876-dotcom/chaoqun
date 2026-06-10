<template>
  <div class="auth-page">
    <div class="auth-shell">
      <section class="auth-hero">
        <div class="hero-tag">User Portal</div>
        <h1>电脑配件商城</h1>
        <p class="hero-desc">
          登录后即可查看订单、收藏商品、管理收货地址，并持续关注装机资讯与平台公告。
        </p>
        <div class="hero-points">
          <div class="point-card">
            <strong>精选配件</strong>
            <span>热门装机单品与整机方案集中浏览</span>
          </div>
          <div class="point-card">
            <strong>购买流程清晰</strong>
            <span>购物车、下单、物流、售后完整串联</span>
          </div>
          <div class="point-card">
            <strong>内容持续更新</strong>
            <span>资讯、论坛、公告统一在前台可见</span>
          </div>
        </div>
      </section>

      <section class="auth-panel">
        <div class="auth-card">
          <div class="form-kicker">Welcome Back</div>
          <h2>用户登录</h2>
          <p class="sub-text">输入账号信息后进入商城个人中心。</p>
          <el-form ref="formRef" :model="form" :rules="rules">
            <el-form-item prop="username">
              <el-input v-model="form.username" prefix-icon="User" placeholder="请输入用户名" size="large" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="form.password"
                prefix-icon="Lock"
                type="password"
                placeholder="请输入密码"
                size="large"
                show-password
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" class="submit-btn" :loading="loading" @click="handleLogin">
                登录
              </el-button>
            </el-form-item>
            <div class="switch-link">
              <span>还没有账号？</span>
              <router-link to="/register">立即注册</router-link>
            </div>
          </el-form>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = () => {
  formRef.value.validate(async valid => {
    if (!valid) return
    loading.value = true
    try {
      const res = await request.post('/api/user/login', form)
      if (res.code !== 0) {
        ElMessage.error(res.msg)
        return
      }
      localStorage.setItem('user-token', res.data.token)
      localStorage.setItem('user-info', JSON.stringify(res.data.user))
      ElMessage.success('登录成功')
      router.push('/')
    } finally {
      loading.value = false
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
    radial-gradient(circle at top left, rgba(249, 115, 22, 0.18), transparent 24%),
    radial-gradient(circle at bottom right, rgba(14, 165, 233, 0.18), transparent 28%),
    linear-gradient(135deg, #fff7ed 0%, #eff6ff 48%, #ecfeff 100%);
}

.auth-page::before,
.auth-page::after {
  content: '';
  position: absolute;
  border-radius: 999px;
  filter: blur(14px);
}

.auth-page::before {
  width: 300px;
  height: 300px;
  left: -80px;
  bottom: 12%;
  background: rgba(251, 146, 60, 0.22);
}

.auth-page::after {
  width: 360px;
  height: 360px;
  right: -120px;
  top: -80px;
  background: rgba(59, 130, 246, 0.18);
}

.auth-shell {
  width: min(1120px, 100%);
  min-height: 700px;
  display: grid;
  grid-template-columns: 1.08fr 0.92fr;
  border-radius: 32px;
  overflow: hidden;
  position: relative;
  z-index: 1;
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid rgba(255, 255, 255, 0.86);
  box-shadow: 0 30px 80px rgba(15, 23, 42, 0.12);
  backdrop-filter: blur(16px);
}

.auth-hero {
  padding: 56px 52px;
  position: relative;
  background:
    radial-gradient(circle at 18% 20%, rgba(251, 146, 60, 0.2), transparent 24%),
    linear-gradient(160deg, rgba(255, 255, 255, 0.7), rgba(255, 255, 255, 0.26));
}

.auth-hero::after {
  content: '';
  position: absolute;
  right: 42px;
  bottom: 38px;
  width: 260px;
  height: 180px;
  border-radius: 30px;
  background:
    linear-gradient(135deg, rgba(14, 165, 233, 0.16), rgba(37, 99, 235, 0.06)),
    linear-gradient(180deg, rgba(255, 255, 255, 0.44), rgba(255, 255, 255, 0.1));
  border: 1px solid rgba(255, 255, 255, 0.7);
  transform: rotate(-8deg);
}

.hero-tag {
  display: inline-flex;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.84);
  color: #c2410c;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  box-shadow: 0 10px 30px rgba(249, 115, 22, 0.1);
}

.auth-hero h1 {
  margin: 26px 0 16px;
  max-width: 420px;
  color: #0f172a;
  font-size: 48px;
  line-height: 1.06;
}

.hero-desc {
  max-width: 520px;
  margin: 0;
  color: #475569;
  font-size: 16px;
  line-height: 1.85;
}

.hero-points {
  margin-top: 42px;
  display: grid;
  gap: 16px;
  position: relative;
  z-index: 1;
}

.point-card {
  padding: 18px 20px;
  border-radius: 22px;
  background: rgba(255, 255, 255, 0.76);
  border: 1px solid rgba(255, 255, 255, 0.82);
  box-shadow: 0 16px 34px rgba(148, 163, 184, 0.12);
}

.point-card strong {
  display: block;
  margin-bottom: 8px;
  color: #0f172a;
  font-size: 17px;
}

.point-card span {
  color: #64748b;
  font-size: 14px;
  line-height: 1.7;
}

.auth-panel {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 34px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.9), rgba(255, 255, 255, 0.78));
}

.auth-card {
  width: 100%;
  max-width: 392px;
  padding: 40px 34px;
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.94);
  border: 1px solid rgba(226, 232, 240, 0.9);
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.08);
}

.form-kicker {
  margin-bottom: 12px;
  color: #ea580c;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.16em;
  text-transform: uppercase;
}

.auth-card h2 {
  margin: 0 0 10px;
  color: #0f172a;
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
  background: linear-gradient(135deg, #f97316 0%, #2563eb 100%);
  box-shadow: 0 14px 28px rgba(37, 99, 235, 0.18);
}

.switch-link {
  text-align: center;
  color: #64748b;
}

.switch-link a {
  margin-left: 6px;
  color: #2563eb;
  text-decoration: none;
  font-weight: 600;
}

:deep(.el-form-item) {
  margin-bottom: 22px;
}

:deep(.el-input__wrapper) {
  min-height: 46px;
  border-radius: 14px;
  box-shadow: 0 0 0 1px rgba(203, 213, 225, 0.9);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px rgba(37, 99, 235, 0.42);
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
