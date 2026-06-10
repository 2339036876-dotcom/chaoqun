<template>
  <el-container style="height: 100vh">
    <el-aside :width="isCollapse ? '64px' : '220px'" style="background: #304156; transition: width 0.3s">
      <div class="logo">
        <span v-if="!isCollapse">配件销售管理端</span>
        <span v-else>管</span>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        :collapse="isCollapse"
        :collapse-transition="false"
      >
        <el-menu-item index="/dashboard"><el-icon><DataAnalysis /></el-icon><span>首页概况</span></el-menu-item>
        <el-sub-menu index="goods-menu">
          <template #title><el-icon><Goods /></el-icon><span>商品管理</span></template>
          <el-menu-item index="/goods">商品列表</el-menu-item>
          <el-menu-item index="/goods/add">添加商品</el-menu-item>
          <el-menu-item index="/goods/type">商品分类</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="order-menu">
          <template #title><el-icon><Document /></el-icon><span>订单管理</span></template>
          <el-menu-item index="/order">订单列表</el-menu-item>
          <el-menu-item index="/order/afterSale">售后管理</el-menu-item>
          <el-menu-item index="/logistics">物流管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/user"><el-icon><User /></el-icon><span>用户管理</span></el-menu-item>
        <el-menu-item index="/consultation"><el-icon><ChatDotRound /></el-icon><span>会话管理</span></el-menu-item>
        <el-sub-menu index="content-menu">
          <template #title><el-icon><Reading /></el-icon><span>内容管理</span></template>
          <el-menu-item index="/article">文章管理</el-menu-item>
          <el-menu-item index="/notice">公告管理</el-menu-item>
          <el-menu-item index="/forum">论坛管理</el-menu-item>
          <el-menu-item index="/comment">评论管理</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/slides"><el-icon><Picture /></el-icon><span>轮播图管理</span></el-menu-item>
        <el-menu-item index="/admin"><el-icon><Setting /></el-icon><span>管理员管理</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div style="display: flex; align-items: center">
          <el-icon style="cursor: pointer; font-size: 20px" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb style="margin-left: 16px">
            <el-breadcrumb-item>{{ $route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <el-dropdown @command="handleCommand">
          <span class="user-name">
            <el-icon style="margin-right: 6px"><UserFilled /></el-icon>{{ currentUser.nickname || currentUser.username }}
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main style="background: #f0f2f5; padding: 20px">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isCollapse = ref(false)
const currentUser = computed(() => JSON.parse(localStorage.getItem('merchant-user') || '{}'))

const handleCommand = command => {
  if (command === 'logout') {
    localStorage.removeItem('merchant-token')
    localStorage.removeItem('merchant-user')
    router.push('/login')
  }
}
</script>

<style scoped>
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: 700;
  background: #263445;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  padding: 0 20px;
}

.user-name {
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #333;
}
</style>
