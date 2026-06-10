import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import { assetUrl } from './assetUrl'

function rewriteUploadPaths(val) {
  if (val == null) return
  if (Array.isArray(val)) {
    val.forEach(rewriteUploadPaths)
    return
  }
  if (typeof val !== 'object') return
  for (const k of Object.keys(val)) {
    const v = val[k]
    if (typeof v === 'string' && v.startsWith('/uploads/')) {
      val[k] = assetUrl(v)
    } else if (v && typeof v === 'object') {
      rewriteUploadPaths(v)
    }
  }
}

const request = axios.create({
  baseURL: '',
  timeout: 30000
})

request.interceptors.request.use(config => {
  // 只在非登录接口添加token
  if (!config.url.includes('/login')) {
    const token = localStorage.getItem('merchant-token')
    if (token && token !== 'undefined') {
      config.headers['token'] = token
    }
  }
  return config
})

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 0 && res.data != null) {
      if (typeof res.data === 'string' && res.data.startsWith('/uploads/')) {
        res.data = assetUrl(res.data)
      } else {
        rewriteUploadPaths(res.data)
      }
    }
    if (res.code === 401) {
      ElMessage.error('请重新登录')
      localStorage.removeItem('merchant-token')
      localStorage.removeItem('merchant-user')
      router.push('/login')
      return Promise.reject(res)
    }
    return res
  },
  error => {
    if (error.response && error.response.status === 401) {
      ElMessage.error('请重新登录')
      localStorage.removeItem('merchant-token')
      localStorage.removeItem('merchant-user')
      router.push('/login')
    } else {
      ElMessage.error('网络错误')
    }
    return Promise.reject(error)
  }
)

export default request
