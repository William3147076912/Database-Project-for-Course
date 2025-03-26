import request from '@/utils/request'

export function getVideo(videoUrl){
    return request({
        url: videoUrl,
        method: 'get',
        responseType: 'blob'
    })
}