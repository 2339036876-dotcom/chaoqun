import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    children: [
      { path: '', component: () => import('../views/Home.vue') },
      { path: 'goods', component: () => import('../views/goods/GoodsList.vue') },
      { path: 'goods/:id', component: () => import('../views/goods/GoodsDetail.vue') },
      { path: 'goods/:id/chat', component: () => import('../views/goods/GoodsChat.vue'), meta: { auth: true } },
      { path: 'cart', component: () => import('../views/cart/Cart.vue'), meta: { auth: true } },
      { path: 'order', component: () => import('../views/order/OrderList.vue'), meta: { auth: true } },
      { path: 'order/:id', component: () => import('../views/order/OrderDetail.vue'), meta: { auth: true } },
      { path: 'checkout', component: () => import('../views/order/Checkout.vue'), meta: { auth: true } },
      { path: 'profile', component: () => import('../views/user/Profile.vue'), meta: { auth: true } },
      { path: 'address', component: () => import('../views/user/AddressList.vue'), meta: { auth: true } },
      { path: 'collect', component: () => import('../views/user/CollectList.vue'), meta: { auth: true } },
      { path: 'forum', component: () => import('../views/forum/ForumList.vue') },
      { path: 'forum/:id', component: () => import('../views/forum/ForumDetail.vue') },
      { path: 'forum/post', component: () => import('../views/forum/ForumPost.vue'), meta: { auth: true } },
      { path: 'article', component: () => import('../views/article/ArticleList.vue') },
      { path: 'article/:id', component: () => import('../views/article/ArticleDetail.vue') },
      { path: 'notice', component: () => import('../views/notice/NoticeList.vue') },
      { path: 'notice/:id', component: () => import('../views/notice/NoticeDetail.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.auth && !localStorage.getItem('user-token')) {
    next('/login')
  } else {
    next()
  }
})

export default router
