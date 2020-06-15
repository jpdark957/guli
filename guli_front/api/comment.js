import request from '@/utils/request'
export default {
  getPageList(page, size, courseId) {
    return request({
      url: `/eduservice/comment/pageComment/${page}/${size}/${courseId}`,
      method: 'get',
    })
  },
  addComment(comment) {
    return request({
      url: `/eduservice/comment/addComment`,
      method: 'post',
      data: comment
    })
  },
  aa() {
    return request({
      url: `/educenter/member/aa`,
      method: 'get',
    })
  }
}