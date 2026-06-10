<template>
  <el-card>
    <div style="display:flex;justify-content:space-between;margin-bottom:16px">
      <div style="display:flex;gap:10px">
        <el-input v-model="search.title" placeholder="搜索商品名称" clearable style="width:200px" @clear="loadData" />
        <el-select v-model="search.type" placeholder="商品分类" clearable style="width:150px" @change="loadData">
          <el-option v-for="t in types" :key="t.typeId" :label="t.name" :value="t.typeId" />
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
      <el-button type="success" @click="$router.push('/goods/add')">添加商品</el-button>
    </div>
    <el-table :data="list" stripe v-loading="loading">
      <el-table-column prop="goodsId" label="ID" width="60" />
      <el-table-column label="封面" width="80">
        <template #default="{row}"><el-image :src="row.img" style="width:50px;height:50px" fit="cover" /></template>
      </el-table-column>
      <el-table-column prop="title" label="商品名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="price" label="售价" width="100">
        <template #default="{row}">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="inventory" label="库存" width="80" />
      <el-table-column prop="sales" label="销量" width="80" />
      <el-table-column prop="hits" label="点击量" width="80" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{row}">
          <el-button size="small" @click="$router.push('/goods/edit/'+row.goodsId)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.goodsId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" v-model:current-page="page" v-model:page-size="size" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @change="loadData" @size-change="loadData" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const list = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)
const types = ref([])
const search = reactive({ title: '', type: null })

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/goods/list', { params: { page: page.value, size: size.value, ...search } })
  list.value = res.data?.list || []
  total.value = res.data?.total || 0
  loading.value = false
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该商品?', '提示', { type: 'warning' }).then(async () => {
    const res = await request.delete('/api/goods/delete/' + id)
    if (res.code === 0) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  }).catch(() => {})
}

onMounted(async () => {
  loadData()
  const res = await request.get('/api/goods/type/list')
  if (res.code === 0) types.value = res.data || []
})
</script>
