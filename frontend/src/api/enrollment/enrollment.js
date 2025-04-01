import request from '@/utils/request'

// 查询选课记录列表
export function listEnrollment(query) {
  return request({
    url: '/enrollment/enrollment/list',
    method: 'get',
    params: query
  })
}

// 查询选课记录详细
export function getEnrollment(enrollmentId) {
  return request({
    url: '/enrollment/enrollment/' + enrollmentId,
    method: 'get'
  })
}

// 新增选课记录
export function addEnrollment(data) {
  return request({
    url: '/enrollment/enrollment',
    method: 'post',
    data: data
  })
}

// 修改选课记录
export function updateEnrollment(data) {
  return request({
    url: '/enrollment/enrollment',
    method: 'put',
    data: data
  })
}

// 删除选课记录
export function delEnrollment(enrollmentId) {
  return request({
    url: '/enrollment/enrollment/' + enrollmentId,
    method: 'delete'
  })
}
// 根据courseId删除选课记录
export function delEnrollmentByCourseId(params) {
  return request({
    url: '/enrollment/enrollment/course',
    method: 'delete',
    params: params
  })
}
