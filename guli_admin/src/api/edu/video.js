import request from '@/utils/request'
export default {
  //添加小节
  addVideo(video) {
    return request({
      url: '/eduservice/video/addVideo',
      method: 'POST',
      data: video
    })
  },
  //删除小节
  deleteVideo(videoId) {
    return request({
      url: `/eduservice/video/deleteVideo/${videoId}`,
      method: 'DELETE',
    })
  },
  //修改小节
  updateVideo(video) {
    return request({
      url: `/eduservice/video/updateVideo`,
      method: 'POST',
      data: video
    })
  },
  //根据小节id查询数据
  getVideoInfo(videoId) {
    return request({
      url: `/eduservice/video/getVideoInfo/${videoId}`,
      method: 'GET'
    })
  },
  deleteAlyvod(id) {
    return request({
      url: `/eduvod/video/removeAlyVideo/${id}`,
      method: 'DELETE'
    })
  },
  
}