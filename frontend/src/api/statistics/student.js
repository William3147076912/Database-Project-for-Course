import request from '@/utils/request'

// 查询课程管理列表
export function listStudent(query) {
    return request({
        url: '/userStatistics/listStudent',
        method: 'get',
        params: query
    })
}
