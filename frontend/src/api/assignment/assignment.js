import request from '@/utils/request'

// 查询作业/考试列表
export function listAssignment(query) {
  return request({
    url: '/assignment/assignment/list',
    method: 'get',
    params: query
  })
}

// 查询作业/考试详细
export function getAssignment(assignmentId) {
  return request({
    url: '/assignment/assignment/' + assignmentId,
    method: 'get'
  })
}

// 新增作业/考试
export function addAssignment(data) {
  return request({
    url: '/assignment/assignment',
    method: 'post',
    data: data
  })
}

// 修改作业/考试
export function updateAssignment(data) {
  return request({
    url: '/assignment/assignment',
    method: 'put',
    data: data
  })
}

// 删除作业/考试
export function delAssignment(assignmentId) {
  return request({
    url: '/assignment/assignment/' + assignmentId,
    method: 'delete'
  })
}
