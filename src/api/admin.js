import request from './request'

// 管理员登录
export const adminLogin = (data) => {
  return request.post('/admin/login', data)
}

// 获取所有用户
export const getAllUsers = () => {
  return request.get('/admin/users')
}

// 删除用户
export const deleteUser = (id) => {
  return request.delete(`/admin/user/${id}`)
}

// 系统统计
export const getSystemStats = () => {
  return request.get('/admin/stats')
}