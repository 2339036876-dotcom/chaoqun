<template>
  <div v-if="loadError" style="background: #fff; padding: 24px; border-radius: 8px; color: #909399">
    {{ loadError }}
  </div>
  <div v-else-if="goods" style="background: #fff; padding: 24px; border-radius: 8px">
    <el-row :gutter="30">
      <el-col :span="10">
        <el-image :src="activeImage" style="width: 100%; border-radius: 8px" fit="contain">
          <template #error>
            <div style="height: 400px; display: flex; align-items: center; justify-content: center; background: #f9f9f9">
              <el-icon :size="60" style="color: #ddd"><Picture /></el-icon>
            </div>
          </template>
        </el-image>
        <div v-if="galleryImages.length > 1" class="gallery-strip">
          <div
            v-for="image in galleryImages"
            :key="image"
            class="gallery-item"
            :class="{ active: image === activeImage }"
            @click="activeImage = image"
          >
            <el-image :src="image" fit="cover" />
          </div>
        </div>
      </el-col>
      <el-col :span="14">
        <h2 style="margin: 0 0 12px">{{ goods.title }}</h2>
        <p style="color: #999; margin-bottom: 16px">{{ goods.description }}</p>
        <div style="background: #fdf6ec; padding: 16px; border-radius: 8px; margin-bottom: 20px">
          <span style="color: #f56c6c; font-size: 32px; font-weight: bold">¥{{ goods.price }}</span>
          <span style="color: #ccc; text-decoration: line-through; margin-left: 12px; font-size: 16px">¥{{ goods.priceAgo }}</span>
        </div>
        <div style="color: #666; margin-bottom: 12px">销量: {{ goods.sales }} | 库存: {{ goods.inventory }} | 浏览: {{ goods.hits }}</div>
        <div v-if="inStock" style="display: flex; align-items: center; gap: 16px; margin-bottom: 20px">
          <span>数量:</span>
          <el-input-number v-model="num" :min="1" :max="goods.inventory" />
        </div>
        <el-alert v-else type="warning" :closable="false" title="暂时缺货" description="商品信息仍可浏览，补货后可再购买。您仍可联系商家咨询。" style="margin-bottom: 20px" />
        <div style="display: flex; gap: 12px; flex-wrap: wrap">
          <el-button type="primary" size="large" :disabled="!inStock" @click="addToCart">加入购物车</el-button>
          <el-button type="danger" size="large" :disabled="!inStock" @click="buyNow">立即购买</el-button>
          <el-button size="large" @click="contactMerchant">联系商家</el-button>
          <el-button :type="collected ? 'warning' : 'default'" size="large" @click="toggleCollect">
            {{ collected ? '已收藏' : '收藏' }}
          </el-button>
        </div>
      </el-col>
    </el-row>
    <el-divider />
    <div v-html="goods.content" style="line-height: 1.8"></div>
    <el-divider />
    <h3>商品评分 (平均 {{ scoreAvg }} 分)</h3>
    <div style="margin-bottom: 16px" v-if="isLogin">
      <el-rate v-model="myScore" :max="5" allow-half style="margin-bottom: 8px" />
      <el-button size="small" @click="submitScore">提交评分</el-button>
    </div>
    <h3>用户评论</h3>
    <div v-for="c in comments" :key="c.commentId" style="border-bottom: 1px solid #f0f0f0; padding: 12px 0">
      <div style="color: #333">{{ c.content }}</div>
      <div v-if="c.reply" style="color: #409eff; margin-top: 4px">商家回复: {{ c.reply }}</div>
      <div style="color: #999; font-size: 12px; margin-top: 4px">{{ c.createTime }}</div>
    </div>
    <div v-if="isLogin" style="margin-top: 16px; display: flex; gap: 10px">
      <el-input v-model="commentText" placeholder="写评论..." />
      <el-button type="primary" @click="submitComment">发表</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const route = useRoute()
const router = useRouter()
const goods = ref(null)
const loadError = ref('')
const num = ref(1)
const collected = ref(false)
const comments = ref([])
const commentText = ref('')
const scoreAvg = ref(0)
const myScore = ref(5)
const activeImage = ref('')
const isLogin = computed(() => !!localStorage.getItem('user-token'))
const inStock = computed(() => goods.value && (goods.value.inventory ?? 0) > 0)
const galleryImages = computed(() => {
  if (!goods.value) return []
  return [goods.value.img, goods.value.img1, goods.value.img2, goods.value.img3, goods.value.img4, goods.value.img5]
    .filter(Boolean)
})

const loadGoods = async () => {
  loadError.value = ''
  const res = await request.get('/api/goods/detail/' + route.params.id)
  if (res.code === 0 && res.data) {
    goods.value = res.data
    activeImage.value = galleryImages.value[0] || ''
    const inv = goods.value.inventory ?? 0
    num.value = inv > 0 ? Math.min(num.value, inv) : 1
  } else {
    goods.value = null
    loadError.value = res.msg || '商品不存在或已下架'
  }
}

const loadComments = async () => {
  const res = await request.get('/api/comment/list/goods/' + route.params.id)
  comments.value = res.data?.list || []
}

const loadScore = async () => {
  const res = await request.get('/api/score/list/goods/' + route.params.id)
  if (res.code === 0) scoreAvg.value = res.data?.avg || 0
}

const checkCollect = async () => {
  if (!isLogin.value) return
  const res = await request.get('/api/collect/check', { params: { sourceTable: 'goods', sourceId: route.params.id } })
  collected.value = res.data
}

const addToCart = async () => {
  if (!isLogin.value) {
    router.push('/login')
    return
  }
  if (!inStock.value) {
    ElMessage.warning('库存不足')
    return
  }
  const res = await request.post('/api/cart/add', { goodsId: Number(route.params.id), num: num.value })
  if (res.code !== 0) {
    ElMessage.error(res.msg || '加入购物车失败')
    return
  }
  ElMessage.success('已加入购物车')
}

const buyNow = () => {
  if (!isLogin.value) {
    router.push('/login')
    return
  }
  if (!inStock.value) {
    ElMessage.warning('库存不足')
    return
  }
  router.push({ path: '/checkout', query: { goodsId: route.params.id, num: num.value } })
}

const contactMerchant = () => {
  if (!isLogin.value) {
    router.push('/login')
    return
  }
  router.push({ path: `/goods/${route.params.id}/chat` })
}

const toggleCollect = async () => {
  if (!isLogin.value) {
    router.push('/login')
    return
  }
  if (collected.value) {
    ElMessage.info('已收藏')
  } else {
    await request.post('/api/collect/add', { sourceTable: 'goods', sourceId: Number(route.params.id), title: goods.value?.title, img: goods.value?.img })
    collected.value = true
    ElMessage.success('收藏成功')
  }
}

const submitComment = async () => {
  if (!commentText.value) return
  await request.post('/api/comment/add', { content: commentText.value, sourceTable: 'goods', sourceId: Number(route.params.id) })
  commentText.value = ''
  ElMessage.success('评论成功')
  loadComments()
}

const submitScore = async () => {
  await request.post('/api/score/add', { scoreNum: myScore.value, sourceTable: 'goods', sourceId: Number(route.params.id) })
  ElMessage.success('评分成功')
  loadScore()
}

onMounted(() => {
  loadGoods()
  loadComments()
  loadScore()
  checkCollect()
})
</script>

<style scoped>
.gallery-strip {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 16px;
}

.gallery-item {
  width: 72px;
  height: 72px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
}

.gallery-item :deep(img) {
  width: 100%;
  height: 100%;
}

.gallery-item.active {
  border-color: #409eff;
}
</style>
