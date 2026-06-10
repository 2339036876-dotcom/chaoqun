import axios from 'axios'
import { assetUrl } from './assetUrl'

export const uploadFile = async (file) => {
  const formData = new FormData()
  formData.append('file', file)
  const token = localStorage.getItem('merchant-token')
  const { data } = await axios.post('/api/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...(token ? { token } : {})
    }
  })
  if (data.code === 0 && typeof data.data === 'string' && data.data.startsWith('/uploads/')) {
    data.data = assetUrl(data.data)
  }
  return data
}
