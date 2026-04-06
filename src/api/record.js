import request from './request'

// 获取记录列表（根据类型）
export const getRecords = (type, userId) => {
  switch(type) {
    case 'movie':
      return request.get('/movie/list', { params: { userId } })
    case 'book':
      return request.get('/book/list', { params: { userId } })
    case 'game':
      return request.get('/game/list', { params: { userId } })
    case 'anime':
      return request.get('/anime/list', { params: { userId } })
    default:
      return Promise.reject(new Error('Invalid type'))
  }
}

// 添加记录
export const addRecord = (type, data) => {
  switch(type) {
    case 'movie':
      return request.post('/movie/add', data)
    case 'book':
      return request.post('/book/add', data)
    case 'game':
      return request.post('/game/add', data)
    case 'anime':
      return request.post('/anime/add', data)
    default:
      return Promise.reject(new Error('Invalid type'))
  }
}

// 更新记录
export const updateRecord = (type, id, data) => {
  switch(type) {
    case 'movie':
      return request.put('/movie/update', data)
    case 'book':
      return request.put('/book/update', data)
    case 'game':
      return request.put('/game/update', data)
    case 'anime':
      return request.put('/anime/update', data)
    default:
      return Promise.reject(new Error('Invalid type'))
  }
}

// 删除记录
export const deleteRecord = (type, id) => {
  switch(type) {
    case 'movie':
      return request.delete(`/movie/${id}`)
    case 'book':
      return request.delete(`/book/${id}`)
    case 'game':
      return request.delete(`/game/${id}`)
    case 'anime':
      return request.delete(`/anime/${id}`)
    default:
      return Promise.reject(new Error('Invalid type'))
  }
}

// 搜索记录
export const searchRecords = (keyword) => {
  return request.get('/record/search', { params: { keyword } })
}