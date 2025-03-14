import request from '@/utils/request'

// 查询作业提交记录列表
export function listSubmission(query) {
  return request({
    url: '/submission/submission/list',
    method: 'get',
    params: query
  })
}

// 查询作业提交记录详细
export function getSubmission(submissionId) {
  return request({
    url: '/submission/submission/' + submissionId,
    method: 'get'
  })
}

// 新增作业提交记录
export function addSubmission(data) {
  return request({
    url: '/submission/submission',
    method: 'post',
    data: data
  })
}

// 修改作业提交记录
export function updateSubmission(data) {
  return request({
    url: '/submission/submission',
    method: 'put',
    data: data
  })
}

// 删除作业提交记录
export function delSubmission(submissionId) {
  return request({
    url: '/submission/submission/' + submissionId,
    method: 'delete'
  })
}
