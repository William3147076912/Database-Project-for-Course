import request from '@/utils/request'

// 查询成绩列表
export function listGrade(query) {
  return request({
    url: '/grade/grade/list',
    method: 'get',
    params: query
  })
}

// 查询成绩详细
export function getGrade(submissionId) {
  return request({
    url: '/grade/grade/' + submissionId,
    method: 'get'
  })
}

// 新增成绩
export function addGrade(data) {
  return request({
    url: '/grade/grade',
    method: 'post',
    data: data
  })
}

// 修改成绩
export function updateGrade(data) {
  return request({
    url: '/grade/grade',
    method: 'put',
    data: data
  })
}

// 删除成绩
export function delGrade(submissionId) {
  return request({
    url: '/grade/grade/' + submissionId,
    method: 'delete'
  })
}
