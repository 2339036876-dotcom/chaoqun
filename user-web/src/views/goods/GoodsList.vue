<template>
  <div>
    <div style="background:#fff;padding:16px;border-radius:8px;margin-bottom:16px;display:flex;gap:10px;align-items:center;flex-wrap:wrap">
      <el-input v-model="search.title" placeholder="搜索商品" clearable style="width:220px" @keyup.enter="loadData" />
      <el-select v-model="search.type" placeholder="全部分类" clearable style="width:140px" @change="loadData">
        <el-option v-for="t in types" :key="t.typeId" :label="t.name" :value="t.typeId" />
      </el-select>
      <el-select v-model="search.sort" placeholder="排序" clearable style="width:130px" @change="loadData">
        <el-option label="最新" value="" /><el-option label="销量优先" value="sales" /><el-option label="价格升序" value="price_asc" /><el-option label="价格降序" value="price_desc" />
      </el-select>
      <el-button type="primary" @click="loadData()">搜索</el-button>
    </div>
    <el-row :gutter="16">
      <el-col :xs="12" :sm="8" :md="6" v-for="g in list" :key="g.goodsId">
        <el-card shadow="hover" style="cursor:pointer;margin-bottom:16px" @click="$router.push('/goods/'+g.goodsId)">
          <div style="height:180px;display:flex;align-items:center;justify-content:center;background:#f9f9f9;border-radius:4px;margin-bottom:12px">
            <el-image :src="g.img" style="max-height:160px;max-width:100%" fit="contain">
              <template #error><el-icon :size="40" style="color:#ddd"><Picture /></el-icon></template>
            </el-image>
          </div>
          <div style="font-size:14px;font-weight:500;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ g.title }}</div>
          <p style="color:#999;font-size:12px;margin:6px 0;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ g.description }}</p>
          <div style="display:flex;justify-content:space-between;align-items:center">
            <span style="color:#f56c6c;font-size:18px;font-weight:bold">¥{{ g.price }}</span>
            <span style="color:#ccc;font-size:12px;text-decoration:line-through">¥{{ g.priceAgo }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div style="display:flex;justify-content:center">
      <el-pagination v-model:current-page="page" v-model:page-size="size" :total="total" layout="prev,pager,next" @change="loadData" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import request from '../../utils/request'

const route = useRoute()
const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(12)
const types = ref([])
const search = reactive({ title: '', type: route.query.type ? Number(route.query.type) : null, sort: '' })

import { ElMessage } from 'element-plus'

const loadData = async () => {
  const params = { page: page.value, size: size.value }
  if (search.title.trim()) params.title = search.title
  if (search.type) params.type = search.type
  if (search.sort) params.sort = search.sort
  const res = await request.get('/api/goods/list', { params })
  if (typeof res.data === 'string') {
    ElMessage.info(res.data)
    list.value = []
    total.value = 0
    return
  }
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

onMounted(async () => {
  const res = await request.get('/api/goods/type/list')
  types.value = res.data || []
  loadData()
})
</script>
