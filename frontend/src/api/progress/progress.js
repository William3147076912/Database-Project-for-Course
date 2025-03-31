import request from '@/utils/request'

// 查询学习进度列表
export function listProgress(query) {
  return request({
    url: '/progress/progress/list',
    method: 'get',
    params: query
  })
}

// 查询学习进度详细
export function getProgress(courseId) {
  return request({
    url: '/progress/progress/' + courseId,
    method: 'get'
  })
}

// 新增学习进度
export function addProgress(data) {
  return request({
    url: '/progress/progress',
    method: 'post',
    data: data
  })
}

// 修改学习进度
export function updateProgress(data) {
  return request({
    url: '/progress/progress',
    method: 'put',
    data: data
  })
}

// 删除学习进度
export function delProgress(courseId) {
  return request({
    url: '/progress/progress/' + courseId,
    method: 'delete'
  })
}
