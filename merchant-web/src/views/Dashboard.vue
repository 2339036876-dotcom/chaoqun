<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col v-for="item in stats" :key="item.title" :span="6">
        <el-card shadow="hover">
          <div style="display: flex; align-items: center; justify-content: space-between">
            <div>
              <div style="color: #999; font-size: 14px">{{ item.title }}</div>
              <div style="font-size: 28px; font-weight: 700; margin-top: 8px">{{ item.value }}</div>
            </div>
            <el-icon :size="40" :color="item.color"><component :is="item.icon" /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card header="最近订单">
          <el-table :data="recentOrders" stripe size="small">
            <el-table-column prop="orderNumber" label="订单号" />
            <el-table-column prop="priceCount" label="金额" width="120">
              <template #default="{ row }">￥{{ row.priceCount }}</template>
            </el-table-column>
            <el-table-column prop="state" label="状态" width="110">
              <template #default="{ row }"><el-tag size="small">{{ row.state }}</el-tag></template>
            </el-table-column>
            <el-table-column prop="createTime" label="时间" width="180" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card header="商品分类统计（按在售商品数量）">
          <div ref="chartRef" style="height: 320px"></div>
          <p v-if="categoryHint" style="margin: 8px 0 0; font-size: 12px; color: #909399">{{ categoryHint }}</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, ref } from 'vue'
import * as echarts from 'echarts'
import request from '../utils/request'

const stats = ref([
  { title: '商品总数', value: 0, icon: 'Goods', color: '#409eff' },
  { title: '订单总数', value: 0, icon: 'Document', color: '#67c23a' },
  { title: '用户总数', value: 0, icon: 'User', color: '#e6a23c' },
  { title: '待处理订单', value: 0, icon: 'Bell', color: '#f56c6c' }
])
const recentOrders = ref([])
const chartRef = ref(null)
const categoryHint = ref('')
let chartInstance = null
let resizeHandler = null

const buildCategoryChart = (rawList) => {
  if (!chartRef.value) return
  const list = rawList || []
  const pieData = list
    .filter((item) => Number(item.count) > 0)
    .map((item) => ({
      name: item.name || '未命名分类',
      value: Number(item.count)
    }))

  if (!chartInstance) {
    chartInstance = echarts.init(chartRef.value)
  }

  if (!pieData.length) {
    categoryHint.value = '当前各分类下暂无商品，上架商品后图表会自动更新。'
    chartInstance.setOption({
      title: {
        text: '暂无数据',
        left: 'center',
        top: 'center',
        textStyle: { color: '#909399', fontSize: 14 }
      },
      series: [{ type: 'pie', radius: ['40%', '70%'], data: [] }]
    })
    return
  }

  categoryHint.value = ''
  chartInstance.setOption({
    title: { show: false },
    tooltip: {
      trigger: 'item',
      formatter: (p) => `${p.marker}${p.name}<br/>商品数：${p.value}（${p.percent}%）`
    },
    legend: {
      type: 'scroll',
      orient: 'horizontal',
      bottom: 0,
      itemWidth: 10,
      itemHeight: 10,
      textStyle: { fontSize: 11 }
    },
    series: [
      {
        name: '商品数',
        type: 'pie',
        radius: ['42%', '72%'],
        center: ['50%', '44%'],
        avoidLabelOverlap: true,
        itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 1 },
        label: {
          formatter: (p) => {
            const name = p.name.length > 6 ? p.name.slice(0, 5) + '…' : p.name
            return `${name}\n${p.value}件`
          },
          fontSize: 11
        },
        labelLine: { length: 12, length2: 8 },
        data: pieData
      }
    ]
  })
}

const loadDashboard = async () => {
  const [goodsRes, orderRes, userRes, pendingRes, categoryRes] = await Promise.all([
    request.get('/api/goods/list', { params: { page: 1, size: 1 } }),
    request.get('/api/order/list', { params: { page: 1, size: 5 } }),
    request.get('/api/user/list', { params: { page: 1, size: 1 } }),
    request.get('/api/order/list', { params: { page: 1, size: 1, state: '待付款' } }),
    request.get('/api/goods/type/stats/count-by-type')
  ])

  stats.value[0].value = goodsRes.data?.total || 0
  stats.value[1].value = orderRes.data?.total || 0
  stats.value[2].value = userRes.data?.total || 0
  stats.value[3].value = pendingRes.data?.total || 0
  recentOrders.value = orderRes.data?.list || []

  const categoryList = categoryRes.code === 0 ? categoryRes.data || [] : []
  buildCategoryChart(categoryList)
}

onMounted(async () => {
  await loadDashboard()
  resizeHandler = () => chartInstance && chartInstance.resize()
  window.addEventListener('resize', resizeHandler)
})

onBeforeUnmount(() => {
  if (resizeHandler) window.removeEventListener('resize', resizeHandler)
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>
