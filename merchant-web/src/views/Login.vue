<template>
  <div class="login-container">
    <div class="login-shell">
      <section class="hero-panel">
        <div class="hero-badge">Merchant Console</div>
        <h1>电脑配件销售系统</h1>
        <p class="hero-text">
          面向管理端的统一工作台，集中处理商品维护、订单流转、内容发布与回收业务。
        </p>
        <div class="hero-grid">
          <article class="hero-card">
            <span>01</span>
            <strong>商品管理</strong>
            <p>快速维护商品图、库存、价格与详情内容。</p>
          </article>
          <article class="hero-card">
            <span>02</span>
            <strong>订单协同</strong>
            <p>发货、售后、物流状态集中跟进，减少切换成本。</p>
          </article>
          <article class="hero-card">
            <span>03</span>
            <strong>内容运营</strong>
            <p>资讯、公告、轮播图统一发布，管理入口更清晰。</p>
          </article>
        </div>
      </section>

      <section class="login-box">
        <div class="login-box-inner">
          <div class="login-kicker">Merchant Access</div>
          <h2>商家端登录</h2>
          <p class="sub-title">请输入商家账号和密码进入后台。</p>
          <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
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
      const res = await request.post('/api/user/admin/login', form)
      if (res.code !== 0) {
        ElMessage.error(res.msg)
        return
      }
      const user = res.data.user
      localStorage.setItem('merchant-token', res.data.token)
      localStorage.setItem('merchant-user', JSON.stringify(user))
      ElMessage.success('登录成功')
      router.push('/')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px;
  overflow: hidden;
  position: relative;
  background:
    radial-gradient(circle at top left, rgba(251, 191, 36, 0.22), transparent 28%),
    radial-gradient(circle at right 18%, rgba(34, 197, 94, 0.18), transparent 24%),
    linear-gradient(135deg, #0f172a 0%, #12304a 44%, #1d4d6d 100%);
}

.login-container::before,
.login-container::after {
  content: '';
  position: absolute;
  border-radius: 999px;
  filter: blur(10px);
}

.login-container::before {
  width: 360px;
  height: 360px;
  left: -80px;
  top: -120px;
  background: rgba(56, 189, 248, 0.18);
}

.login-container::after {
  width: 420px;
  height: 420px;
  right: -120px;
  bottom: -140px;
  background: rgba(250, 204, 21, 0.12);
}

.login-shell {
  width: min(1120px, 100%);
  min-height: 680px;
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  position: relative;
  z-index: 1;
  border: 1px solid rgba(255, 255, 255, 0.14);
  border-radius: 32px;
  overflow: hidden;
  background: rgba(7, 16, 29, 0.34);
  box-shadow: 0 32px 90px rgba(3, 10, 22, 0.34);
  backdrop-filter: blur(18px);
}

.hero-panel {
  padding: 56px 54px;
  color: #e5f3ff;
  position: relative;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.08), transparent 42%),
    linear-gradient(135deg, rgba(14, 116, 144, 0.12), rgba(59, 130, 246, 0.04));
}

.hero-panel::after {
  content: '';
  position: absolute;
  inset: auto 48px 42px auto;
  width: 220px;
  height: 220px;
  border-radius: 28px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  background:
    linear-gradient(135deg, rgba(255, 255, 255, 0.16), rgba(255, 255, 255, 0.03)),
    radial-gradient(circle at 30% 30%, rgba(251, 191, 36, 0.22), transparent 45%);
  transform: rotate(12deg);
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.09);
  border: 1px solid rgba(255, 255, 255, 0.14);
  font-size: 12px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
}

.hero-panel h1 {
  margin: 28px 0 16px;
  max-width: 420px;
  font-size: 46px;
  line-height: 1.08;
  color: #ffffff;
}

.hero-text {
  max-width: 500px;
  margin: 0;
  color: rgba(226, 232, 240, 0.88);
  font-size: 16px;
  line-height: 1.8;
}

.hero-grid {
  margin-top: 44px;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
  position: relative;
  z-index: 1;
}

.hero-card {
  padding: 20px 18px;
  border-radius: 22px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(10px);
}

.hero-card span {
  display: inline-flex;
  margin-bottom: 14px;
  color: #fcd34d;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.14em;
}

.hero-card strong {
  display: block;
  margin-bottom: 10px;
  font-size: 17px;
  color: #fff;
}

.hero-card p {
  margin: 0;
  color: rgba(226, 232, 240, 0.78);
  font-size: 13px;
  line-height: 1.7;
}

.login-box {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px;
  background: rgba(255, 255, 255, 0.9);
}

.login-box-inner {
  width: 100%;
  max-width: 380px;
  padding: 42px 36px;
  border-radius: 28px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.96), rgba(248, 250, 252, 0.92));
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.12);
}

.login-kicker {
  margin-bottom: 12px;
  color: #0f766e;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.16em;
  text-transform: uppercase;
}

.login-box h2 {
  margin: 0 0 10px;
  color: #0f172a;
  font-size: 32px;
  line-height: 1.15;
}

.sub-title {
  margin: 0 0 28px;
  color: #64748b;
  line-height: 1.7;
}

.submit-btn {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 14px;
  background: linear-gradient(135deg, #0f766e 0%, #0891b2 100%);
  box-shadow: 0 14px 28px rgba(8, 145, 178, 0.24);
}

:deep(.el-form-item) {
  margin-bottom: 22px;
}

:deep(.el-input__wrapper) {
  min-height: 46px;
  border-radius: 14px;
  box-shadow: 0 0 0 1px rgba(148, 163, 184, 0.18);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px rgba(8, 145, 178, 0.44);
}

@media (max-width: 980px) {
  .login-shell {
    grid-template-columns: 1fr;
    min-height: auto;
  }

  .hero-panel {
    padding: 42px 28px 24px;
  }

  .hero-panel::after {
    display: none;
  }

  .hero-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .login-container {
    padding: 16px;
  }

  .login-box {
    padding: 18px;
  }

  .login-box-inner {
    padding: 28px 22px;
  }

  .hero-panel h1 {
    font-size: 34px;
  }
}
</style>
