import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '首页概况' } },
      { path: 'goods', component: () => import('../views/goods/GoodsList.vue'), meta: { title: '商品管理' } },
      { path: 'goods/add', component: () => import('../views/goods/GoodsEdit.vue'), meta: { title: '添加商品' } },
      { path: 'goods/edit/:id', component: () => import('../views/goods/GoodsEdit.vue'), meta: { title: '编辑商品' } },
      { path: 'goods/type', component: () => import('../views/goods/GoodsType.vue'), meta: { title: '商品分类' } },
      { path: 'order', component: () => import('../views/order/OrderList.vue'), meta: { title: '订单管理' } },
      { path: 'order/afterSale', component: () => import('../views/order/AfterSale.vue'), meta: { title: '售后管理' } },
      { path: 'user', component: () => import('../views/user/UserList.vue'), meta: { title: '用户管理' } },
      { path: 'consultation', component: () => import('../views/merchant/ServiceList.vue'), meta: { title: '会话管理' } },
      { path: 'consultation/:sessionId', component: () => import('../views/merchant/ServiceChat.vue'), meta: { title: '会话详情' } },
      { path: 'article', component: () => import('../views/content/ArticleList.vue'), meta: { title: '文章管理' } },
      { path: 'article/edit', component: () => import('../views/content/ArticleEdit.vue'), meta: { title: '编辑文章' } },
      { path: 'notice', component: () => import('../views/content/NoticeList.vue'), meta: { title: '公告管理' } },
      { path: 'forum', component: () => import('../views/content/ForumList.vue'), meta: { title: '论坛管理' } },
      { path: 'slides', component: () => import('../views/system/SlidesList.vue'), meta: { title: '轮播图管理' } },
      { path: 'logistics', component: () => import('../views/order/LogisticsList.vue'), meta: { title: '物流管理' } },
      { path: 'comment', component: () => import('../views/content/CommentList.vue'), meta: { title: '评论管理' } },
      { path: 'admin', component: () => import('../views/system/AdminList.vue'), meta: { title: '管理员管理' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('merchant-token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
