<template>
  <div style="background:#fff;padding:24px;border-radius:8px">
    <h2 style="margin:0 0 20px">我的收藏</h2>
    <el-row :gutter="16">
      <el-col :span="6" v-for="c in list" :key="c.collectId">
        <el-card shadow="hover" style="margin-bottom:16px">
          <el-image :src="c.img" style="width:100%;height:120px" fit="cover" />
          <div style="padding:8px 0">
            <div style="font-size:14px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ c.title }}</div>
            <div style="display:flex;justify-content:space-between;margin-top:8px">
              <el-button size="small" @click="$router.push('/goods/'+c.sourceId)">查看</el-button>
              <el-button size="small" type="danger" @click="handleDelete(c.collectId)">取消</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-pagination style="justify-content:center" v-model:current-page="page" :total="total" layout="prev,pager,next" @change="loadData" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'

const list = ref([])
const total = ref(0)
const page = ref(1)

const loadData = async () => {
  const res = await request.get('/api/collect/list', { params: { page: page.value, size: 12 } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
}

const handleDelete = async (id) => {
  await request.delete('/api/collect/delete/' + id)
  ElMessage.success('已取消收藏')
  loadData()
}

onMounted(loadData)
</script>
