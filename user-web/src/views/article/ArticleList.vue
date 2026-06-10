<template>
  <div>
    <el-card v-for="a in list" :key="a.articleId" shadow="hover" style="margin-bottom:12px;cursor:pointer" @click="$router.push('/article/'+a.articleId)">
      <div style="display:flex;gap:16px">
        <el-image v-if="a.img" :src="a.img" style="width:160px;height:100px;flex-shrink:0;border-radius:4px" fit="cover" />
        <div>
          <h3 style="margin:0 0 8px;font-size:16px">{{ a.title }}</h3>
          <p style="color:#999;font-size:13px;margin:0;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden">{{ a.description }}</p>
          <div style="color:#bbb;font-size:12px;margin-top:8px">浏览 {{ a.hits }} | {{ a.createTime }}</div>
        </div>
      </div>
    </el-card>
    <el-pagination style="justify-content:center" v-model:current-page="page" :total="total" layout="prev,pager,next" @change="loadData" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'

const list = ref([])
const total = ref(0)
const page = ref(1)

const loadData = async () => {
  const res = await request.get('/api/article/list', { params: { page: page.value, size: 10 } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

onMounted(loadData)
</script>
