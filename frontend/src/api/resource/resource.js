import request from '@/utils/request'

// 查询教学资源列表
export function listResource(query) {
  return request({
    url: '/resource/resource/list',
    method: 'get',
    params: query
  })
}

// 查询教学资源详细
export function getResource(resourceId) {
  return request({
    url: '/resource/resource/' + resourceId,
    method: 'get'
  })
}

// 新增教学资源
export function addResource(data) {
  return request({
    url: '/resource/resource',
    method: 'post',
    data: data
  })
}

// 修改教学资源
export function updateResource(data) {
  return request({
    url: '/resource/resource',
    method: 'put',
    data: data
  })
}

// 删除教学资源
export function delResource(resourceId) {
  return request({
    url: '/resource/resource/' + resourceId,
    method: 'delete'
  })
}
