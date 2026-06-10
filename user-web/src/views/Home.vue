<template>
  <div>
    <el-carousel height="360px" style="border-radius: 8px; overflow: hidden; margin-bottom: 20px">
      <el-carousel-item v-for="item in slides" :key="item.slidesId">
        <div class="slide" @click="item.url && $router.push(item.url)">
          <el-image :src="item.img" fit="cover" style="width: 100%; height: 100%">
            <template #error>
              <div style="display: flex; align-items: center; justify-content: center; height: 100%; background: linear-gradient(135deg, #0f766e, #2563eb); color: #fff">
                <div style="text-align: center">
                  <h2 style="margin: 0">{{ item.title }}</h2>
                  <p style="font-size: 16px; margin-top: 10px; opacity: 0.85">{{ item.content }}</p>
                </div>
              </div>
            </template>
          </el-image>
        </div>
      </el-carousel-item>
    </el-carousel>

    <h3 style="margin-bottom: 16px">商品分类</h3>
    <div style="display: flex; gap: 12px; flex-wrap: wrap; margin-bottom: 30px">
      <el-tag
        v-for="item in types"
        :key="item.typeId"
        size="large"
        style="cursor: pointer; font-size: 14px; padding: 8px 20px"
        @click="$router.push('/goods?type=' + item.typeId)"
      >
        {{ item.name }}
      </el-tag>
    </div>

    <h3 style="margin-bottom: 16px">热门商品</h3>
    <el-row :gutter="16">
      <el-col v-for="item in hotGoods" :key="item.goodsId" :xs="12" :sm="8" :md="6">
        <el-card shadow="hover" style="cursor: pointer; margin-bottom: 16px" @click="$router.push('/goods/' + item.goodsId)">
          <div class="goods-cover">
            <el-image :src="item.img" style="max-height: 160px; max-width: 100%" fit="contain">
              <template #error><el-icon :size="40" style="color: #ddd"><Picture /></el-icon></template>
            </el-image>
          </div>
          <div class="goods-title">{{ item.title }}</div>
          <div class="goods-meta">
            <span class="goods-price">￥{{ item.price }}</span>
            <span class="goods-sales">销量 {{ item.sales }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import request from '../utils/request'

const slides = ref([])
const types = ref([])
const hotGoods = ref([])

onMounted(async () => {
  const [slideRes, typeRes, goodsRes] = await Promise.all([
    request.get('/api/slides/list'),
    request.get('/api/goods/type/list'),
    request.get('/api/goods/list', { params: { page: 1, size: 8, sort: 'sales' } })
  ])
  slides.value = slideRes.data || []
  types.value = typeRes.data || []
  hotGoods.value = goodsRes.data?.list || []
})
</script>

<style scoped>
.slide {
  height: 360px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0f766e, #2563eb);
  color: #fff;
  font-size: 28px;
  cursor: pointer;
}

.goods-cover {
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f9f9f9;
  border-radius: 4px;
  margin-bottom: 12px;
}

.goods-title {
  font-size: 14px;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goods-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.goods-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: 700;
}

.goods-sales {
  color: #999;
  font-size: 12px;
}
</style>
