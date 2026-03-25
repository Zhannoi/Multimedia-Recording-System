import axios from 'axios'

const request = axios.create({
  baseURL: 'http://localhost:8080/api'
})

// 自动带 token
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.token = token
  }
  return config
})

export default request