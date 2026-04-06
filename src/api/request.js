import axios from 'axios'

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000, // 10秒超时
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器 - 添加token
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}` // 使用标准的Authorization头
  }
  console.log('发送请求:', config.method?.toUpperCase(), config.url)
  return config
}, error => {
  console.error('请求拦截器错误:', error)
  return Promise.reject(error)
})

// 响应拦截器 - 统一处理响应
request.interceptors.response.use(
  response => {
    console.log('收到响应:', response.status, response.config.url)
    return response
  },
  error => {
    console.error('响应拦截器错误:', error)
    
    // 处理网络错误
    if (!error.response) {
      alert('网络连接失败，请检查网络或服务器状态')
      return Promise.reject(error)
    }
    
    // 处理HTTP状态码错误
    switch (error.response.status) {
      case 401:
        alert('未授权，请重新登录')
        localStorage.removeItem('token')
        window.location.href = '/login'
        break
      case 403:
        alert('禁止访问')
        break
      case 404:
        alert('请求的资源不存在')
        break
      case 500:
        alert('服务器内部错误')
        break
      default:
        alert(`请求失败: ${error.response.status}`)
    }
    
    return Promise.reject(error)
  }
)

export default request