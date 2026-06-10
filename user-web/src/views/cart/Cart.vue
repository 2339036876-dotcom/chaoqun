<template>
  <div style="background:#fff;padding:24px;border-radius:8px">
    <h2 style="margin:0 0 20px">购物车</h2>
    <el-table :data="list" v-loading="loading" style="margin-bottom:20px">
      <el-table-column label="商品" min-width="300">
        <template #default="{row}">
          <div style="display:flex;align-items:center;gap:12px">
            <el-image :src="row.goods?.img" style="width:60px;height:60px" fit="cover" />
            <span style="cursor:pointer;color:#409eff" @click="$router.push('/goods/'+row.cart.goodsId)">{{ row.goods?.title }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="单价" width="100"><template #default="{row}">¥{{ row.goods?.price }}</template></el-table-column>
      <el-table-column label="数量" width="150">
        <template #default="{row}">
          <el-input-number
            v-if="(row.goods?.inventory ?? 0) > 0"
            v-model="row.cart.num"
            :min="1"
            :max="row.goods.inventory"
            size="small"
            @change="() => updateCart(row)"
          />
          <span v-else style="color:#f56c6c;font-size:13px">缺货</span>
        </template>
      </el-table-column>
      <el-table-column label="小计" width="120"><template #default="{row}"><span style="color:#f56c6c;font-weight:bold">¥{{ (row.goods?.price * row.cart.num).toFixed(2) }}</span></template></el-table-column>
      <el-table-column label="操作" width="80"><template #default="{row}"><el-button type="danger" size="small" @click="removeItem(row.cart.cartId)">删除</el-button></template></el-table-column>
    </el-table>
    <div style="display:flex;justify-content:space-between;align-items:center">
      <el-button @click="clearCart">清空购物车</el-button>
      <div>
        <span style="margin-right:16px">共 {{ list.length }} 件商品，合计: <span style="color:#f56c6c;font-size:24px;font-weight:bold">¥{{ totalPrice }}</span></span>
        <el-button type="danger" size="large" @click="goCheckout" :disabled="!list.length">去结算</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const router = useRouter()
const list = ref([])
const loading = ref(false)

const totalPrice = computed(() => list.value.reduce((s, r) => s + (r.goods?.price || 0) * r.cart.num, 0).toFixed(2))

const loadData = async () => {
  loading.value = true
  const res = await request.get('/api/cart/list')
  list.value = res.data || []
  loading.value = false
}

const updateCart = async (row) => {
  const inv = row.goods?.inventory ?? 0
  if (inv <= 0) return
  if (row.cart.num > inv) {
    row.cart.num = inv
    ElMessage.warning('已超过当前库存，已为您调整为可购买数量')
  }
  const res = await request.put('/api/cart/update', row.cart)
  if (res.code !== 0) {
    ElMessage.error(res.msg || '更新失败')
    const r = await request.get('/api/cart/list')
    list.value = r.data || []
  }
}
const removeItem = async (id) => { await request.delete('/api/cart/delete/' + id); ElMessage.success('已删除'); loadData() }
const clearCart = () => { ElMessageBox.confirm('确定清空?').then(async () => { await request.delete('/api/cart/clear'); loadData() }).catch(() => {}) }
const goCheckout = () => {
  const bad = list.value.filter(r => !r.goods || (r.goods.inventory ?? 0) < r.cart.num)
  if (bad.length) {
    ElMessage.warning('部分商品库存不足或已下架，请调整数量或删除后再结算')
    return
  }
  router.push('/checkout')
}

onMounted(loadData)
</script>
