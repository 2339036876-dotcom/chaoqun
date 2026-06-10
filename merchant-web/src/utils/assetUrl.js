/**
 * 将后端返回的相对路径（如 /uploads/xxx）转为可访问的绝对地址。
 * 开发环境可在 .env.development 中设置 VITE_API_BASE=http://localhost:8082，避免仅依赖代理时图片不显示。
 */
export function assetUrl(path) {
  if (!path) return ''
  if (/^https?:\/\//i.test(path)) return path
  const base = import.meta.env.VITE_API_BASE || ''
  if (!base) return path
  return `${base.replace(/\/$/, '')}${path.startsWith('/') ? path : `/${path}`}`
}
