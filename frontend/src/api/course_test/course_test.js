import request from '@/utils/request'

// 查询课程列表
export function listCourse_test(query) {
  return request({
    url: '/course_test/course_test/list',
    method: 'get',
    params: query
  })
}

// 查询课程详细
export function getCourse_test(courseId) {
  return request({
    url: '/course_test/course_test/' + courseId,
    method: 'get'
  })
}

// 新增课程
export function addCourse_test(data) {
  return request({
    url: '/course_test/course_test',
    method: 'post',
    data: data
  })
}

// 修改课程
export function updateCourse_test(data) {
  return request({
    url: '/course_test/course_test',
    method: 'put',
    data: data
  })
}

// 删除课程
export function delCourse_test(courseId) {
  return request({
    url: '/course_test/course_test/' + courseId,
    method: 'delete'
  })
}
