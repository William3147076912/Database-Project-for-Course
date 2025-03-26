import request from '@/utils/request'

// 查询课程管理列表
export function listTeacher(query) {
    return request({
        url: '/userStatistics/listTeacher',
        method: 'get',
        params: query
    })
}
