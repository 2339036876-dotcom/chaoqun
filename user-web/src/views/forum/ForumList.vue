<template>
  <div>
    <div style="display:flex;justify-content:space-between;margin-bottom:16px">
      <div style="display:flex;gap:10px">
        <el-input v-model="searchTitle" placeholder="搜索帖子" clearable style="width:220px" @keyup.enter="loadData" />
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
      <el-button type="success" @click="$router.push('/forum/post')">发帖</el-button>
    </div>
    <el-card v-for="f in list" :key="f.forumId" shadow="hover" style="margin-bottom:12px;cursor:pointer" @click="$router.push('/forum/'+f.forumId)">
      <div style="display:flex;justify-content:space-between;align-items:center">
        <div>
          <el-tag v-if="f.istop" type="danger" size="small" style="margin-right:8px">置顶</el-tag>
          <span style="font-size:16px;font-weight:500">{{ f.title }}</span>
        </div>
        <div style="color:#999;font-size:13px">
          <span style="margin-right:16px">{{ f.nickname }}</span>
          <span style="margin-right:16px">浏览 {{ f.hits }}</span>
          <span>{{ f.createTime }}</span>
        </div>
      </div>
    </el-card>
    <el-pagination style="justify-content:center;margin-top:16px" v-model:current-page="page" :total="total" layout="prev,pager,next" @change="loadData" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'

const list = ref([])
const total = ref(0)
const page = ref(1)
const searchTitle = ref('')

const loadData = async () => {
  const res = await request.get('/api/forum/list', { params: { page: page.value, size: 15, title: searchTitle.value || undefined } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

onMounted(loadData)
</script>
