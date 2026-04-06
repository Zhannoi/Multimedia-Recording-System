import request from './request'

// 用户注册
export const userRegister = (data) => {
  return request.post('/user/register', data)
}

// 用户登录
export const userLogin = (data) => {
  return request.post('/user/login', data)
}