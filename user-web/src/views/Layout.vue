<template>
  <div>
    <el-header class="header">
      <div class="header-left">
        <h1 class="brand" @click="$router.push('/')">电脑配件商城</h1>
        <el-menu mode="horizontal" :default-active="$route.path" :ellipsis="false" router class="top-menu">
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/goods">全部商品</el-menu-item>
          <el-menu-item index="/forum">论坛</el-menu-item>
          <el-menu-item index="/article">资讯</el-menu-item>
          <el-menu-item index="/notice">公告</el-menu-item>
        </el-menu>
      </div>
      <div class="header-right">
        <template v-if="isLogin">
          <el-badge :value="cartCount" :hidden="!cartCount">
            <el-icon style="font-size: 22px; cursor: pointer" @click="$router.push('/cart')"><ShoppingCart /></el-icon>
          </el-badge>
          <el-dropdown @command="handleCmd">
            <span class="user-entry">
              <el-avatar :size="28" :src="userInfo.avatar" style="margin-right: 6px">
                {{ (userInfo.nickname || 'U')[0] }}
              </el-avatar>
              {{ userInfo.nickname || userInfo.username }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="order">我的订单</el-dropdown-item>
                <el-dropdown-item command="collect">我的收藏</el-dropdown-item>
                <el-dropdown-item command="address">收货地址</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </el-header>
    <div style="max-width: 1200px; margin: 20px auto; padding: 0 20px; min-height: calc(100vh - 160px)">
      <router-view />
    </div>
    <footer class="footer">电脑配件销售系统 © 2026</footer>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()
const cartCount = ref(0)
const isLogin = computed(() => !!localStorage.getItem('user-token'))
const userInfo = computed(() => JSON.parse(localStorage.getItem('user-info') || '{}'))

const handleCmd = command => {
  if (command === 'logout') {
    localStorage.removeItem('user-token')
    localStorage.removeItem('user-info')
    router.push('/login')
    return
  }
  router.push('/' + command)
}

onMounted(async () => {
  if (!isLogin.value) return
  try {
    const res = await request.get('/api/cart/list')
    if (res.code === 0) {
      cartCount.value = (res.data || []).length
    }
  } catch {}
})
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  padding: 0;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  display: flex;
  align-items: center;
  padding-left: 20px;
}

.brand {
  font-size: 20px;
  color: #2563eb;
  margin: 0;
  cursor: pointer;
}

.top-menu {
  margin-left: 40px;
  border: none;
  height: 60px;
}

.header-right {
  padding-right: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-entry {
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #333;
}

.footer {
  text-align: center;
  padding: 20px;
  color: #999;
  font-size: 13px;
  background: #fff;
  border-top: 1px solid #eee;
}
</style>
