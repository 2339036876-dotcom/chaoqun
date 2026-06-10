<template>
  <div style="background:#fff;padding:24px;border-radius:8px">
    <h2 style="margin:0 0 20px">确认订单</h2>
    <h4>收货地址</h4>
    <el-radio-group v-model="selectedAddr" style="display:flex;flex-direction:column;gap:8px;margin-bottom:20px">
      <el-radio v-for="a in addresses" :key="a.addressId" :label="a.addressId" border>{{ a.name }} {{ a.phone }} - {{ a.address }}</el-radio>
    </el-radio-group>
    <h4>商品信息</h4>
    <el-table :data="orderItems" style="margin-bottom:20px">
      <el-table-column prop="title" label="商品" />
      <el-table-column prop="price" label="单价" width="100"><template #default="{row}">¥{{ row.price }}</template></el-table-column>
      <el-table-column prop="num" label="数量" width="80" />
      <el-table-column label="小计" width="120"><template #default="{row}"><span style="color:#f56c6c">¥{{ (row.price*row.num).toFixed(2) }}</span></template></el-table-column>
    </el-table>
    <el-form-item label="备注"><el-input v-model="remark" placeholder="订单备注(可选)" style="width:400px" /></el-form-item>
    <div style="text-align:right;margin-top:20px">
      <span style="margin-right:20px;font-size:18px">总计: <span style="color:#f56c6c;font-weight:bold;font-size:24px">¥{{ totalPrice }}</span></span>
      <el-button type="danger" size="large" @click="submitOrder" :loading="submitting">提交订单</el-button>
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
const addresses = ref([])
const selectedAddr = ref(null)
const orderItems = ref([])
const remark = ref('')
const submitting = ref(false)

const totalPrice = computed(() => orderItems.value.reduce((s, i) => s + i.price * i.num, 0).toFixed(2))

onMounted(async () => {
  const addrRes = await request.get('/api/address/list')
  addresses.value = addrRes.data || []
  const def = addresses.value.find(a => a.isDefault)
  if (def) selectedAddr.value = def.addressId
  else if (addresses.value.length) selectedAddr.value = addresses.value[0].addressId

  if (route.query.goodsId) {
    const res = await request.get('/api/goods/detail/' + route.query.goodsId)
    if (res.code === 0) {
      orderItems.value = [{ goodsId: res.data.goodsId, title: res.data.title, price: res.data.price, num: Number(route.query.num) || 1 }]
    }
  } else {
    const res = await request.get('/api/cart/list')
    const rows = res.data || []
    orderItems.value = rows
      .filter(r => r.goods && r.cart)
      .map(r => ({
        goodsId: r.cart.goodsId,
        title: r.goods.title,
        price: r.goods.price,
        num: r.cart.num,
        inventory: r.goods.inventory
      }))
    const bad = rows.filter(r => !r.goods)
    if (bad.length) {
      ElMessage.warning('购物车中有已下架商品，请返回购物车删除后再结算')
    }
  }
})

const submitOrder = async () => {
  if (!selectedAddr.value) { ElMessage.warning('请选择收货地址'); return }
  const addr = addresses.value.find(a => a.addressId === selectedAddr.value)
  submitting.value = true
  try {
    if (route.query.goodsId) {
      const item = orderItems.value[0]
      if (!item?.goodsId) {
        ElMessage.error('商品信息异常')
        return
      }
      const res = await request.post('/api/order/add', {
        goodsId: item.goodsId,
        num: item.num,
        contactName: addr.name,
        contactPhone: addr.phone,
        contactAddress: addr.address,
        remark: remark.value
      })
      if (res.code !== 0) {
        ElMessage.error(res.msg || '下单失败')
        return
      }
    } else {
      const res = await request.post('/api/order/checkout-cart', {
        contactName: addr.name,
        contactPhone: addr.phone,
        contactAddress: addr.address,
        remark: remark.value
      })
      if (res.code !== 0) {
        ElMessage.error(res.msg || '下单失败')
        return
      }
    }
    ElMessage.success('下单成功')
    router.push('/order')
  } finally {
    submitting.value = false
  }
}
</script>
